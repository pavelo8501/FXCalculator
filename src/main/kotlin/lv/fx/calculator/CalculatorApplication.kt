package lv.fx.calculator

import io.github.cdimascio.dotenv.Dotenv
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import io.swagger.v3.oas.annotations.info.Info

@SpringBootApplication
@OpenAPIDefinition(
	info = Info(
		title = "FX Calculator API",
		version = "v1",
		description = "API for FX Calculator"
	)
)
class CalculatorApplication

fun main(args: Array<String>) {
	val dotenv = Dotenv.configure().load()
	dotenv.entries().forEach { entry ->
		System.setProperty(entry.key, entry.value)
	}
	runApplication<CalculatorApplication>(*args)
}