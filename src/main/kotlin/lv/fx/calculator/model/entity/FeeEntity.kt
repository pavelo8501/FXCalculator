package lv.fx.calculator.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "fees")
data class FeeEntity(

    @OneToOne
    @JoinColumn(name = "from_currency_id") var fromCurrency: RateEntity,

    @OneToOne
    @JoinColumn(name = "to_currency_id") var toCurrency: RateEntity,
    var fee: Double): GeneratedIdEntity(){

    @CreationTimestamp var createdAt : LocalDateTime?  = LocalDateTime.now()
    @CreationTimestamp var updatedAt : LocalDateTime?  = LocalDateTime.now()
    constructor():this(RateEntity(),RateEntity(),0.0)

}