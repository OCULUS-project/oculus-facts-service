package pl.poznan.put.oculus.oculusfactsservice.validator

import pl.poznan.put.oculus.oculusfactsservice.exception.EmptyNameException
import pl.poznan.put.oculus.oculusfactsservice.exception.EmptyValueException
import pl.poznan.put.oculus.oculusfactsservice.exception.InvalidTypeException
import pl.poznan.put.oculus.oculusfactsservice.exception.OculusValidationException
import pl.poznan.put.oculus.oculusfactsservice.exception.PatternNotMatchedException
import pl.poznan.put.oculus.oculusfactsservice.exception.ValueNotAllowedException
import pl.poznan.put.oculus.oculusfactsservice.exception.ValueOutsideAllowedRangeException
import pl.poznan.put.oculus.oculusfactsservice.model.AttributeRange
import pl.poznan.put.oculus.oculusfactsservice.model.AttributeTemplate
import pl.poznan.put.oculus.oculusfactsservice.model.AttributeType

internal class AttributeValidator (
        private val name: String,
        private val value: String,
        private val template: AttributeTemplate
) {
    fun validate(): List<OculusValidationException> {
        try {
            validateConstraints()
        } catch (e: OculusValidationException) {
            return listOf(e)
        }
        return emptyList()
    }

    private fun validateConstraints() {
        if (name.isBlank()) throw EmptyNameException(name, value, template)
        if (value.isBlank()) throw EmptyValueException(name, value, template)

        validateType()
        validateValues()
        validateRegex()
        validateRange()
    }

    private fun validateType() {
        try {
            when (template.type) {
                AttributeType.INT -> validateInt()
                AttributeType.UINT -> validateUnsignedInt()
                AttributeType.FLOAT -> validateFloat()
                AttributeType.UFLOAT -> validateUnsignedFloat()
                AttributeType.STRING -> validateString()
            }
        } catch (e: NumberFormatException) {
            throw InvalidTypeException(AttributeType.STRING, name, value, template)
        }
    }

    private fun validateInt() {
        value.toInt()
    }

    private fun validateUnsignedInt() {
        val number = value.toInt()
        if (number < 0) throw InvalidTypeException(AttributeType.INT, name, value, template)
    }

    private fun validateFloat() {
        value.toFloat()
    }

    private fun validateUnsignedFloat() {
        val number = value.toFloat()
        if (number < 0) throw InvalidTypeException(AttributeType.FLOAT, name, value, template)
    }

    private fun validateString() {
        if (value.isBlank()) throw EmptyValueException(name, value, template)
    }

    private fun validateValues() {
        if (template.values.isNotEmpty()) {
            if (!template.values.contains(value)) throw ValueNotAllowedException(name, value, template)
        }
    }

    private fun validateRegex() {
        if (template.regex.isNotEmpty()) {
            if (!value.matches(Regex(template.regex))) throw PatternNotMatchedException(name, value, template)
        }
    }

    private fun validateRange() {
        if (!(template.range.min == "0" && template.range.max == "0")) {
            if (template.type != AttributeType.STRING) {
                if (value notIn template.range) throw ValueOutsideAllowedRangeException(name, value, template)
            }
        }
    }

    private infix fun String.notIn(range: AttributeRange) = !checkRange(this, range.min, range.max)

    private fun checkRange(n: String, min: String,max: String) = checkRange(n.toFloat(), min.toFloat(), max.toFloat())

    private fun checkRange(n: Float, min: Float,max: Float) = n in min..max
}
