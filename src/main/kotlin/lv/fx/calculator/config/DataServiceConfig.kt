package lv.fx.calculator.config

import io.github.cdimascio.dotenv.Dotenv
import lv.fx.calculator.services.data.DataService
import lv.fx.calculator.services.db.FeeService
import lv.fx.calculator.services.db.RateService
import lv.fx.calculator.services.http.RateParser
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DataServiceConfig(
    private val feeService: FeeService,
    private val rateService: RateService,
    private val rateParser: RateParser

) {

    init {
        config()
    }



    fun config(){
        //Make soft dependency injections
        DataService.DataServiceManager.provideFeeService(feeService)
        DataService.DataServiceManager.provideRateService(rateService)
        DataService.DataServiceManager.provideRateParser(rateParser)

        //Start the service
        DataService.DataServiceManager.start()
    }

}