package lv.fx.calculator.services.data

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import lv.fx.calculator.model.data.RateModel
import lv.fx.calculator.model.entity.RateEntity
import lv.fx.calculator.model.exceptions.ServiceException
import lv.fx.calculator.model.warning.ServiceWarning
import lv.fx.calculator.services.db.FeeService
import lv.fx.calculator.services.db.RateService
import lv.fx.calculator.services.http.RateParser

//Service that initializes is companion.
class DataService {

    //Manages and supplies data
    //Serves as an in memory cache
    companion object DataServiceManager{

        private var temp = "none"

        private var dbFeeService : FeeService? = null
        private var dbRateService : RateService? = null
        private var httpRateParser : RateParser? = null

        private val rates = mutableListOf<RateModel>()

        private val dateServiceManagerScope: CoroutineScope = CoroutineScope(
            Job() + Dispatchers.Default + CoroutineName("DateServiceManagerScope Coroutine")
        )

        fun provideFeeService(feeService : FeeService){
            this.dbFeeService = feeService
        }

        fun provideRateService(rateService : RateService){
            this.dbRateService = rateService
        }

        fun provideRateParser(rateParser : RateParser){
            this.httpRateParser = rateParser
        }

        private suspend fun compareRateAndUpdate(){

        }

        //Loads rates from the database
        private suspend fun loadRates(){
            val values =   dbRateService!!.select()
            values.forEach {
                rates.add(it)
            }
        }

        //Fetches rates from the WEB and updates the database
        private suspend fun fetchRates(){
            val response =  httpRateParser!!.fetchCurrencySuspended()
            if (response.ok) {
                response.result?.forEach {
                    val foundRate = rates.firstOrNull { entity -> entity.currency == it.currency }
                    if (foundRate != null) {
                        if (foundRate.rate != it.rate) {
                            foundRate.rate = it.rate
                            dbRateService!!.update(foundRate)
                        }
                    } else {
                        val newRate = dbRateService!!.insert(RateModel(RateEntity(it.currency, it.rate)))
                        rates.add(newRate)
                    }
                }
            }else{
                throw ServiceWarning("DateServiceManager failed to fetch rates with message: ${response.description} and error code: ${response.errorCode}")
            }
        }

        //Starting point for the service. Proceeds to all methods that are called once on the start of App
        fun start(){
            if(this.dbRateService == null || this.httpRateParser == null || this.dbFeeService == null){
                throw ServiceException("RateService, RateParser or FeeService are not provided")
                return
            }
            dateServiceManagerScope.launch{
                loadRates()
                fetchRates()
            }
        }

        val rateSubject = MutableSharedFlow<List<RateModel>>()
        suspend fun getRateFlow(): SharedFlow<List<RateModel>>{
            val dataToEmit = rates.toList()
            rateSubject.emit(dataToEmit)
            return rateSubject
        }

        suspend fun getRates(): List<RateModel>{
            val dataToReturn = rates.toList()
            return dataToReturn
        }


        val productSubject = MutableSharedFlow<Any>()
        suspend fun sendProductUpdate(id : Int){

        }

        fun setTempValue(value: String){
            this.temp = value
        }

        fun getTempValue(): String{
            return this.temp
        }
    }

}