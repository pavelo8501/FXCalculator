package lv.fx.calculator.controller.api.pub

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import kotlinx.coroutines.flow.firstOrNull
import lv.fx.calculator.model.data.CalculatePostRequest
import lv.fx.calculator.model.data.ExchangeTriangulationResult
import lv.fx.calculator.model.data.FeeRequest
import lv.fx.calculator.model.data.ListResponse
import lv.fx.calculator.model.data.RateModel
import lv.fx.calculator.model.data.SingleResponse
import lv.fx.calculator.services.data.DataService
import lv.fx.calculator.services.http.RateParser
import lv.fx.calculator.services.http.ServiceResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono


@RestController
@RequestMapping("/api/rates")
class PubRateController() {

    @GetMapping
    @Operation(summary = "Get fees", description = "Retrieves a list of rates")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(schema = Schema(implementation = ListResponse::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal Error",
                content = [Content(schema = Schema(implementation = ListResponse::class))]
            )
        ]
    )
    suspend fun getRates(): ResponseEntity<ListResponse<RateModel>>{
        try {
            val rates = DataService.DataServiceManager.getRates()
            val response = ResponseEntity.ok(ListResponse<RateModel>(rates))
            return response
        }catch (e: Exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ListResponse<RateModel>().also { it.setError(500, e.message ?: "") })
        }
    }

    @PostMapping
    @Operation(summary = "Post Request", description = "Performs actions on rates collection")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(schema = Schema(implementation = ListResponse::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal Error",
                content = [Content(schema = Schema(implementation = ListResponse::class))]
            )
        ]
    )
    suspend fun executePostRequest(@RequestBody body: CalculatePostRequest): ResponseEntity<SingleResponse<ExchangeTriangulationResult>> {
        try {
            val calculation =  DataService.DataServiceManager.calculateExchange(body.data.fromCurrency, body.data.toCurrency,body.data.amount)
            return ResponseEntity.ok(SingleResponse<ExchangeTriangulationResult>(calculation))
        }catch (e: Exception){
           val response = SingleResponse<ExchangeTriangulationResult>()
           response.setError(500, e.message ?: "")
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body<SingleResponse<ExchangeTriangulationResult>>(response)
        }
    }

}