package lv.fx.calculator.model.data

import com.fasterxml.jackson.annotation.JsonIgnore
import lv.fx.calculator.model.entity.FeeEntity
import lv.fx.calculator.model.entity.IdEntity
import lv.fx.calculator.model.entity.RateEntity

interface RateData{
    val currency: String
    val rate: Double
}

abstract class DataModel<T: IdEntity>(entity: T) {
    private val entity: T
    var id: Int = 0

    init {
        this.entity = entity
    }

    fun getEntity(): T {
        return this.entity
    }
}

data class RateModel (
    @JsonIgnore
    private val entity: RateEntity
):DataModel<RateEntity>(entity), RateData{

    override var currency : String = ""
        get() = entity.currency

    override var rate : Double = 0.0
        get() = entity.rate

    init {
        this.id = entity.id
        this.currency = entity.currency
        this.rate = entity.rate
    }
}


data class FeeModel (
    @JsonIgnore
    private val entity: FeeEntity
):DataModel<FeeEntity>(entity) {

    var fromCurrency : RateEntity
            get() = entity.fromCurrency
    var toCurrency: RateEntity
        get() = entity.toCurrency

    var fee: Double
        get() = entity.fee

    init {
        this.id = entity.id
        this.fromCurrency = entity.fromCurrency
        this.toCurrency = entity.toCurrency
        this.fee = entity.fee
    }
}

data class RateRecord  (
    override val currency: String,
    override val rate: Double
) : RateData