package lv.fx.calculator.config

import lv.fx.calculator.services.data.DataService
import lv.fx.calculator.services.db.FeeService
import lv.fx.calculator.services.db.RateService
import lv.fx.calculator.services.http.RateParser
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DataServiceConfig(
    private val feeService: FeeService,
    private val rateService: RateService,
    private val rateParser: RateParser

) {

    val dataService = DataService()

    init {
        config()
    }


    fun config(){
        DataService.DataServiceManager.setTempValue("Some value")

        //Make soft dependency injections
        DataService.DataServiceManager.provideFeeService(feeService)
        DataService.DataServiceManager.provideRateService(rateService)
        DataService.DataServiceManager.provideRateParser(rateParser)

        //Start the service
        DataService.DataServiceManager.start()
    }

}