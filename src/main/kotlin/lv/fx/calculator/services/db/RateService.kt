package lv.fx.calculator.services.db

import lv.fx.calculator.model.data.ExRate
import lv.fx.calculator.model.entity.RateEntity
import lv.fx.calculator.repository.RateRepository
import org.springframework.stereotype.Service


@Service
class RateService(private val rateRepository: RateRepository) {

    fun select(): List<RateEntity> = rateRepository.findAll()

    fun pick(id: Int): RateEntity? = rateRepository.findById(id).orElse(null)

    fun update(rate: RateEntity): RateEntity = rateRepository.save(rate)

    fun delete(id: Int) = rateRepository.deleteById(id)

    fun insert(rate: RateEntity): RateEntity = rateRepository.save(rate)

    fun toData(source: RateEntity): ExRate{
        return ExRate(
            source.id,
            source.currency,
            source.rate,
        )
    }

    fun toEntity(source: ExRate): RateEntity{
        return RateEntity(
            source.id,
            source.currency,
            source.rate,
        )
    }
}