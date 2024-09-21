package lv.fx.calculator.model.data

import lv.fx.calculator.model.entity.FeeEntity
import lv.fx.calculator.model.entity.IdEntity
import lv.fx.calculator.model.entity.RateEntity

abstract class DataModel<T: IdEntity>(entity: T) {
    var id: Int = 0

    private val entity: T

    init {
        this.entity = entity
    }

    fun getEntity(): T {
        return this.entity
    }
}

data class RateModel (
     val entity: RateEntity
):DataModel<RateEntity>(entity){

    var currency : String = ""
        get() = entity.currency

    var rate : Double = 0.0
        get() = entity.rate

    init {
        this.id = entity.id
        this.currency = entity.currency
        this.rate = entity.rate
    }
}


data class FeeModel (
    val entity: FeeEntity
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