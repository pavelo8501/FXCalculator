package lv.fx.calculator.controller.api.admin

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import lv.fx.calculator.services.db.RateService
import lv.fx.calculator.model.data.ExRate
import lv.fx.calculator.model.entity.FeeEntity
import lv.fx.calculator.model.entity.RateEntity
import lv.fx.calculator.services.db.FeeService
import lv.fx.calculator.services.http.RateParser
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
class FeeController(
    private val feeService: FeeService,
    private val rateService: RateService
) {

    private val adminScope: CoroutineScope = CoroutineScope(
        Job() + Dispatchers.IO + CoroutineName("FeeController Coroutine") )

    @GetMapping
    fun getAllFees(): List<FeeEntity> = feeService.select()

    @PutMapping(value = ["/save"])
    suspend fun saveFee(
        @RequestParam("fromCurrencyId") fromCurrencyId: Int,
        @RequestParam("toCurrencyId") toCurrencyId: Int,
        @RequestParam("fee") fee: Double
    ): FeeEntity {
        val fromCurrency = rateService.pick(fromCurrencyId)
        val toCurrency = rateService.pick(toCurrencyId)
        if(fromCurrency != null && toCurrency != null){
            return feeService.update(FeeEntity(fromCurrency,toCurrency,fee))
        }else{
            throw IllegalArgumentException("Currency pair not found")
        }
    }

    @PatchMapping(value = ["/update"])
    suspend fun updateFee(
        @RequestParam("id") id: Int,
        @RequestParam("fee") fee: Double
    ): FeeEntity {
        val feeEntity = feeService.pick(id)
        if(feeEntity != null){
            feeEntity.fee = fee
            return feeService.update(feeEntity)
        }else{
            throw IllegalArgumentException("Fee not found")
        }
    }

    @DeleteMapping(value = ["/delete"])
    suspend fun deleteFee(
        @RequestParam("id") id: Int
    ): Boolean {
        return feeService.delete(id)
    }

}