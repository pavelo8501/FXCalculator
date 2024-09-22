package lv.fx.calculator.controller.api.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import lv.fx.calculator.model.data.AuthPostRequest
import lv.fx.calculator.model.data.SingleResponse
import lv.fx.calculator.services.security.HashService
import lv.fx.calculator.services.security.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/api/authenticate")
class AuthController(
    private val hashService: HashService,
    private val userService: UserService,
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

        val user = userService.findByEmail(authenticationRequest.username)

//        if (!hashService.checkBcrypt(authenticationRequest.password, user.password)) {
//
//        }
        //val token = tokenService.createToken(user)
        val token = "some token"

       return ResponseEntity.ok(SingleResponse<String>(token))

    }
}