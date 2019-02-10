package kg.enesai.toshok.controllers

import kg.enesai.toshok.enums.Permission
import kg.enesai.toshok.services.UserService
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class CommonController(
        private val userService: UserService
) {

    @GetMapping("/login")
    fun login(): String {
        return "login"
    }

    @GetMapping("/welcome")
    fun welcome(model: Model): String {
        return "welcome"
    }

    @GetMapping("/dashboard")
    fun dashboard(authentication: Authentication, model: Model): String {
        if (authentication.authorities.map { it.authority }.contains(Permission.ACCOUNT_APPROVE.toString())) return "redirect:/account/approve-list"
        if (authentication.authorities.map { it.authority }.contains(Permission.ACCOUNT_VIEW.toString())) return "redirect:/account/list"

        val principal = authentication.principal as User
        val user = userService.findByUsername(principal.username)!!

        if(user.account == null) return "redirect:/user/profile"
        return """redirect:/account/info/${user.account?.id}"""
    }
}