package pl.poznan.put.oculus.oculusfactsservice.validator

import pl.poznan.put.oculus.oculusfactsservice.exception.NoSuchAttributeException
import pl.poznan.put.oculus.oculusfactsservice.exception.OculusValidationException
import pl.poznan.put.oculus.oculusfactsservice.model.AttributeTemplate

class AttributesValidator (
        private val templates: List<AttributeTemplate>
) {
    fun validate(attributes: Map<String, String>) = attributes
            .map { it.toPair() }
            .map { it to validate(it)}

    fun validate(attribute: Pair<String, String>) = validate(attribute.first, attribute.second)

    private fun validate(name: String, value: String): List<OculusValidationException> {
        val template = try {
            getTemplate(name)
        } catch (e: OculusValidationException) {
            return listOf(e)
        }
        return validate(name, value, template)
    }

    private fun validate(name: String, value: String, template: AttributeTemplate): List<OculusValidationException> {
        return AttributeValidator(name, value, template).validate()
    }

    private fun getTemplate(name: String) = templates.find {
        it.name == name
    } ?: throw NoSuchAttributeException(name)
}
