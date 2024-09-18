package lv.fx.calculator.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient


@Configuration
class HttpClientConfig {

    @Bean
    fun webClient(builder: WebClient.Builder): WebClient {
        return builder
            .baseUrl("https://www.ecb.europa.eu")
            .build()
    }

}