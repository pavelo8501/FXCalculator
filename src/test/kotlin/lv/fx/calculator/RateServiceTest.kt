package lv.fx.calculator

import io.github.cdimascio.dotenv.Dotenv
import lv.fx.calculator.model.entity.RateEntity
import lv.fx.calculator.repository.RateRepository
import lv.fx.calculator.services.db.RateService
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestContext
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.support.AbstractTestExecutionListener
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import org.mockito.Mockito.*


class DotenvTestExecutionListener : AbstractTestExecutionListener() {
    override fun beforeTestClass(testContext: TestContext) {
        val dotenv = Dotenv.configure().load()
        dotenv.entries().forEach { entry ->
            System.setProperty(entry.key, entry.value)
        }
    }
}

@TestExecutionListeners(
    listeners = [DotenvTestExecutionListener::class, DependencyInjectionTestExecutionListener::class],
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)


@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = [
    "spring.datasource.url=\${DB_URL}",
    "spring.datasource.username=\${DB_USER}",
    "spring.datasource.password=\${DB_PASSWORD}"
])

class RateServiceTest(@Autowired val rateService: RateService) {

    private val rateRepository: RateRepository = mock(RateRepository::class.java)
    private val mockRateService: RateService = RateService(rateRepository)

    @Test
    fun `test select not null no exception thrown`() {
        assertDoesNotThrow {
            val result = rateService.select()
            assertNotNull(result)
        }
    }

    @Test
    fun `test pick method returns correct rate`() {
        val rateEntity = RateEntity(1, "USD", 1.12)
        `when`(rateRepository.findById(1)).thenReturn(java.util.Optional.of(rateEntity))
        val result = rateService.pick(1)
        assertNotNull(result)
        assertEquals("USD", result.currency)
    }

    @Test
    fun `test pick wrong id returns null exception not thrown`() {
        assertDoesNotThrow {
            val result = rateService.pick(0)
            assertNull(result)
        }
    }

    @Test
    fun `test update method updates rate`() {
        val rateEntity = RateEntity(1, "USD", 1.12)
        `when`(rateRepository.save(rateEntity)).thenReturn(rateEntity)
        val result = mockRateService.update(rateEntity)
        assertEquals(1.12, result.rate)
        verify(rateRepository, times(1)).save(rateEntity)
    }

    @Test
    fun `test insert method inserts new rate`() {
        val rateEntity = RateEntity(1, "USD", 1.12)
        `when`(rateRepository.save(rateEntity)).thenReturn(rateEntity)
        val result = mockRateService.insert(rateEntity)
        assertNotNull(result)
        assertEquals("USD", result.currency)
    }

    @Test
    fun `test delete method removes rate by id`() {
        doNothing().`when`(rateRepository).deleteById(1)
        mockRateService.delete(1)
        verify(rateRepository, times(1)).deleteById(1)
    }



}
