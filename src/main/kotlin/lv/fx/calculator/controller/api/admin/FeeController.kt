package lv.fx.calculator.controller.api.admin

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import lv.fx.calculator.model.data.BooleanResponse
import lv.fx.calculator.model.data.ExFee
import lv.fx.calculator.services.db.RateService
import lv.fx.calculator.model.data.ExRate
import lv.fx.calculator.model.data.ListResponse
import lv.fx.calculator.model.data.SingleResponse
import lv.fx.calculator.model.entity.FeeEntity
import lv.fx.calculator.model.entity.RateEntity
import lv.fx.calculator.services.db.FeeService
import lv.fx.calculator.services.http.RateParser
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController



@RestController
@RequestMapping("/admin/api/fees")
@CrossOrigin(origins = ["http://localhost:4200"])
class FeeController(
    private val feeService: FeeService,
    private val rateService: RateService
) {

    private val adminScope: CoroutineScope = CoroutineScope(
        Job() + Dispatchers.IO + CoroutineName("FeeController Coroutine") )

    @GetMapping
    suspend fun getAllFees(): ListResponse<ExFee> {
        val result = feeService.select().map { ExFee(it.id, rateService.toData(it.fromCurrency), rateService.toData(it.toCurrency), it.fee)}
        return ListResponse<ExFee>(true).also { it.setData(result) }
    }

    @PutMapping(value = ["/save"])
    suspend fun saveFee(
        @RequestParam("fromCurrencyId") fromCurrencyId: Int,
        @RequestParam("toCurrencyId") toCurrencyId: Int,
        @RequestParam("fee") fee: Double
    ): SingleResponse<ExFee> {
        val fromCurrency = rateService.pick(fromCurrencyId)
        val toCurrency = rateService.pick(toCurrencyId)
        if(fromCurrency != null && toCurrency != null){
            val result = feeService.update(FeeEntity(0,fromCurrency,toCurrency,fee)).let {
                ExFee(it.id,rateService.toData(it.fromCurrency),rateService.toData(it.toCurrency),it.fee )
            }
            return SingleResponse<ExFee>(true).also { it.setData(result) }
        }else{
            throw IllegalArgumentException("Currency pair not found")
        }
    }

    @PatchMapping(value = ["/update"])
    suspend fun updateFee(
        @RequestParam("id") id: Int,
        @RequestParam("fee") fee: Double
    ): BooleanResponse {
        val feeEntity = feeService.pick(id)
        if(feeEntity != null){
            feeEntity.fee = fee
            try {
                val result = feeService.update(feeEntity)
                return BooleanResponse(true)
            }catch (e: Exception){
                return BooleanResponse(false)
            }
        }else{
            return BooleanResponse(false).also { it.setError(104,"Fee not found") }
        }
    }

    @DeleteMapping(value = ["/delete"])
    suspend fun deleteFee(
        @RequestParam("id") id: Int
    ): BooleanResponse {
        val response = BooleanResponse(feeService.delete(id))
        if(!response.result){
            response.setError(104,"Fee not found")
        }
        return response
    }

}