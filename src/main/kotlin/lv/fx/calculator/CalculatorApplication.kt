package lv.fx.calculator

import lv.fx.calculator.controller.RateController
import lv.fx.calculator.services.RateParser
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CalculatorApplication


fun testController(){
	//RateController()
}


fun main(args: Array<String>) {
	//testController()
	runApplication<CalculatorApplication>(*args)
}
