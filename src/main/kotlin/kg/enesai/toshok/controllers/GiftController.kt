package kg.enesai.toshok.controllers

import kg.enesai.toshok.dtos.AccountSearchDto
import kg.enesai.toshok.dtos.GiftCreateForm
import kg.enesai.toshok.services.AccountService
import kg.enesai.toshok.services.GiftService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Controller
@RequestMapping("gift")
class GiftController(
        private val accountService: AccountService,
        private val giftService: GiftService
) {
    @GetMapping("/gift-needed")
    fun getGiftNeeded(@ModelAttribute("accountSearchDto") accountSearchDto: AccountSearchDto, pageable: Pageable, model: Model): String {
        model.addAttribute("accounts", accountService.findAllGiftNeededAccounts(accountSearchDto.fullname!!, pageable))
        return "gift/giftNeededAccountList"
    }

    @GetMapping("/getGiftCreateForm/{accountId}")
    fun getGiftCreateForm(@PathVariable accountId: Int, @ModelAttribute("giftCreateForm") giftCreateForm: GiftCreateForm, model: Model): String {
        giftCreateForm.accountId = accountId
        return "gift/giftCreateForm"
    }

    @PostMapping
    fun create(@Valid @ModelAttribute("giftCreateForm") giftCreateForm: GiftCreateForm, bindingResult: BindingResult, model: Model): String {
        if(bindingResult.hasErrors()){
            return "gift/giftCreateForm"
        }
        giftService.create(giftCreateForm)
        return "redirect:/gift/gift-needed"
    }
}