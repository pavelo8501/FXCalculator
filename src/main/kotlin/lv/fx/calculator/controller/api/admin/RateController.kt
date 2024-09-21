package lv.fx.calculator.controller.api.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import lv.fx.calculator.services.db.RateService
import lv.fx.calculator.model.data.ListResponse
import lv.fx.calculator.model.data.RateModel
import lv.fx.calculator.model.entity.RateEntity
import lv.fx.calculator.services.data.DataService
import lv.fx.calculator.services.http.RateParser
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/api/rates")
@Tag(description = "Admin API for managing conversion fees and rates", name = "Admin Fees API")
class RateController(
    private val rateService: RateService,
    private val rateParser: RateParser
) {

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
    @Operation(summary = "Update rates", description = "Update rates from the source")
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
    suspend fun updateRates(): ResponseEntity<ListResponse<RateModel>> {
       return try {
            val rates =  DataService.DataServiceManager.updateRates().firstOrNull() ?: emptyList()
            ResponseEntity.ok(ListResponse(rates))
        }catch (e: Exception){
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ListResponse<RateModel>().also { it.setError(500, e.message ?: "") })
        }
    }
}