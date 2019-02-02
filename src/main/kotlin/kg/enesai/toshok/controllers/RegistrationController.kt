package kg.enesai.toshok.controllers

import kg.enesai.toshok.dtos.RegisterForm
import kg.enesai.toshok.services.AccountService
import kg.enesai.toshok.services.RegionService
import kg.enesai.toshok.services.endpoint.RegistrationEndpointService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid

@Controller
@RequestMapping("registration")
class RegistrationController(
        private val registrationEndpoint: RegistrationEndpointService,
        private val accountService: AccountService,
        private val regionService: RegionService
) {
    @GetMapping()
    fun getRegister(@ModelAttribute("registerForm") registerForm: RegisterForm, model: Model): String {
        model.addAttribute("regions", regionService.findAll())
        model.addAttribute("accounts", accountService.findAll())
        return "register"
    }

    @PostMapping()
    fun create(@Valid @ModelAttribute("registerForm") registerForm: RegisterForm, bindingResult: BindingResult, model: Model): String {
        if(bindingResult.hasErrors()){
            model.addAttribute("regions", regionService.findAll())
            model.addAttribute("accounts", accountService.findAll())
            return "register"
        }
        registrationEndpoint.register(registerForm)
        return "welcome"
    }

}