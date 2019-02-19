package kg.enesai.toshok.controllers

import kg.enesai.toshok.dtos.*
import kg.enesai.toshok.enums.AccountStatus
import kg.enesai.toshok.enums.Permission
import kg.enesai.toshok.services.AccountService
import kg.enesai.toshok.services.GiftService
import kg.enesai.toshok.services.RegionService
import kg.enesai.toshok.services.UserService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
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
        private val userService: UserService,
        private val giftService: GiftService
) {
    @GetMapping("/list")
    fun getAccountList(@ModelAttribute("accountSearchDto") accountSearchDto: AccountSearchDto, pageable: Pageable, model: Model): String {
        val accountsPage = accountService.findAll(accountSearchDto, pageable)
        model.addAttribute("accounts", accountsPage)
        model.addAttribute("statuses", AccountStatus.values())
        model.addAttribute("regions", regionService.findAll())
        if(pageable.pageNumber >= accountsPage.totalElements) model.addAttribute("pageable", PageRequest.of(accountsPage.totalPages, pageable.pageSize))
        return "account/accountList"
    }

    @GetMapping("/search")
    @ResponseBody
    fun search(@RequestParam("term") term: String): List<AccountSearchResultDto> {
        return accountService.find20ByTerm(term)
    }


    @GetMapping("/info/{id}")
    fun getAccountInfo(authentication: Authentication, @PathVariable id: Int, model: Model): String {
        val principal = authentication.principal as User
        val user = userService.findByUsername(principal.username)!!
        if(!authentication.authorities.map { it.authority }.contains(Permission.ACCOUNT_VIEW.toString()) && id != user.account?.id ){
            if(user.account == null) return """redirect:/user/profile"""
            return """redirect:/account/info/${user.account?.id}"""
        }
        model.addAttribute("account", accountService.getAccountInfo(id))
        model.addAttribute("gifts", giftService.findAllByAccountId(id))
        return "account/accountInfo"
    }

//    @GetMapping("/getAccountCreateForm")
//    fun getAccountCreateForm(@ModelAttribute("accountCreateForm") accountCreateForm: AccountCreateForm, model: Model): String {
//        model.addAttribute("regions", regionService.findAll())
//        model.addAttribute("accounts", accountService.findAll())
//        return "account/accountCreateForm"
//    }

    @GetMapping("/getAccountUpdateForm/{id}")
    fun getAccountUpdateForm(@PathVariable id: Int, model: Model): String {
        val accountUpdateForm = accountService.getUpdateForm(id)
        model.addAttribute("accountUpdateForm", accountUpdateForm)
        model.addAttribute("regions", regionService.findAll())
        accountUpdateForm.parentId?.let { id -> model.addAttribute("parent", accountService.findById(id).let { AccountDto.of(it) }) }
        return "account/accountUpdateForm"
    }

//    @PostMapping()
//    fun create(@Valid @ModelAttribute("accountCreateForm") accountCreateForm: AccountCreateForm, bindingResult: BindingResult, model: Model): String {
//        if(bindingResult.hasErrors()){
//            model.addAttribute("regions", regionService.findAll())
//            model.addAttribute("accounts", accountService.findAll())
//            return "account/accountCreateForm"
//        }
//        accountService.create(accountCreateForm)
//        return "redirect:/account/list"
//    }

    @PostMapping("/update")
    fun update(@Valid @ModelAttribute("accountUpdateForm") accountUpdateForm: AccountUpdateForm, bindingResult: BindingResult, model: Model): String {
        if(bindingResult.hasErrors()){
            model.addAttribute("regions", regionService.findAll())
            accountUpdateForm.parentId?.let { id -> model.addAttribute("parent", accountService.findById(id).let { AccountDto.of(it) }) }
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
    fun getApproveList(@ModelAttribute("accountSearchDto") accountSearchDto: AccountSearchDto, pageable: Pageable, model: Model): String {
        val pendingPage = accountService.findAllPending(accountSearchDto.fullname!!, pageable)
        model.addAttribute("accounts", pendingPage)
        if(pageable.pageNumber >= pendingPage.totalPages) model.addAttribute("pageable", PageRequest.of(pendingPage.totalPages, pageable.pageSize))
        return "account/approveList"
    }

    @GetMapping("/approve/{id}")
    fun approve(@PathVariable id: Int, @RequestParam page: Int): String {
        accountService.approve(id)
        return "redirect:/account/approve-list?page=$page"
    }

//    @GetMapping("/give-gift/{id}")
//    fun giveGift(@PathVariable id: Int): String {
//        accountService.giveGift(id)
//        return "redirect:/account/gift-needed"
//    }
}