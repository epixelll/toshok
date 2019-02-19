package kg.enesai.toshok.validators

import org.apache.commons.beanutils.BeanUtils
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass


@Constraint(validatedBy = [ParentNotNullValidator::class])
@Target(allowedTargets = [AnnotationTarget.CLASS])
@Retention(AnnotationRetention.RUNTIME)
annotation class ParentNotNull(
        val message: String = "Parent id shouldn't be null",
        val groups: Array<KClass<*>> = arrayOf(),
        val payload: Array<KClass<out Payload>> = arrayOf()
)


class ParentNotNullValidator: ConstraintValidator<ParentNotNull, Any> {

    override fun initialize(constraintAnnotation: ParentNotNull) {}

    override fun isValid(any: Any, cxt: ConstraintValidatorContext): Boolean {
        val topOfHierarchy = BeanUtils.getProperty(any, "topOfHierarchy")!!.toBoolean()
        val parentId = BeanUtils.getProperty(any, "parentId")
        val isValid =  topOfHierarchy || (parentId != null && parentId.isNotEmpty())
        if(!isValid) {
            cxt.disableDefaultConstraintViolation()
            cxt.buildConstraintViolationWithTemplate(cxt.defaultConstraintMessageTemplate).addPropertyNode("parentId").addConstraintViolation()
        }
        return isValid
    }
}