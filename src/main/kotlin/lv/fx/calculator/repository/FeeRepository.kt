package lv.fx.calculator.repository

import lv.fx.calculator.model.entity.FeeEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FeeRepository : JpaRepository<FeeEntity, Int>{
    fun findByFromCurrencyIdAndToCurrencyId(fromCurrencyId: Int, toCurrencyId: Int): FeeEntity?
}