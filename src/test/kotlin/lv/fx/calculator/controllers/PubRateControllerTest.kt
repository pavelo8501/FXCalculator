package lv.fx.calculator.controllers

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkObject
import lv.fx.calculator.controller.api.admin.RateController
import lv.fx.calculator.controller.api.pub.PubRateController
import lv.fx.calculator.model.data.CalculatePostRequest
import lv.fx.calculator.model.data.CurrencyDataContextImpl
import lv.fx.calculator.model.data.ExchangeTriangulationResult
import lv.fx.calculator.model.data.RateModel
import lv.fx.calculator.model.entity.RateEntity
import lv.fx.calculator.repository.RateRepository
import lv.fx.calculator.services.data.DataService
import lv.fx.calculator.services.db.RateService
import lv.fx.calculator.services.http.RateParser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(PubRateController::class)
internal class PubRateControllerTest{

    @Autowired
    lateinit var webTestClient: WebTestClient

//    @MockBean
//    lateinit var rateRepository: RateRepository
//
//    @MockBean
//    lateinit var feeRepository: RateRepository
//
//    @MockBean
//    lateinit var feeRepository: RateRepository

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(DataService.DataServiceManager)
    }

    @Test
    fun `should return all rates` () {

        val rateModels = listOf(
            RateModel(RateEntity("USD", 1.12).also { it.id = 1 }),
            RateModel(RateEntity("GBP", 2.12).also { it.id = 2 })
        )

        coEvery { DataService.DataServiceManager.getRates() } returns rateModels

        webTestClient.get().uri("/api/rates")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
    }

    @Test
    fun `should handle exception in getRates`() {
        coEvery { DataService.DataServiceManager.getRates() } throws RuntimeException("Internal Error")

         webTestClient.get().uri("/api/rates")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody()
            .jsonPath("$.errorCode").isEqualTo(500)
            .jsonPath("$.error").isEqualTo("Internal Error")

       // println(responseBody?.toString(Charsets.UTF_8))
    }

//    @Test
//    fun `should execute post request`() {
//        val request = CalculatePostRequest("calculate", CurrencyDataContextImpl("USD", "EUR", 100.0))
//        val result = ExchangeTriangulationResult()
//
//        coEvery { DataService.DataServiceManager.calculateExchange(any(), any(), any()) } returns result
//
//        webTestClient.post().uri("/api/rates/")
//            .contentType(MediaType.APPLICATION_JSON)
//            .bodyValue(request)
//            .exchange()
//            .expectStatus().isOk
//            .expectHeader().contentType(MediaType.APPLICATION_JSON)
//            .expectBody()
//            .jsonPath("$.data").isNotEmpty
//    }

    @Test
    fun `should handle exception in executePostRequest`() {

        coEvery { DataService.DataServiceManager.calculateExchange(any(), any(), any()) } throws RuntimeException("Internal Error")

        webTestClient.post().uri("/api/rates/")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(CalculatePostRequest("calculate", CurrencyDataContextImpl("USD", "EUR", 100.0)))
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody()
            .jsonPath("$.errorCode").isEqualTo(500)
            .jsonPath("$.error").isEqualTo("Internal Error")
    }

}