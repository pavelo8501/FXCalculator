package lv.fx.calculator.services

import io.mockk.MockKAdditionalAnswerScope
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import lv.fx.calculator.model.data.FeeModel
import lv.fx.calculator.model.entity.FeeEntity
import lv.fx.calculator.model.entity.RateEntity
import lv.fx.calculator.model.warning.ServiceWarning
import lv.fx.calculator.repository.FeeRepository
import lv.fx.calculator.repository.RateRepository
import lv.fx.calculator.services.db.FeeService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.data.repository.findByIdOrNull


internal class FeeServiceTest {

   // var rateRepository : RateRepository = mockk()
    var feeRepository : FeeRepository = mockk()
    var rateRepository : RateRepository = mockk()
    val feeService = FeeService(feeRepository, rateRepository)

    var mockedEntity : MockKAdditionalAnswerScope<FeeEntity, FeeEntity> = mockk()

    val fromEntity = RateEntity("USD", 1.12).also { it.id = 1 }
    val toEntity = RateEntity("GBP", 2.12).also { it.id = 2 }

    var feeEntity = FeeEntity().also {
        it.id = 1
        it.fromCurrency = fromEntity
        it.toCurrency = toEntity
        it.fee = 0.12
    }

    @BeforeEach
    fun init() {
        mockedEntity = every {feeRepository.findById(1).orElse(null)} returns FeeEntity().also {
            it.id = 1
            it.fromCurrency = fromEntity
            it.toCurrency = toEntity
            it.fee = 0.12 }


        every { feeRepository.findAll() } returns listOf(
            feeEntity
        )
        every { feeRepository.findById(1).orElse(null) } returns feeEntity
        every { feeRepository.save(FeeEntity()) } returns feeEntity

        every { feeRepository.findByFromCurrencyIdAndToCurrencyId(1,2) } returns feeEntity

        every {feeRepository.save(feeEntity)} returns feeEntity
    }

    @Test
    fun `should call its datasource to retrieve FeeModel` (){

        feeService.select()
        verify(exactly = 1){feeRepository.findAll()}
    }

    @Test
    fun `test select not null no exception thrown`() {
        assertDoesNotThrow {
            val result = feeService.select()
            assertNotNull(result)
        }
    }

    @Test
    fun `whet pick returns FeeModel with correct id` (){
        val result = feeService.pick(1)
        assertEquals(result, FeeModel(feeEntity))
        assert(result!!.id == 1)
    }
    @Test
    fun `whet pick wrong id null returned` (){
        every { feeRepository.findByIdOrNull(0) } returns null
        assertNull(feeService.pick(0))
    }

    @Test
    fun `test insert method inserts new rate`() {
        every { feeRepository.findByFromCurrencyIdAndToCurrencyId(1, 2) } returns null
        every { feeRepository.save(any<FeeEntity>()) } answers { firstArg<FeeEntity>().apply { id = 1 } }
        val result = feeService.insert(FeeModel(feeEntity))
        assertNotNull(result)
        assertEquals(1, result.id)
    }

    @Test
    fun `test update method updates field`() {
        val result = feeService.update(FeeModel(feeEntity), 0.12)
        verify { feeRepository.save(match { it.fee == 0.12 }) }
        assertEquals(0.12, result!!.fee)
    }

    @Test
    fun `test update method with wrong id throws ServiceWarning`() {
        every { feeRepository.findById(0).orElse(null) } returns null
        val exception = assertThrows(ServiceWarning::class.java) {
            feeService.update(FeeModel(feeEntity).also { it.id = 0 }, 0.12)
        }
        assertEquals("Fee with id 0 not found", exception.message)
    }
}