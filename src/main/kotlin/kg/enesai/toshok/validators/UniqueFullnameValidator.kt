package kg.enesai.toshok.validators

import kg.enesai.toshok.services.AccountService
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass


@Constraint(validatedBy = [UniqueFullnameValidator::class])
@Target(allowedTargets = [AnnotationTarget.FIELD])
@Retention(AnnotationRetention.RUNTIME)
annotation class UniqueFullname(
        val message: String = "Fullname already exists",
        val groups: Array<KClass<*>> = arrayOf(),
        val payload: Array<KClass<out Payload>> = arrayOf()
)


class UniqueFullnameValidator(
        private val accountService: AccountService
) : ConstraintValidator<UniqueFullname, String> {

    override fun initialize(uniqueFullname: UniqueFullname) {}

    override fun isValid(uniqueFullname: String, cxt: ConstraintValidatorContext): Boolean {

        return accountService.findByFullname(uniqueFullname) == null
    }
}