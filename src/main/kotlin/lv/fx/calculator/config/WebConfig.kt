package lv.fx.calculator.config

import com.fasterxml.jackson.databind.ObjectMapper
import lv.fx.calculator.interceptor.ApiVersionInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.server.WebFilter

@Configuration
class WebConfig {

    @Bean
    fun versionInterceptor(): WebFilter {
        return ApiVersionInterceptor()
    }
}