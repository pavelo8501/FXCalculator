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
import lv.fx.calculator.model.data.ExFee
import lv.fx.calculator.services.db.RateService
import lv.fx.calculator.model.data.ListResponse
import lv.fx.calculator.model.data.SingleResponse
import lv.fx.calculator.model.entity.FeeEntity
import lv.fx.calculator.services.db.FeeService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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
                description = "List of fees retrieved successfully",
                content = [Content(schema = Schema(implementation = ListResponse::class))]
            )
        ]
    )
    suspend fun getFees(): Mono<ListResponse<ExFee>> {
        val result = feeService.select()
            .map { ExFee(it.id, rateService.toData(it.fromCurrency), rateService.toData(it.toCurrency), it.fee) }
        return Mono.just(ListResponse<ExFee>(true).also { it.setData(result) })
    }

    @PutMapping(value = ["/"])
    @Operation(summary = "Save a fee", description = "Saves a new fee")
    suspend fun saveFee(
        @RequestParam("fromCurrencyId") fromCurrencyId: Int,
        @RequestParam("toCurrencyId") toCurrencyId: Int,
        @RequestParam("fee") fee: Double
    ): Mono<SingleResponse<ExFee>> {
        val fromCurrency = rateService.pick(fromCurrencyId)
        val toCurrency = rateService.pick(toCurrencyId)
        if (fromCurrency != null && toCurrency != null) {
            val result = feeService.update(FeeEntity(0, fromCurrency, toCurrency, fee)).let {
                ExFee(it.id, rateService.toData(it.fromCurrency), rateService.toData(it.toCurrency), it.fee)
            }
            return Mono.just(SingleResponse<ExFee>(true).also { it.setData(result) })
        } else {
            throw IllegalArgumentException("Currency pair not found")
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
                responseCode = "404",
                description = "Fee not found",
                content = [Content(schema = Schema(implementation = BooleanResponse::class))]
            )
        ]
    )
    suspend fun updateFee(
        @Parameter(description = "ID of the fee to be updated") @PathVariable("id") id: Int,
        @Parameter(description = "New fee value") @RequestBody fee: Double
    ): Mono<BooleanResponse> {
        val feeEntity = feeService.pick(id)
        if (feeEntity != null) {
            feeEntity.fee = fee
            try {
                val result = feeService.update(feeEntity)
                return Mono.just(BooleanResponse(true))
            } catch (e: Exception) {
                return Mono.just(BooleanResponse(false))
            }
        } else {
            return Mono.just(BooleanResponse(false).also { it.setError(104, "Fee not found") })
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
                responseCode = "404",
                description = "Fee not found",
                content = [Content(schema = Schema(implementation = BooleanResponse::class))]
            )
        ]
    )
    suspend fun deleteFee(
        @Parameter(description = "ID of the fee to be deleted") @PathVariable("id") id: Int
    ): Mono<BooleanResponse> {
        val response = BooleanResponse(feeService.delete(id))
        if (!response.result) {
            response.setError(704, "Fee not found")
        }
        return Mono.just(response)
    }

}