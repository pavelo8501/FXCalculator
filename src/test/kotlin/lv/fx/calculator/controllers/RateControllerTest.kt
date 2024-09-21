package lv.fx.calculator.controllers

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import lv.fx.calculator.controller.api.admin.RateController
import lv.fx.calculator.model.data.RateModel
import lv.fx.calculator.model.entity.RateEntity
import lv.fx.calculator.repository.RateRepository
import lv.fx.calculator.services.db.RateService
import lv.fx.calculator.services.http.RateParser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.mockito.BDDMockito.*


@WebFluxTest(RateController::class)
internal class RateControllerTest{

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockBean
    lateinit var rateRepository: RateRepository

    @MockBean
    lateinit  var rateService : RateService

    @MockBean
    lateinit var rateParser: RateParser


//    @MockBean
//    lateinit var rateController: RateController


    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should return all rates` (){

        val rates = listOf(
            RateEntity("USD", 1.12).also { it.id = 1},
            RateEntity("GBP", 2.12).also { it.id = 2}
        )

        val rateModels = listOf(
            RateModel(RateEntity("USD", 1.12).also { it.id = 1}),
            RateModel(RateEntity("GBP", 2.12).also { it.id = 2})
        )


        given(rateService.select()).willReturn(rateModels)

       // coEvery {rateService.select() } returns rateModels


        webTestClient.get().uri("/admin/api/rates")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.data[0].currency").isEqualTo("USD")
            .jsonPath("$.data[1].currency").isEqualTo("EUR")

    }



}