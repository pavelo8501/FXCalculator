package lv.fx.calculator.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class JwtFilter(
    private val jwtUtil: JWT,
    private val userDetailsService: ReactiveUserDetailsService
) : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val authorizationHeader = exchange.request.headers.getFirst("Authorization")

        return if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            val jwt = authorizationHeader.substring(7)
            val username = jwtUtil.extractAllClaims(jwt).subject

            userDetailsService.findByUsername(username).flatMap { userDetails ->
                if (jwtUtil.validateToken(jwt, userDetails.username)) {
                    val authenticationToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    Mono.just(authenticationToken)
                } else {
                    Mono.empty<UsernamePasswordAuthenticationToken>()
                }
            }.flatMap { authenticationToken ->
                chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authenticationToken))
            }
        } else {
            chain.filter(exchange)
        }
    }
}