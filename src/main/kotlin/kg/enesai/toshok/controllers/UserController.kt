package kg.enesai.toshok.controllers

import kg.enesai.toshok.dtos.AccountSearchDto
import kg.enesai.toshok.dtos.ChangePasswordForm
import kg.enesai.toshok.dtos.UserCreateForm
import kg.enesai.toshok.dtos.UserUpdateForm
import kg.enesai.toshok.services.AccountService
import kg.enesai.toshok.services.RoleService
import kg.enesai.toshok.services.UserService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Controller
@RequestMapping("user")
class UserController(
        private val userService: UserService,
        private val accountService: AccountService,
        private val roleService: RoleService
) {
    @GetMapping("/list")
    fun getUserList(@ModelAttribute("username") username: String = "", pageable: Pageable, model: Model): String {
        model.addAttribute("users", userService.findAllByUsername(username, pageable))
        return "user/userList"
    }

    @GetMapping("/getUserCreateForm")
    fun getUserCreateForm(@ModelAttribute("userCreateForm") userCreateForm: UserCreateForm, model: Model): String {
        model.addAttribute("roles", roleService.findAll())
        val accountSearchDto = AccountSearchDto("", null, null)
        model.addAttribute("accounts", accountService.findAll(accountSearchDto, PageRequest.of(0, 20)))
        return "user/userCreateForm"
    }

    @GetMapping("/getUserUpdateForm/{id}")
    fun getUserUpdateForm(@PathVariable id: Int, model: Model): String {
        model.addAttribute("userUpdateForm", userService.getUpdateForm(id))
        model.addAttribute("roles", roleService.findAll())
        val accountSearchDto = AccountSearchDto("", null, null)
        model.addAttribute("accounts", accountService.findAll(accountSearchDto, PageRequest.of(0, 20)))
        return "user/userUpdateForm"
    }

    @PostMapping()
    fun create(@Valid @ModelAttribute("userCreateForm") userCreateForm: UserCreateForm, bindingResult: BindingResult, model: Model): String {
        if(bindingResult.hasErrors()){
            model.addAttribute("roles", roleService.findAll())
            val accountSearchDto = AccountSearchDto("", null, null)
            model.addAttribute("accounts", accountService.findAll(accountSearchDto, PageRequest.of(0, 20)))
            return "user/userCreateForm"
        }
        userService.create(userCreateForm)
        return "redirect:/user/list"
    }

    @PostMapping("/update")
    fun update(@Valid @ModelAttribute("userUpdateForm") userUpdateForm: UserUpdateForm, bindingResult: BindingResult, model: Model): String {
        if(bindingResult.hasErrors()){
            model.addAttribute("roles", roleService.findAll())
            val accountSearchDto = AccountSearchDto("", null, null)
            model.addAttribute("accounts", accountService.findAll(accountSearchDto, PageRequest.of(0, 20)))
            return "user/userUpdateForm"
        }
        userService.update(userUpdateForm)
        return "redirect:/user/list"
    }

    @GetMapping("/delete/{id}")
    fun delete(@PathVariable id: Int): String {
        userService.delete(id)
        return "redirect:/user/list"
    }

    @GetMapping("/profile")
    fun profile(model: Model): String {
        model.addAttribute("profile", userService.getProfile())
        return "user/profile"
    }

    @GetMapping("/getChangePasswordForm")
    fun getChangePasswordForm(@ModelAttribute("changePasswordForm") changePasswordForm: ChangePasswordForm, model: Model): String {
        return "user/changePasswordForm"
    }

    @PostMapping("/changePassword")
    fun changePassword(@Valid @ModelAttribute("changePasswordForm") changePasswordForm: ChangePasswordForm, bindingResult: BindingResult, model: Model): String {
        if(bindingResult.hasErrors()){
            return "user/changePasswordForm"
        }
        userService.changePassword(changePasswordForm)
        return "redirect:/login"
    }
}