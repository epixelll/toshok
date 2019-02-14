package kg.enesai.toshok.validators

import kg.enesai.toshok.services.AccountService
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass


@Constraint(validatedBy = [FourApprovedChildsValidator::class])
@Target(allowedTargets = [AnnotationTarget.FIELD])
@Retention(AnnotationRetention.RUNTIME)
annotation class FourApprovedChilds(
        val message: String = "Parent has already 4 approved childs.",
        val groups: Array<KClass<*>> = arrayOf(),
        val payload: Array<KClass<out Payload>> = arrayOf()
)


class FourApprovedChildsValidator(
        private val accountService: AccountService
) : ConstraintValidator<FourApprovedChilds, Int?> {

    override fun initialize(fourApprovedChilds: FourApprovedChilds?) {}

    override fun isValid(parentId: Int?, cxt: ConstraintValidatorContext): Boolean {
        if(parentId == null) return true
        val parentsChilds = accountService.findAllApprovedByParentId(parentId)
        return parentsChilds.size < 4 || parentsChilds.map { it.id }.contains(parentId)
    }

}