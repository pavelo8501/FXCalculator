package lv.fx.calculator

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
class CalculatorApplication

fun main(args: Array<String>) {
	val dotenv = Dotenv.configure().load()
	dotenv.entries().forEach { entry ->
		System.setProperty(entry.key, entry.value)
	}
	runApplication<CalculatorApplication>(*args)
}