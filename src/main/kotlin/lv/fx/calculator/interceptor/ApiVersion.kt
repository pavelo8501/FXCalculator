package lv.fx.calculator.interceptor

import com.fasterxml.jackson.databind.ObjectMapper
import lv.fx.calculator.model.data.BooleanResponse
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono


@Component
class ApiVersionInterceptor(
   // private val objectMapper: ObjectMapper
) : WebFilter {
    private val objectMapper  = ObjectMapper()
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val apiVersion = exchange.request.headers.getFirst("X-API-VERSION")
        return if (apiVersion == null) {
            exchange.response.statusCode = HttpStatus.BAD_REQUEST
            val json = objectMapper.writeValueAsString(
                BooleanResponse(false).apply {
                    setError(400, "X-API-VERSION header is missing")
                }
            )
            val buffer: DataBuffer = DefaultDataBufferFactory().wrap(json.toByteArray())
            exchange.response.writeWith(Mono.just(buffer))
        } else {
            chain.filter(exchange)
        }
    }
}