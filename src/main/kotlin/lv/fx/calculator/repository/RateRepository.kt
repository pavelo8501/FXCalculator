package lv.fx.calculator.repository

import lv.fx.calculator.model.data.ExRate
import lv.fx.calculator.model.entity.RateEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RateRepository : JpaRepository<RateEntity, Int>
