package lv.fx.calculator.model.data

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.round


interface CurrencyDataContext {
    val fromCurrency: String
    val toCurrency: String
    val amount: Double
}

interface FxCalculableData : CurrencyDataContext {
    val fromCurrencyId: Int
    val toCurrencyId: Int
}


//Result class that holds the result and logic of the exchange calculation in order to keep it in one place
class ExchangeTriangulationResult(
   override val amount: Double,
   var fee : Double,
   calculatedFromDefaultFee: Boolean = false
):FxCalculableData{

    val baseCurrency = "EUR"

    override var fromCurrency: String = ""
    override var toCurrency: String = ""
    override var fromCurrencyId: Int = 0
    override var toCurrencyId: Int = 0

    var calculatedFromDefaultFee = true
    var resultAmount: Double = 0.0
    var errorMessage: String? = null


    fun calculate(from : RateModel, to : RateModel): ExchangeTriangulationResult {
        //initializing new values not to keep reference to the source objects
        this.fromCurrency = from.currency
        this.toCurrency = to.currency
        this.fromCurrencyId = from.id
        this.toCurrencyId = to.id


        /*
            since source data have no direct conversion rates, triangulation approach is used
            1. calculate amount subtracting fee amount = (amount - amount * fee)
            2. convert from {fromCurrency} to EUR. EUR = (amount / rate)
            3. convert from EUR to {toCurrency}. {toCurrency} = (EUR * rate)
        */
        var feeDeducted = false
        var adjustedAmount = amount
        if(fromCurrency != baseCurrency){
            adjustedAmount =  (amount  - amount * fee) / from.rate
            //rounding to 2 decimal places to avoid floating point issues
            adjustedAmount = BigDecimal(adjustedAmount).setScale(2, RoundingMode.HALF_UP).toDouble()
            feeDeducted = true
        }

        if(toCurrency != baseCurrency){
            //check if fee was deducted from the amount on first operation
            //to cover cases when changing from base currency to another currency
            if(feeDeducted) {
                adjustedAmount = adjustedAmount * to.rate
            }else{
                adjustedAmount = (adjustedAmount  - adjustedAmount * fee) * to.rate
                feeDeducted = true
            }
        }
        //to cover cases when somebody changes same currency to same currency :)))
        if(!feeDeducted){
            adjustedAmount = adjustedAmount - adjustedAmount * fee
        }
        resultAmount = BigDecimal(adjustedAmount).setScale(2, RoundingMode.HALF_UP).toDouble()
        return this
    }

}