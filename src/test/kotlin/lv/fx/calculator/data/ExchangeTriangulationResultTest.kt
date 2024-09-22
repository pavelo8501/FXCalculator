package lv.fx.calculator.data

import lv.fx.calculator.model.data.ExchangeTriangulationResult
import lv.fx.calculator.model.data.RateModel
import lv.fx.calculator.model.entity.RateEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ExchangeTriangulationResultTest {

    val fee = 0.02
    val fromCurrency = RateModel(RateEntity("GBP", 0.83))
    val toCurrency = RateModel(RateEntity("USD", 1.16))

    val baseCurrency = RateModel(RateEntity("EUR", 1.0))

    @Test
    fun `test result value for GBP  to USD conversion`() {
        /*
            GBP -> EUR =  (amount - amount * fee) * rate = (100 - 100 * 0.02) / 0.83 = 118.07
            EUR -> USD = 118.07 * rate = 118.07 * 1.16 = 136.96
         */
        val calculation = ExchangeTriangulationResult(100.0,fee).calculate(fromCurrency, toCurrency)
        assertEquals(136.96, calculation.amount)
    }

    @Test
    fun `test result value for USD  to GBP conversion`() {
        /*
            USD -> EUR =  (amount - amount * fee) / rate = (100 - 100 * 0.02) / 1.16 = 84.48
            EUR -> GBP = 84.48 * rate = 113.68 * 0.83 = 70.12
         */
        val calculation = ExchangeTriangulationResult(100.0,fee).calculate(toCurrency, fromCurrency)
        assertEquals(70.12, calculation.amount)
    }

    @Test
    fun `test result value for EUR  to GBP conversion`() {
        /*
            EUR -> GBP =  (amount - amount * fee) * rate = (100 - 100 * 0.02) * 0.83 = 81.34
         */
        val calculation = ExchangeTriangulationResult(100.0,fee).calculate(baseCurrency, fromCurrency)
        assertEquals(81.34, calculation.amount)
    }

    @Test
    fun `test result value for GBP  to EUR conversion`() {
        /*
            GBP -> EUR =  (amount - amount * fee) / rate = (100 - 100 * 0.02) / 0.83 = 118.07
         */
        val calculation = ExchangeTriangulationResult(100.0,fee).calculate(fromCurrency, baseCurrency)
        assertEquals(118.07, calculation.amount)
    }

    @Test
    fun `test result value for EUR  to EUR conversion`() {
        /*
            GBP -> EUR =  (amount - amount * fee) / rate = (100 - 100 * 0.02) / 1 = 98
         */
        val calculation = ExchangeTriangulationResult(100.0,fee).calculate(baseCurrency, baseCurrency)
        assertEquals(98.0, calculation.amount)
    }

}