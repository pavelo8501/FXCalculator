package lv.fx.calculator

import kotlinx.coroutines.runBlocking
import lv.fx.calculator.services.MessageResponse
import lv.fx.calculator.services.http.RateParser
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.mockito.Mockito.`when`
import org.springframework.test.web.servlet.get


@SpringBootTest
@AutoConfigureMockMvc
class RateControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var rateParser: RateParser

    @Test
    fun shouldReturnRates() : Unit = runBlocking{
        val sampleResponse = MessageResponse(true)
        `when`(rateParser.fetchRates()).thenReturn(sampleResponse)
        mockMvc.get("/api/rates")
            .andExpect {
                status { isOk() }
                content { string("Sample Rates") }
            }

    }

}