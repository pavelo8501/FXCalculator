package lv.fx.calculator.controller

import kotlinx.coroutines.runBlocking
import lv.fx.calculator.common.model.ExRate
import lv.fx.calculator.services.RateParser
import lv.fx.calculator.services.ServiceResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono


@RestController
@RequestMapping("/api")
class RateController(private val rateParser: RateParser) {

    @GetMapping("/rates")
    fun getRates(): Mono<ServiceResponse>{
      val res =  rateParser.fetchRates()
      return res
    }

}