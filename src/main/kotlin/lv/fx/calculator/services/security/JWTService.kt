package lv.fx.calculator.services.security


import lv.fx.calculator.model.security.User
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit



//
//class TokenService(
//    private val userService: UserService,
//) {
//    fun createToken(user: User): String {
//        val jwsHeader = JwsHeader.with { "HS256" }.build()
//        val claims = JwtClaimsSet.builder()
//            .issuedAt(Instant.now())
//            .expiresAt(Instant.now().plus(30L, ChronoUnit.DAYS))
//            .subject(user.email)
//            .claim("userId", user.id)
//            .build()
//        return ""
//    }
//
//    fun parseToken(token: String): User? {
//        return try {
//            //val jwt = jwtDecoder.decode(token)
//            //val userId = jwt.claims["userId"] as Int
//            userService.findById(1)
//        } catch (e: Exception) {
//            null
//        }
//    }
//}