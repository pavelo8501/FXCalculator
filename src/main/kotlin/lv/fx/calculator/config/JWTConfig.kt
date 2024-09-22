package lv.fx.calculator.config


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order


open class MultiSecurityHttpConfig {
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    @Bean
//    open fun apiHttpSecurity(http: ServerHttpSecurity): SecurityWebFilterChain {
//        return http {
//            securityMatcher(PathPatternParserServerWebExchangeMatcher("/api/**"))
//            authorizeExchange {
//                authorize(anyExchange, authenticated)
//            }
//            oauth2ResourceServer {
//                jwt { }
//            }
//        }
//    }
//
//    @Bean
//    open fun webHttpSecurity(http: ServerHttpSecurity): SecurityWebFilterChain {
//        return http {
//            authorizeExchange {
//                authorize(anyExchange, authenticated)
//            }
//            httpBasic { }
//        }
//    }

}