package lv.fx.calculator.services

import io.github.cdimascio.dotenv.Dotenv
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import lv.fx.calculator.model.data.RateModel
import lv.fx.calculator.model.data.RateRecord
import lv.fx.calculator.model.entity.FeeEntity
import lv.fx.calculator.model.entity.RateEntity
import lv.fx.calculator.repository.FeeRepository
import lv.fx.calculator.repository.RateRepository
import lv.fx.calculator.services.data.DataService
import lv.fx.calculator.services.db.FeeService
import lv.fx.calculator.services.db.RateService
import lv.fx.calculator.services.http.RateParser
import lv.fx.calculator.services.http.ServiceResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll


class DataServiceManagerTest {


    companion object {
        lateinit var feeRepository: FeeRepository
        lateinit var rateRepository: RateRepository
        lateinit var rateParser: RateParser
        lateinit var mockedRateEntities: List<RateEntity>
        lateinit var mockedRateRecords : List<RateRecord>
        lateinit var mockedRateParserResponse : ServiceResponse

        @BeforeAll
        @JvmStatic
        fun init() {
            clearAllMocks()

            feeRepository = mockk()
            rateRepository = mockk()
            rateParser = mockk()

            val feeService = FeeService(feeRepository, rateRepository)
            val rateService = RateService(rateRepository)

            mockedRateEntities = listOf(
                RateEntity("USD", 1.16).also { it.id = 1 },
                RateEntity("GBP", 0.83).also { it.id = 2 },
                RateEntity("JPY", 161.08).also { it.id = 2 }
            )

            mockedRateParserResponse = ServiceResponse(
                true,
                listOf(
                    RateRecord("USD", 1.16),
                    RateRecord("GBP", 0.83),
                    RateRecord("JPY", 161.08)
                )
            )

            every { rateRepository.findAll() } returns mockedRateEntities
            every {feeRepository.findAll() } returns listOf(FeeEntity(mockedRateEntities[0], mockedRateEntities[1], 0.1))
            coEvery { rateParser.fetchCurrencySuspended() } returns mockedRateParserResponse

            DataService.DataServiceManager.provideRateParser(rateParser)
            DataService.DataServiceManager.provideFeeService(feeService)
            DataService.DataServiceManager.provideRateService(rateService)
            DataService.DataServiceManager.start()
        }
    }

    @Test
    fun `getRates() method test` (){
        val result = DataService.DataServiceManager.getRates()
        assertNotNull(result)
        assertEquals(3, result.size)
        assertEquals("USD", result[0].currency)
        assertEquals("GBP", result[1].currency)
        assertEquals("JPY", result[2].currency)
    }

    @Test
    fun `test method calculateExchange() consumes fee from env file` (){
        val dotenv = Dotenv.load()
        val calculation = DataService.DataServiceManager.calculateExchange("USD", "JPY", 100.0)
        assertEquals(dotenv["DEFAULT_FEE"].toDouble(), calculation.fee)
    }

    @Test
    fun `test method calculateExchange() consumes custom Fee 10 percent` () {

        val calculation = DataService.DataServiceManager.calculateExchange("USD", "GBP", 100.0)
        assertEquals(0.1, calculation.fee)
    }

}