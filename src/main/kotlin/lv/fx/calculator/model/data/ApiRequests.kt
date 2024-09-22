package lv.fx.calculator.model.data


data class FeeRequest(
    val fromCurrencyId: Int,
    val toCurrencyId: Int,
    val fee: Double
)

interface RequestablePost{
    val action: String
    val data: Any
}

class CurrencyDataContextImpl(
    override val fromCurrency: String,
    override val toCurrency: String,
    override val initialAmount: Double
) :CurrencyDataContext{

}

data class CalculatePostRequest(
   override val action: String,
   override val data: CurrencyDataContextImpl
): RequestablePost

data class AuthPostRequest(
    val username: String,
    val password: String
)