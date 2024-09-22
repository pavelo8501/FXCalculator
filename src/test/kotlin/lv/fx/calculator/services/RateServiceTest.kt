package lv.fx.calculator.services

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import lv.fx.calculator.model.data.RateModel
import lv.fx.calculator.model.entity.RateEntity
import lv.fx.calculator.model.warning.ServiceWarning
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import lv.fx.calculator.repository.RateRepository
import lv.fx.calculator.services.db.RateService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.context.annotation.Import
import org.springframework.data.repository.findByIdOrNull


internal class RateServiceTest {

    var rateRepository : RateRepository = mockk()
    val rateService = RateService(rateRepository)

    @BeforeEach
    fun init() {
        every { rateRepository.findAll() } returns listOf(
            RateEntity("USD", 1.12).also { it.id = 1}
        )
        every { rateRepository.findById(1).orElse(null) } returns RateEntity("USD", 1.12).also { it.id = 1}
        every { rateRepository.save(RateEntity("USD", 1.12).also { it.id = 1}) } returns RateEntity("USD", 1.12).also { it.id = 1}
    }

    @Test
    fun `should call its datasource to retreive RateModel` (){
        rateRepository.findAll()
        verify(exactly = 1){rateRepository.findAll()}
    }

    @Test
    fun `test select not null no exception thrown`() {
        assertDoesNotThrow {
            val result = rateService.select()
            assertNotNull(result)
        }
    }

    @Test
    fun `whet pick returns RateModel with correct id` (){
        val result = rateService.pick(1)
        verify(exactly = 1){rateRepository.findById(1)}
        assertEquals(result, RateModel(RateEntity("USD", 1.12)))
        assert(result!!.id == 1)
    }
    @Test
    fun `whet pick wrong id null returned` (){
        every { rateRepository.findByIdOrNull(0) } returns null
        assertNull(rateService.pick(0))
    }

    @Test
    fun `test insert method inserts new rate`() {
        val result = rateService.insert(RateModel(RateEntity("USD", 1.12)))
        assertNotNull(result)
        assertEquals(1, result.id)
    }

    @Test
    fun `test update method updates field`() {

        val initialEntity = RateEntity("USD", 1.12).also { it.id = 1 }
        val updatedEntity = RateEntity("USD", 2.12).also { it.id = 1 }

        every { rateRepository.findById(1).orElse(null) } returns initialEntity
        every { rateRepository.save(any<RateEntity>()) } returns updatedEntity

        val result = rateService.update(RateModel(updatedEntity))

        verify { rateRepository.save(match { it.rate == 2.12 }) }
        assertEquals(2.12, result.rate)
    }

    @Test
    fun `test update method updates rate`() {
        every { rateRepository.findById(0).orElse(null) } returns null
        val exception  = assertThrows(ServiceWarning::class.java, {
            rateService.update(RateModel(RateEntity("USD", 1.12)).apply { id = 0 })
        })
        assertEquals("Rate with id 0 not found", exception.message)
    }
}