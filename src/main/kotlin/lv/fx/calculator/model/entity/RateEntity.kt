package lv.fx.calculator.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Table(name = "rates")
@Entity
data class RateEntity(
    val currency: String,
    var rate: Double) : GeneratedIdEntity() {
        @CreationTimestamp var createdAt : LocalDateTime?  = null
        @CreationTimestamp var updatedAt : LocalDateTime?  = null
        constructor() : this("", 0.0)
}