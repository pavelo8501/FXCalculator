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
import lv.fx.calculator.model.entity.RateEntity
import lv.fx.calculator.services.http.RateParser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/admin/api/rates")
class RateController(
    private val rateService: RateService,
    private val rateParser: RateParser
) {

    private val adminScope: CoroutineScope = CoroutineScope(
        Job() + Dispatchers.IO + CoroutineName("RatesController Coroutine")
    )

    @GetMapping
    fun getAllRates(): List<RateEntity> = rateService.select()

    @PostMapping(value = ["/update"])
    suspend fun updateRates(): List<ExRate> {

        val resultListDeferred = CompletableDeferred<List<ExRate>>()
        val resultList = mutableListOf<ExRate>()
        adminScope.launch {

            val rateEntitiesDeferred = async {
                rateService.select()
            }

            val rateParserResponseDeferred = async {
                rateParser.fetchCurrencySuspended()
            }

            val rateEntities = rateEntitiesDeferred.await()
            val response = rateParserResponseDeferred.await()
            if (response.ok) {
                response.result?.forEach {
                    val foundEntity = rateEntities.firstOrNull { entity -> entity.currency == it.currency }
                    if (foundEntity != null) {
                        if (foundEntity.rate != it.rate) {
                            foundEntity.rate = it.rate
                            rateService.update(foundEntity)
                        }
                        it.id = foundEntity.id
                        resultList.add(it)
                    } else {
                        val newEntity = RateEntity(it.id, it.currency, it.rate)
                        rateService.insert(newEntity)
                        resultList.add(rateService.toData(newEntity))
                    }
                }
            }
            resultListDeferred.complete(resultList.toList())
        }
        return resultListDeferred.await()
    }
}