package pl.poznan.put.oculus.oculusfactsservice.validator

import pl.poznan.put.oculus.boot.exception.ErrorMessage
import pl.poznan.put.oculus.oculusfactsservice.exception.NoSuchAttributeException
import pl.poznan.put.oculus.oculusfactsservice.exception.OculusValidationException
import pl.poznan.put.oculus.oculusfactsservice.model.Attribute

class AttributesValidator (
        private val templates: List<Attribute>
) {
    fun validate(attributes: Map<String, String>) = attributes
            .map { it.toPair() }
            .map { it to validate(it)}

    fun validate(attribute: Pair<String, String>) = validate(attribute.first, attribute.second)

    private fun validate(name: String, value: String): List<ErrorMessage> {
        val template = try {
            getTemplate(name)
        } catch (e: OculusValidationException) {
            return listOf(e.errorMessage())
        }
        return validate(name, value, template)
    }

    private fun validate(name: String, value: String, template: Attribute): List<ErrorMessage> {
        return AttributeValidator(name, value, template).validate()
    }

    private fun getTemplate(name: String) = templates.find {
        it.name == name
    } ?: throw NoSuchAttributeException(name)
}
