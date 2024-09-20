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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int,

    @OneToOne
    @JoinColumn(name = "from_currency_id") var fromCurrency: RateEntity,

    @OneToOne
    @JoinColumn(name = "to_currency_id") var toCurrency: RateEntity,
    var fee: Double){

    @CreationTimestamp
    var createdAt : LocalDateTime?  = LocalDateTime.now()

    constructor():this(0,RateEntity(),RateEntity(),0.0)

}