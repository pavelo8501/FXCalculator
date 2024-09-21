package lv.fx.calculator.model.data

data class FeeRequest(
    val fromCurrencyId: Int,
    val toCurrencyId: Int,
    val fee: Double
)