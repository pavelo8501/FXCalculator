package lv.fx.calculator.security

import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono


//class JwtServerAuthenticationConverter : ServerAuthenticationConverter {
//    override fun convert(exchange: ServerWebExchange?): Mono<Authentication> {
//        return Mono.justOrEmpty(exchange)
//            .flatMap { Mono.justOrEmpty(it.request.cookies["X-Auth"]) }
//            .filter { it.isNotEmpty() }
//            .map { it[0].value }
//            .map { UsernamePasswordAuthenticationToken(it, it) }
//    }
//}