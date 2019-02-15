package kg.enesai.toshok.controllers

import kg.enesai.toshok.dtos.AccountDto
import kg.enesai.toshok.dtos.RegisterForm
import kg.enesai.toshok.services.AccountService
import kg.enesai.toshok.services.RegionService
import kg.enesai.toshok.services.endpoint.RegistrationEndpointService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid

@Controller
@RequestMapping("registration")
class RegistrationController(
        private val registrationEndpoint: RegistrationEndpointService,
        private val regionService: RegionService,
        private val accountService: AccountService
) {
    @GetMapping
    fun getRegister(@ModelAttribute("registerForm") registerForm: RegisterForm, model: Model): String {
        model.addAttribute("regions", regionService.findAll())
        registerForm.parentId?.let { id -> model.addAttribute("parent", accountService.findById(id).let { AccountDto.of(it) }) }
        return "register"
    }

    @PostMapping
    fun create(@Valid @ModelAttribute("registerForm") registerForm: RegisterForm, bindingResult: BindingResult, model: Model): String {
        if(bindingResult.hasErrors()){
            model.addAttribute("regions", regionService.findAll())
            registerForm.parentId?.let { id -> model.addAttribute("parent", accountService.findById(id).let { AccountDto.of(it) }) }
            return "register"
        }
        registrationEndpoint.register(registerForm)
        return "redirect:/login"
    }

    @PostMapping("/upload-csv")
    @ResponseBody
    fun import(@RequestParam("file") file: MultipartFile){
        registrationEndpoint.import(file)
    }
}