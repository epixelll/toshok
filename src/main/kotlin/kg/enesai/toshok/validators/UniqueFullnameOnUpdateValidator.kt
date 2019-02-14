package kg.enesai.toshok.validators

import kg.enesai.toshok.services.AccountService
import org.apache.commons.beanutils.BeanUtils
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass


@Constraint(validatedBy = [UniqueFullnameOnUpdateValidator::class])
@Target(allowedTargets = [AnnotationTarget.CLASS])
@Retention(AnnotationRetention.RUNTIME)
annotation class UniqueFullnameOnUpdate(
        val message: String = "Fullname already exists",
        val groups: Array<KClass<*>> = arrayOf(),
        val payload: Array<KClass<out Payload>> = arrayOf()
)


class UniqueFullnameOnUpdateValidator(
        private val accountService: AccountService
) : ConstraintValidator<UniqueFullnameOnUpdate, Any> {

    override fun initialize(constraintAnnotation: UniqueFullnameOnUpdate) {}

    override fun isValid(any: Any, cxt: ConstraintValidatorContext): Boolean {
        val id = BeanUtils.getProperty(any, "id").toInt()
        val fullname = BeanUtils.getProperty(any, "fullname")
        val account = accountService.findByFullname(fullname)
        val isValid =  account == null || account.id == id
        if(!isValid) {
            cxt.disableDefaultConstraintViolation()
            cxt.buildConstraintViolationWithTemplate(cxt.defaultConstraintMessageTemplate).addPropertyNode("fullname").addConstraintViolation()
        }
        return isValid
    }
}