package lv.fx.calculator.model.entity



import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "rates")
data class RateEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int,
    val currency: String,
    var rate: Double){
        @CreationTimestamp var createdAt : LocalDateTime?  = null
        @CreationTimestamp var updatedAt : LocalDateTime?  = null

        constructor() : this(0, "", 0.0)
}