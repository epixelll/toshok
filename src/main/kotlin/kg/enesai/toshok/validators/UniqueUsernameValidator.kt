package kg.enesai.toshok.validators

import kg.enesai.toshok.services.UserService
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass


@Constraint(validatedBy = [UniqueUsernameValidator::class])
@Target(allowedTargets = [AnnotationTarget.FIELD])
@Retention(AnnotationRetention.RUNTIME)
annotation class UniqueUsername(
        val message: String = "Username already exists",
        val groups: Array<KClass<*>> = arrayOf(),
        val payload: Array<KClass<out Payload>> = arrayOf()
)


class UniqueUsernameValidator(
        private val userService: UserService
) : ConstraintValidator<UniqueUsername, String> {

    override fun initialize(uniqueUsername: UniqueUsername) {}

    override fun isValid(uniqueUsername: String, cxt: ConstraintValidatorContext): Boolean {

        return userService.findByUsername(uniqueUsername) == null
    }
}