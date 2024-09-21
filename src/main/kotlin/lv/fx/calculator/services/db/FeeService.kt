package lv.fx.calculator.services.db

import jakarta.persistence.Id
import lv.fx.calculator.model.data.FeeModel
import lv.fx.calculator.model.entity.FeeEntity
import lv.fx.calculator.model.entity.RateEntity
import lv.fx.calculator.model.warning.ServiceWarning
import lv.fx.calculator.repository.FeeRepository
import lv.fx.calculator.repository.RateRepository
import org.springframework.stereotype.Service

@Service
class FeeService(private val feeRepository: FeeRepository) {

    fun select():List<FeeModel> {
        return feeRepository.findAll().map { FeeModel(it) }
    }

    fun pick(id: Int): FeeModel? = feeRepository.findById(id).orElse(null)?.let{ FeeModel(it) }

    fun update(fee: FeeModel, feeValue : Double): FeeModel?{
        val feeEntity = feeRepository.findById(fee.id).orElse(null)
        if(feeEntity == null){
            throw ServiceWarning("Fee with id ${fee.id} not found")
        }
        feeEntity.fee = feeValue
        return FeeModel(feeRepository.save(feeEntity))
    }

    fun delete(id: Int): Boolean{
        if(feeRepository.existsById(id)){
            feeRepository.deleteById(id)
            return !feeRepository.existsById(id)
        }else{
            throw ServiceWarning("Fee with id $id not found")
        }
    }

    fun insert(fee: FeeModel): FeeModel{
        val existentEntity = feeRepository.findByFromCurrencyIdAndToCurrencyId(fee.fromCurrency.id, fee.toCurrency.id)
        if (existentEntity != null) {
            throw ServiceWarning("Fee for this currency pair already exists")
        }
        return FeeModel(feeRepository.save(fee.getEntity()))
    }
    fun insert(fromCurrencyId: Int, toCurrencyId : Int, feeValue: Double ): FeeModel{
        val existentEntity = feeRepository.findByFromCurrencyIdAndToCurrencyId(fromCurrencyId, toCurrencyId)
        if (existentEntity != null) {
            throw ServiceWarning("Fee for this currency pair already exists")
        }
        return FeeEntity().also {
            it.fromCurrency = RateEntity().apply { id = fromCurrencyId }
            it.toCurrency = RateEntity().apply { id = toCurrencyId }
            it.fee = feeValue
            feeRepository.save(it) }.let { FeeModel(it) }
    }

}