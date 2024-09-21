package lv.fx.calculator.controller.api.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.models.annotations.OpenAPI31
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import lv.fx.calculator.model.data.BooleanResponse
import lv.fx.calculator.model.data.FeeModel
import lv.fx.calculator.model.data.FeeRequest
import lv.fx.calculator.services.db.RateService
import lv.fx.calculator.model.data.ListResponse
import lv.fx.calculator.model.data.SingleResponse
import lv.fx.calculator.model.entity.FeeEntity
import lv.fx.calculator.model.warning.ServiceWarning
import lv.fx.calculator.services.db.FeeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono


@RestController
@RequestMapping("/admin/api/fees")
@CrossOrigin(origins = ["http://localhost:4200"])
@Tag(description = "Admin API for managing conversion fees and rates", name = "Admin Fees API")
class FeeController(
    private val feeService: FeeService,
    private val rateService: RateService
) {

    private val adminScope: CoroutineScope = CoroutineScope(
        Job() + Dispatchers.IO + CoroutineName("FeeController Coroutine")
    )

    @GetMapping
    @Operation(summary = "Get fees", description = "Retrieves a list of all fees")
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
    suspend fun getFees(): Mono<ResponseEntity<ListResponse<FeeModel>?>?> {
        try {
            val fees = feeService.select()
            val response = ResponseEntity.ok(ListResponse<FeeModel>(fees))
            return Mono.just(response)
        } catch (e: Exception) {
            val response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ListResponse<FeeModel>().also { it.setError(500, e.message ?: "") })
            return Mono.just(response)
        }
    }

    @PostMapping(value = ["/"])
    @Operation(summary = "Save a fee", description = "Saves a new fee")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(schema = Schema(implementation = SingleResponse::class))]
            ),
            ApiResponse(
                responseCode = "420",
                description = "Method Failure",
                content = [Content(schema = Schema(implementation = SingleResponse::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal Error",
                content = [Content(schema = Schema(implementation = SingleResponse::class))]
            )
        ]
    )
    suspend fun saveFee(
        @RequestBody feeRequest: FeeRequest
    ): Mono<ResponseEntity<SingleResponse<FeeModel>>> {
        try {
            val fromCurrency = rateService.pick(feeRequest.fromCurrencyId)
            if(fromCurrency == null){
                ResponseEntity.status(HttpStatus.METHOD_FAILURE)
                    .body(SingleResponse<FeeModel>().also { it.setError(46, "from_currency_id not found") })
            }
            val toCurrency = rateService.pick(feeRequest.toCurrencyId)
            if(toCurrency == null){
                ResponseEntity.status(HttpStatus.METHOD_FAILURE)
                    .body(SingleResponse<FeeModel>().also { it.setError(46, "to_currency_id not found") })
            }

            val createdFee = feeService.insert(fromCurrency!!.id, toCurrency!!.id, feeRequest.fee)
            val response = ResponseEntity.ok(SingleResponse<FeeModel>().also { it.setData(createdFee) })
            return Mono.just(response)
        }catch (w: ServiceWarning) {
            val response = ResponseEntity.status(HttpStatus.METHOD_FAILURE)
                .body(SingleResponse<FeeModel>().also { it.setError(46, w.message ?: "") })
            return Mono.just(response)
        }
        catch (e: Exception){
            val response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(SingleResponse<FeeModel>().also { it.setError(500, e.message ?: "") })
            return Mono.just(response)
        }
    }

    @PatchMapping(value = ["/{id}"])
    @Operation(summary = "Update a fee", description = "Updates the fee for the given ID")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Fee updated successfully",
                content = [Content(schema = Schema(implementation = BooleanResponse::class))]
            ),
            ApiResponse(
                responseCode = "420",
                content = [Content(schema = Schema(implementation = SingleResponse::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal Error",
                content = [Content(schema = Schema(implementation = SingleResponse::class))]
            )
        ]
    )
    suspend fun updateFee(
        @Parameter(description = "ID of the fee to be updated") @PathVariable("id") id: Int,
        @Parameter(description = "New fee value") @RequestBody fee: Double
    ): Mono<ResponseEntity<BooleanResponse>> {

        try {
            val feeEntity = feeService.pick(id)
            if(feeEntity == null){
                val response = ResponseEntity.status(HttpStatus.METHOD_FAILURE)
                    .body(BooleanResponse(false).also { it.setError(46, "Fee with id $id not found") })
                return Mono.just(response)
            }
            feeService.update(feeEntity, fee)
            val response = ResponseEntity.ok(BooleanResponse(true))
            return Mono.just(response)
        }catch (e: Exception){
            val response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BooleanResponse(false).also { it.setError(500, e.message ?: "") })
            return Mono.just(response)
        }
    }

    @DeleteMapping(value = ["/{id}"])
    @Operation(summary = "Delete a fee", description = "Deletes the fee for the given ID")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Fee deleted successfully",
                content = [Content(schema = Schema(implementation = BooleanResponse::class))]
            ),
            ApiResponse(
                responseCode = "420",
                content = [Content(schema = Schema(implementation = BooleanResponse::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal Error",
                content = [Content(schema = Schema(implementation = BooleanResponse::class))]
            )
        ]
    )
    suspend fun deleteFee(
        @Parameter(description = "ID of the fee to be deleted") @PathVariable("id") id: Int
    ): Mono<ResponseEntity<BooleanResponse>> {
        try {
            val response = BooleanResponse(feeService.delete(id))
            if (response.result) {
                val response = ResponseEntity.ok(response)
                return Mono.just(response)
            } else {
                val response = ResponseEntity.status(HttpStatus.METHOD_FAILURE)
                    .body(response.also { it.setError(46, "Fee with id $id not found") })
                return Mono.just(response)
            }
        }catch (e: Exception) {
            val response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BooleanResponse(false).also { it.setError(500, e.message ?: "") })
            return Mono.just(response)
        }
    }
}