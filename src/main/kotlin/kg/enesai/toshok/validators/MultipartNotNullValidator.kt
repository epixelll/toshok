package kg.enesai.toshok.validators

import org.springframework.web.multipart.MultipartFile
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass


@Constraint(validatedBy = [MultipartNotNullValidator::class])
@Target(allowedTargets = [AnnotationTarget.FIELD])
@Retention(AnnotationRetention.RUNTIME)
annotation class MultipartNotNull(
        val message: String = "Field is not nullable",
        val groups: Array<KClass<*>> = arrayOf(),
        val payload: Array<KClass<out Payload>> = arrayOf()
)


class MultipartNotNullValidator : ConstraintValidator<MultipartNotNull, MultipartFile?> {

    override fun initialize(multipartNotNull: MultipartNotNull) {}

    override fun isValid(multipartNotNull: MultipartFile?, cxt: ConstraintValidatorContext): Boolean {

        return multipartNotNull != null && !multipartNotNull.isEmpty
    }
}