package lv.fx.calculator.services.db

import lv.fx.calculator.model.entity.FeeEntity
import lv.fx.calculator.repository.FeeRepository
import org.springframework.stereotype.Service

@Service
class FeeService(private val feeRepository: FeeRepository) {

    fun select(): List<FeeEntity> = feeRepository.findAll()

    fun pick(id: Int): FeeEntity? = feeRepository.findById(id).orElse(null)

    fun update(fee: FeeEntity): FeeEntity = feeRepository.save(fee)

    fun delete(id: Int) = feeRepository.deleteById(id)

    fun insert(fee: FeeEntity): FeeEntity = feeRepository.save(fee)

}