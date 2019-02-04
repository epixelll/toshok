package kg.enesai.toshok.controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class CommonController {

    @GetMapping("/login")
    fun login(): String {
        return "login"
    }

    @GetMapping("/welcome")
    fun welcome(model: Model): String {
        return "welcome"
    }

    @GetMapping("/dashboard")
    fun dashboard(model: Model): String {
        return "dashboard"
    }
}