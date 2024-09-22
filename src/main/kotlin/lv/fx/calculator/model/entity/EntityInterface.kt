package lv.fx.calculator.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

interface IdEntity{
    val id: Int
}

@MappedSuperclass
abstract class GeneratedIdEntity() : IdEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Int =0

    @CreationTimestamp
    var createdAt : LocalDateTime?  = LocalDateTime.now()

    @CreationTimestamp
    var updatedAt : LocalDateTime?  = LocalDateTime.now()
}

