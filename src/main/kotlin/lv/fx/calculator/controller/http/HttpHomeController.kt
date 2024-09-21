package lv.fx.calculator.controller.http

import lv.fx.calculator.services.db.RateService
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.stereotype.Controller
import org.springframework.ui.set
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class HttpHomeController(private val rateService: RateService) {

    @GetMapping("/")
    fun main(homeModel: Model): String {
        homeModel["title"] = "FXCalculator - Currency Conversions"
        homeModel["admin_url"] = "/admin/index.html"
        homeModel["rates"] = rateService.select()
        return "home"
    }

    @PostMapping("/submit")
    fun submitForm(
        @RequestParam amount: String,
        @RequestParam fromCurrencyId: Int,
        @RequestParam toCurrencyId: Int,
        model: Model): String {

        model["title"] = "FXCalculator - Currency Conversions"
        model["admin_url"] = "/admin/index.html"
        model["rates"] = rateService.select()
        model["price"] = 100
        return "home"
    }

}