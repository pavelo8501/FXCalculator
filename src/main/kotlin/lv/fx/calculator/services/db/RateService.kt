package lv.fx.calculator.services.db

import lv.fx.calculator.model.data.RateModel
import lv.fx.calculator.model.entity.RateEntity
import lv.fx.calculator.model.warning.ServiceWarning
import lv.fx.calculator.repository.RateRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class RateService(private val rateRepository: RateRepository) {

    fun select(): List<RateModel>{
       return rateRepository.findAll().map { toData(it) }
    }

    fun pick(id: Int): RateModel? {
        rateRepository.findById(id).orElse(null)?.let { return toData(it) }
        return null
    }

    fun update(rate: RateModel): RateModel{
       val found = rateRepository.findById(rate.id).orElse(null)
        if (found == null) {
            throw ServiceWarning("Rate with id ${rate.id} not found")
        }
       found.also {
           it.rate = rate.rate
           it.updatedAt = LocalDateTime.now()
       }
       return toData(rateRepository.save(found))
    }

    fun insert(rate: RateModel): RateModel  {
       return toData(rateRepository.save(rate.getEntity()))
    }

    fun toData(source: RateEntity): RateModel{
        return RateModel(source)
    }

}