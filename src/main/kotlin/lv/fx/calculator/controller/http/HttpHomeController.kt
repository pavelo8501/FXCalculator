package lv.fx.calculator.controller.http

import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.stereotype.Controller
import org.springframework.ui.set

@Controller
class HttpHomeController() {

    @GetMapping("/")
    fun main(homeModel: Model): String {
        homeModel["title"] = "Some title"
        homeModel["admin_url"] = "/admin/index.html"
        return "home"
    }

}