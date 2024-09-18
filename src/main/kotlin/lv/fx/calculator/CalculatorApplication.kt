package lv.fx.calculator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CalculatorApplication


fun run(){
	print("First Run")
}


fun main(args: Array<String>) {
	run()
	runApplication<CalculatorApplication>(*args)
}
