package lv.fx.calculator.controller.api.pub

import lv.fx.calculator.services.http.RateParser
import lv.fx.calculator.services.http.ServiceResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono


@RestController
@RequestMapping("/api")
class PubRateController(private val rateParser: RateParser) {

    @GetMapping("/rates")
    fun getRates(): Mono<ServiceResponse>{
      val res =  rateParser.fetchRates()
      return res
    }
}