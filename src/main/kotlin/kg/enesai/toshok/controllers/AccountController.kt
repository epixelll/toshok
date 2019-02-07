package kg.enesai.toshok.controllers

import kg.enesai.toshok.dtos.AccountCreateForm
import kg.enesai.toshok.dtos.AccountUpdateForm
import kg.enesai.toshok.enums.Permission
import kg.enesai.toshok.services.AccountService
import kg.enesai.toshok.services.RegionService
import kg.enesai.toshok.services.UserService
import org.springframework.data.domain.Pageable
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Controller
@RequestMapping("account")
class AccountController(
        private val accountService: AccountService,
        private val regionService: RegionService,
        private val userService: UserService
) {
    @GetMapping("/list")
    fun getAccountList(pageable: Pageable, model: Model): String {
        model.addAttribute("accounts", accountService.findAll(pageable))
        return "account/accountList"
    }

    @GetMapping("/info/{id}")
    fun getAccountInfo(authentication: Authentication, @PathVariable id: Int, model: Model): String {
        val user = authentication.principal as User
        val userId = userService.findByUsername(user.username)?.id
        if(!authentication.authorities.map { it.authority }.contains(Permission.ACCOUNT_VIEW.toString()) && id != userId ){
            return """redirect:/account/info/$userId"""
        }
        model.addAttribute("account", accountService.getAccountInfo(id))
        return "account/accountInfo"
    }

    @GetMapping("/getAccountCreateForm")
    fun getAccountCreateForm(@ModelAttribute("accountCreateForm") accountCreateForm: AccountCreateForm, model: Model): String {
        model.addAttribute("regions", regionService.findAll())
        model.addAttribute("accounts", accountService.findAll())
        return "account/accountCreateForm"
    }

    @GetMapping("/getAccountUpdateForm/{id}")
    fun getAccountUpdateForm(@PathVariable id: Int, model: Model): String {
        model.addAttribute("accountUpdateForm", accountService.getUpdateForm(id))
        model.addAttribute("regions", regionService.findAll())
        model.addAttribute("accounts", accountService.findAll())
        return "account/accountUpdateForm"
    }

    @PostMapping()
    fun create(@Valid @ModelAttribute("accountCreateForm") accountCreateForm: AccountCreateForm, bindingResult: BindingResult, model: Model): String {
        if(bindingResult.hasErrors()){
            model.addAttribute("regions", regionService.findAll())
            model.addAttribute("accounts", accountService.findAll())
            return "account/accountCreateForm"
        }
        accountService.create(accountCreateForm)
        return "redirect:/account/list"
    }

    @PostMapping("/update")
    fun update(@Valid @ModelAttribute("accountUpdateForm") accountUpdateForm: AccountUpdateForm, bindingResult: BindingResult, model: Model): String {
        if(bindingResult.hasErrors()){
            model.addAttribute("regions", regionService.findAll())
            model.addAttribute("accounts", accountService.findAll())
            return "account/accountUpdateForm"
        }
        accountService.update(accountUpdateForm)
        return "redirect:/account/list"
    }

    @GetMapping("/delete/{id}")
    fun delete(@PathVariable id: Int): String {
        accountService.delete(id)
        return "redirect:/account/list"
    }

    @GetMapping("/approve-list")
    fun getApproveList(pageable: Pageable, model: Model): String {
        model.addAttribute("accounts", accountService.findAllPending(pageable))
        return "account/approveList"
    }

    @GetMapping("/approve/{id}")
    fun approve(@PathVariable id: Int): String {
        accountService.approve(id)
        return "redirect:/account/approve-list"
    }
}