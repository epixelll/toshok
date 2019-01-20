package kg.enesai.toshok.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class CommonController {

    @GetMapping("/login")
    fun login(): String {
        return "login"
    }

    @GetMapping("/welcome")
    fun welcome(model: MutableMap<String, Any>): String {
        model["message"] = "this message"
        return "welcome"
    }
}