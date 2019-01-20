package kg.enesai.toshok.controllers

import kg.enesai.toshok.dtos.CreateAccountDto
import kg.enesai.toshok.services.AccountService
import kg.enesai.toshok.services.RegionService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("account")
class AccountController(
        private val accountService: AccountService,
        private val regionService: RegionService
) {
    @GetMapping("/register")
    fun getRegister(model: Model): String {
        model.addAttribute("account", CreateAccountDto())
        model.addAttribute("regions", regionService.findAll())
        model.addAttribute("accounts", accountService.findAll())
        return "register"
    }

    @PostMapping("/create")
    fun create(createAccountDto: CreateAccountDto, model: Model): String {
        accountService.create(createAccountDto)
        return "welcome"
    }

}