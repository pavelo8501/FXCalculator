package lv.fx.calculator.controller.api.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import lv.fx.calculator.model.data.AuthPostRequest
import lv.fx.calculator.model.data.FeeModel
import lv.fx.calculator.model.data.ListResponse
import lv.fx.calculator.model.data.SingleResponse
import lv.fx.calculator.security.JWT
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/api/authenticate")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JWT,
    private val userDetailsService: UserDetailsService
) {

    @PostMapping
    @Operation(summary = "JWT Authentication", description = "Client authentication endpoint")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(schema = Schema(implementation = SingleResponse::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Internal Error",
                content = [Content(schema = Schema(implementation = SingleResponse::class))]
            )
        ]
    )
    fun createAuthenticationToken(@RequestBody authenticationRequest: AuthPostRequest): ResponseEntity<SingleResponse<String>> {
        return try {
            val authentication: Authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(authenticationRequest.username, authenticationRequest.password)
            )

            val userDetails: UserDetails = userDetailsService.loadUserByUsername(authenticationRequest.username)
            val jwt: String = jwtUtil.generateToken(userDetails.username)
            val value = SingleResponse<String>(jwt)
            return ResponseEntity.ok(value)
        } catch (e: AuthenticationException) {
          return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(SingleResponse<String>().also { it.setError(401, "Unauthorized") })
        }
    }
}