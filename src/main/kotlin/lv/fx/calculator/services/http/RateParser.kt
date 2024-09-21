package lv.fx.calculator.services.http

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service
import lv.fx.calculator.model.data.RateModel
import lv.fx.calculator.model.entity.RateEntity
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.jvm.java


data class ServiceResponse(
    var ok: Boolean,
    var result: List<RateModel>? = null,
    var errorCode: Int? = null,
    val description: String? = null,
)

@Service
class RateParser(private val webClient: WebClient) {

    fun fetchRates(): Mono<ServiceResponse> {

       val rates = mutableListOf<RateModel>()
       return webClient.get()
            .uri("/stats/eurofxref/eurofxref-daily.xml")
            .retrieve()
            .bodyToMono(String::class.java)
            .flatMap{ xmlText ->
                val factory = DocumentBuilderFactory.newInstance()
                val builder = factory.newDocumentBuilder()
                val doc = builder.parse(xmlText.byteInputStream())
                val cubes = doc.getElementsByTagName("Cube")
                for(i in 0 until cubes.length){
                    val cube = cubes.item(i)
                    val currency = cube.attributes?.getNamedItem("currency")?.nodeValue
                    val rate = cube.attributes?.getNamedItem("rate")?.nodeValue
                    if (currency != null && rate != null) {

                        RateEntity(currency, rate.toDouble())

                        rates.add(RateModel(RateEntity(currency, rate.toDouble())))
                    }
                }
               Mono.just(ServiceResponse(ok = true, result = rates))
            }
    }

    suspend fun fetchCurrencySuspended(): ServiceResponse {
        val response = webClient.get()
            .uri("/stats/eurofxref/eurofxref-daily.xml")
            .retrieve()
            .bodyToMono(String::class.java)
            .awaitSingle()

        if(response != null){
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val doc = builder.parse(response.byteInputStream())
            val cubes = doc.getElementsByTagName("Cube")
            val rates = mutableListOf<RateModel>()
            for(i in 0 until cubes.length){
                val cube = cubes.item(i)
                val currency = cube.attributes?.getNamedItem("currency")?.nodeValue
                val rate = cube.attributes?.getNamedItem("rate")?.nodeValue
                if (currency != null && rate != null) {

                    rates.add(RateModel(RateEntity(currency, rate.toDouble())))
                }
            }
            return ServiceResponse(true).also { it.result = rates }
        }else{
            return ServiceResponse(false).also { it.errorCode = 500 }
        }
    }
}