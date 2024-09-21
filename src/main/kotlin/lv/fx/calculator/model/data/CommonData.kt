package lv.fx.calculator.model.data

import kotlin.to


interface FxCalculableData {
    val fromCurrency: String
    val toCurrency: String
    val fromCurrencyId: Int
    val toCurrencyId: Int
    val amount: Double
}


//Result class that holds the result and logic of the exchange calculation in order to keep it in one place
class ExchangeTriangulationResult(
   override val amount: Double,
   val fee : Double,
):FxCalculableData{

    val baseCurrency = "EUR"

    override var fromCurrency: String = ""
    override var toCurrency: String = ""
    override var fromCurrencyId: Int = 0
    override var toCurrencyId: Int = 0


    var calculatedFromDefaultFee = true
    var result: Double = 0.0
    var errorMessage: String? = null

    private fun feeCalculation(amount: Double, fee: Double): Double {
        return (amount - amount * fee)

    }

    fun calculate(from : RateModel, to : RateModel): ExchangeTriangulationResult {
        //initializing new values not to keep reference to the source objects
        this.fromCurrency = from.currency
        this.toCurrency = to.currency
        this.fromCurrencyId = from.id
        this.toCurrencyId = to.id

       // since source data have no direct conversion rates, triangulation approach is used
       // 1. convert from {fromCurrency} to EUR
       // 2. convert from EUR to {toCurrency}
       // 3. calculate fee using the formula  (amount - amount * fee) * rate
       // 4. apply fee to the result

        var cumulativeFee = 0.0

        var buyBaseCost = amount
        if(fromCurrency != baseCurrency){
            buyBaseCost = (amount / from.rate)
            cumulativeFee += feeCalculation(amount, from.rate)
        }

        var buyToCost = buyBaseCost
        if(toCurrency != baseCurrency){
            buyToCost = (buyBaseCost * to.rate)
            cumulativeFee += feeCalculation(amount, from.rate)
        }
        result = buyToCost + cumulativeFee

        return this
    }

}