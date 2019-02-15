package kg.enesai.toshok.validators

import kg.enesai.toshok.services.AccountService
import org.apache.commons.beanutils.BeanUtils
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass


@Constraint(validatedBy = [FourApprovedChildsOnUpdateValidator::class])
@Target(allowedTargets = [AnnotationTarget.CLASS])
@Retention(AnnotationRetention.RUNTIME)
annotation class FourApprovedChildsOnUpdate(
        val message: String = "Parent has already 4 approved childs.",
        val groups: Array<KClass<*>> = arrayOf(),
        val payload: Array<KClass<out Payload>> = arrayOf()
)


class FourApprovedChildsOnUpdateValidator(
        private val accountService: AccountService
) : ConstraintValidator<FourApprovedChildsOnUpdate, Any> {

    override fun initialize(fourApprovedChilds: FourApprovedChildsOnUpdate?) {}

    override fun isValid(any: Any, cxt: ConstraintValidatorContext): Boolean {
        val id = BeanUtils.getProperty(any, "id").toInt()
        val parentId = BeanUtils.getProperty(any, "parentId")?.toIntOrNull() ?: return true

        val parentsChilds = accountService.findAllApprovedByParentId(parentId)
        val isValid = parentsChilds.size < 4 || parentsChilds.map { it.id }.contains(id)

        if(!isValid) {
            cxt.disableDefaultConstraintViolation()
            cxt.buildConstraintViolationWithTemplate(cxt.defaultConstraintMessageTemplate).addPropertyNode("parentId").addConstraintViolation()
        }
        return isValid
    }

}