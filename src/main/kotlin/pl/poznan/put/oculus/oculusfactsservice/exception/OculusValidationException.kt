package pl.poznan.put.oculus.oculusfactsservice.exception

import pl.poznan.put.oculus.oculusfactsservice.model.Attribute
import pl.poznan.put.oculus.oculusfactsservice.model.AttributeType

open class OculusValidationException (
        msg: String,
        val attributeName: String,
        val attributeValue: String,
        val template: Attribute
) : OculusException(msg) {
    override val details: Map<String, Any>
        get() = mapOf(
                "name" to attributeName,
                "value" to attributeValue,
                "template" to template
        )
}

class EmptyNameException (
        attributeName: String,
        attributeValue: String,
        template: Attribute
) : OculusValidationException(
        "The key should not be empty",
        attributeName, attributeValue, template
)

class EmptyValueException (
        attributeName: String,
        attributeValue: String,
        template: Attribute
) : OculusValidationException(
        "The value should not be empty",
        attributeName, attributeValue, template
)

class NoSuchAttributeException (
        attributeName: String
) : OculusValidationException(
        "Cannot find attribute name specified: $attributeName",
        attributeName, "", Attribute.empty()
)

class InvalidTypeException(
        givenType: AttributeType,
        attributeName: String,
        attributeValue: String,
        template: Attribute
) : OculusValidationException(
        "Expecting value of type ${template.type} instead of $givenType",
        attributeName, attributeValue, template
)

class ValueNotAllowedException (
        attributeName: String,
        attributeValue: String,
        template: Attribute
) : OculusValidationException(
        "The given value \"$attributeValue\" is not allowed for this attribute. The possibilities are: ${template.values.joinToString()}",
        attributeName, attributeValue, template
)

class PatternNotMatchedException (
        attributeName: String,
        attributeValue: String,
        template: Attribute
) : OculusValidationException(
        "The given value \"$attributeValue\" does not match the pattern ${template.regex}",
        attributeName, attributeValue, template
)

class ValueOutsideAllowedRangeException (
        attributeName: String,
        attributeValue: String,
        template: Attribute
) : OculusValidationException(
        "The given value \"$attributeValue\" does not fit into the range <${template.range.min}, ${template.range.max}>",
        attributeName, attributeValue, template
)