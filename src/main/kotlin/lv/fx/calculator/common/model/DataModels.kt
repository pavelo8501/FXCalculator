package lv.fx.calculator.common.model

data class ExRate (
    val buy: String,
    val rate : Double,
    val sell: String = "EUR",
)