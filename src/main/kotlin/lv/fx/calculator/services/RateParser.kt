package lv.fx.calculator.services

import org.springframework.stereotype.Service
import lv.fx.calculator.common.model.ExRate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.jvm.java


data class ServiceResponse(
    var ok: Boolean,
    val result: List<ExRate>? = null,
    var errorCode: Int? = null,
    val description: String? = null,
)

@Service
class RateParser(private val webClient: WebClient) {

    fun fetchRates(): Mono<ServiceResponse> {

       val rates = mutableListOf<ExRate>()
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
                        rates.add(ExRate(currency, rate.toDouble()))
                    }
                }
               Mono.just(ServiceResponse(ok = true, result = rates))
            }
    }
}