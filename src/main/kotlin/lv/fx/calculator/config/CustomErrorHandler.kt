package lv.fx.calculator.config

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.servlet.NoHandlerFoundException
import java.time.LocalDateTime

@ControllerAdvice
class CustomErrorHandler {
    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNotFound(e: NoHandlerFoundException): ResponseEntity<Map<String, String>> {
        val responseBody = mapOf(
            "timestamp" to LocalDateTime.now().toString(),
            "url" to e.requestURL,
            "error" to "Resource not found"
        )
        return ResponseEntity(responseBody, HttpStatus.NOT_FOUND)
    }
}