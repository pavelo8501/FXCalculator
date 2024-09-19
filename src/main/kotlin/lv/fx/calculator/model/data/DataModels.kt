package lv.fx.calculator.model.data

data class ExRate (
    var id: Int = 0,
    val currency: String,
    val rate : Double,
)


data class ExFee (
    var id: Int = 0,
    val currency: String,
    val fee : Double,
)