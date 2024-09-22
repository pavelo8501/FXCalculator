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
class FeeService(private val feeRepository: FeeRepository, private val rateRepository: RateRepository) {

    var onNewFee: ((FeeModel) -> Unit)? = null
    var onFeeChange: ((FeeModel) -> Unit)? = null
    var onFeeDelete: ((Int) -> Unit)? = null

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
        val savedFee = FeeModel(feeRepository.save(feeEntity))
        onFeeChange?.invoke(savedFee)
        return savedFee
    }

    fun delete(id: Int): Boolean{
        if(feeRepository.existsById(id)){
            feeRepository.deleteById(id)
            //Double check if the fee was deleted
            if(feeRepository.existsById(id)){
                return false
            }else{
                onFeeDelete?.invoke(id)
                return true
            }
        }else{
            throw ServiceWarning("Fee with id $id not found")
        }
    }

    fun insert(fromCurrencyId: Int, toCurrencyId : Int, feeValue: Double ): FeeModel{
        val existentEntity = feeRepository.findByFromCurrencyIdAndToCurrencyId(fromCurrencyId, toCurrencyId)
        if (existentEntity != null) {
            throw ServiceWarning("Fee for this currency pair already exists")
        }
        val newFeeModel = FeeEntity().also {
            val fromRate = rateRepository.findById(fromCurrencyId).orElse(null)
            val toRate = rateRepository.findById(toCurrencyId).orElse(null)
            if(fromRate == null || toRate == null){
                throw ServiceWarning("Currency not found")
            }
            it.fromCurrency = fromRate
            it.toCurrency =  toRate
            it.fee = feeValue
             feeRepository.save(it) }.let {
                FeeModel(it)
            }
        onNewFee?.invoke(newFeeModel)
        return newFeeModel
    }

    fun insert(fee: FeeModel): FeeModel{
        return insert(fee.fromCurrency.id, fee.toCurrency.id, fee.fee)
    }

}