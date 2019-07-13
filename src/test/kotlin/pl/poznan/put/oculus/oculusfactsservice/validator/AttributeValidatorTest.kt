package pl.poznan.put.oculus.oculusfactsservice.validator

import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import pl.poznan.put.oculus.oculusfactsservice.exception.ErrorMessage
import pl.poznan.put.oculus.oculusfactsservice.model.Attribute
import pl.poznan.put.oculus.oculusfactsservice.model.AttributeRange
import pl.poznan.put.oculus.oculusfactsservice.model.AttributeType
import pl.poznan.put.oculus.oculusfactsservice.model.AttributeUnit

class AttributeValidatorTest {

    private companion object {
        private const val NAME = "some name"
    }

    @Test
    fun `should return error when given empty name`() {
        // given
        val name = ""
        val value = "value"
        val template = Attribute.empty()
        val validator = AttributeValidator(name, value, template)

        // when
        val result = validator.validate()

        // then
        assertEquals(1, result.size)
        assertErrorMessage(name, value, template, "EMPTY_NAME", result.first())
    }

    @Test
    fun `should return error when given empty value`() {
        // given
        val value = ""
        val template = Attribute.empty()
        val validator = AttributeValidator(NAME, value, template)

        // when
        val result = validator.validate()

        // then
        assertEquals(1, result.size)
        assertErrorMessage(NAME, value, template, "EMPTY_VALUE", result.first())
    }

    @DisplayName("should validate INT type")
    @ParameterizedTest(name = "for {0}")
    @ValueSource(strings = ["1", "123", "-1", "-123"])
    fun `should validate INT type` (value: String) {
        // given
        val template = Attribute(NAME, AttributeUnit.UNIT, AttributeType.INT)
        val validator = AttributeValidator(NAME, value, template)

        // when
        val result = validator.validate()

        // then
        assertEquals(0, result.size)
    }

    @DisplayName("should validate invalid INT type")
    @ParameterizedTest(name = "for {0}")
    @ValueSource(strings = ["1.0", "1.1", "123.123", "-1.1", "-123.123", "abc", "ABC", "abc def"])
    fun `should validate invalid INT type` (value: String) {
        // given
        val template = Attribute(NAME, AttributeUnit.UNIT, AttributeType.INT)
        val validator = AttributeValidator(NAME, value, template)

        // when
        val result = validator.validate()

        // then
        assertEquals(1, result.size)
        assertErrorMessage(NAME, value, template, "INVALID_TYPE", result.first())
    }

    @DisplayName("should validate UINT type")
    @ParameterizedTest(name = "for {0}")
    @ValueSource(strings = ["1", "123"])
    fun `should validate UINT type` (value: String) {
        // given
        val template = Attribute(NAME, AttributeUnit.UNIT, AttributeType.UINT)
        val validator = AttributeValidator(NAME, value, template)

        // when
        val result = validator.validate()

        // then
        assertEquals(0, result.size)
    }

    @DisplayName("should validate invalid UINT type")
    @ParameterizedTest(name = "for {0}")
    @ValueSource(strings = ["-1", "-123", "1.0", "1.1", "123.123", "-1.1", "-123.123", "abc", "ABC", "abc def"])
    fun `should validate invalid UINT type` (value: String) {
        // given
        val template = Attribute(NAME, AttributeUnit.UNIT, AttributeType.UINT)
        val validator = AttributeValidator(NAME, value, template)

        // when
        val result = validator.validate()

        // then
        assertEquals(1, result.size)
        assertErrorMessage(NAME, value, template, "INVALID_TYPE", result.first())
    }

    @DisplayName("should validate FLOAT type")
    @ParameterizedTest(name = "for {0}")
    @ValueSource(strings = ["1.0", "1.1", "123.123", "-1.1", "-123.123"])
    fun `should validate FLOAT type` (value: String) {
        // given
        val template = Attribute(NAME, AttributeUnit.UNIT, AttributeType.FLOAT)
        val validator = AttributeValidator(NAME, value, template)

        // when
        val result = validator.validate()

        // then
        assertEquals(0, result.size)
    }

    @DisplayName("should validate invalid FLOAT type")
    @ParameterizedTest(name = "for {0}")
    @ValueSource(strings = ["abc", "ABC", "abc def"])
    fun `should validate invalid FLOAT type` (value: String) {
        // given
val template = Attribute(NAME, AttributeUnit.UNIT, AttributeType.FLOAT)
        val validator = AttributeValidator(NAME, value, template)

        // when
        val result = validator.validate()

        // then
        assertEquals(1, result.size)
        assertErrorMessage(NAME, value, template, "INVALID_TYPE", result.first())
    }

    @DisplayName("should validate UFLOAT type")
    @ParameterizedTest(name = "for {0}")
    @ValueSource(strings = ["1.0", "1.1", "123.123"])
    fun `should validate UFLOAT type` (value: String) {
        // given
        val template = Attribute(NAME, AttributeUnit.UNIT, AttributeType.UFLOAT)
        val validator = AttributeValidator(NAME, value, template)

        // when
        val result = validator.validate()

        // then
        assertEquals(0, result.size)
    }

    @DisplayName("should validate invalid UFLOAT type")
    @ParameterizedTest(name = "for {0}")
    @ValueSource(strings = ["-1.1", "-123.123", "abc", "ABC", "abc def"])
    fun `should validate invalid UFLOAT type` (value: String) {
        // given
        val template = Attribute(NAME, AttributeUnit.UNIT, AttributeType.UFLOAT)
        val validator = AttributeValidator(NAME, value, template)

        // when
        val result = validator.validate()

        // then
        assertEquals(1, result.size)
        assertErrorMessage(NAME, value, template, "INVALID_TYPE", result.first())
    }

    @DisplayName("should validate STRING type")
    @ParameterizedTest(name = "for {0}")
    @ValueSource(strings = ["1", "123", "-1", "-123", "1.0", "1.1", "123.123", "-1.1", "-123.123", "abc", "ABC", "abc def"])
    fun `should validate STRING type` (value: String) {
        // given
        val template = Attribute(NAME, AttributeUnit.UNIT, AttributeType.STRING)
        val validator = AttributeValidator(NAME, value, template)

        // when
        val result = validator.validate()

        // then
        assertEquals(0, result.size)
    }

    @DisplayName("should validate invalid STRING type")
    @ParameterizedTest(name = "for {0}")
    @ValueSource(strings = [""])
    fun `should validate invalid STRING type` (value: String) {
        // given
        val template = Attribute(NAME, AttributeUnit.UNIT, AttributeType.STRING)
        val validator = AttributeValidator(NAME, value, template)

        // when
        val result = validator.validate()

        // then
        assertEquals(1, result.size)
        assertErrorMessage(NAME, value, template, "EMPTY_VALUE", result.first())
    }

    @Test
    fun `should validate allowed values`() {
        // given
        val value = "value"
        val template = Attribute(NAME, AttributeUnit.NONE, AttributeType.STRING, values = listOf("value"))
        val validator = AttributeValidator(NAME, value, template)

        // when
        val result = validator.validate()

        // then
        assertEquals(0, result.size)
    }

    @Test
    fun `should validate invalid allowed values`() {
        // given
        val value = "value1"
        val template = Attribute(NAME, AttributeUnit.NONE, AttributeType.STRING, values = listOf("value"))
        val validator = AttributeValidator(NAME, value, template)

        // when
        val result = validator.validate()

        // then
        assertEquals(1, result.size)
        assertErrorMessage(NAME, value, template, "VALUE_NOT_ALLOWED", result.first())
    }

    @Test
    fun `should validate regex`() {
        // given
        val value = "AB"
        val template = Attribute(NAME, AttributeUnit.NONE, AttributeType.STRING, regex = "[A-Z][A-Z]")
        val validator = AttributeValidator(NAME, value, template)

        // when
        val result = validator.validate()

        // then
        assertEquals(0, result.size)
    }

    @Test
    fun `should validate invalid regex`() {
        // given
        val value = "ab"
        val template = Attribute(NAME, AttributeUnit.NONE, AttributeType.STRING, regex = "[A-Z][A-Z]")
        val validator = AttributeValidator(NAME, value, template)

        // when
        val result = validator.validate()

        // then
        assertEquals(1, result.size)
        assertErrorMessage(NAME, value, template, "PATTERN_NOT_MATCHED", result.first())
    }

    @Test
    fun `should validate range`() {
        // given
        val value = "0.5"
        val template = Attribute(NAME, AttributeUnit.NONE, AttributeType.FLOAT, range = AttributeRange("0", "1"))
        val validator = AttributeValidator(NAME, value, template)

        // when
        val result = validator.validate()

        // then
        assertEquals(0, result.size)
    }

    @Test
    fun `should validate invalid range`() {
        // given
        val value = "1.5"
        val template = Attribute(NAME, AttributeUnit.NONE, AttributeType.FLOAT, range = AttributeRange("0", "1"))
        val validator = AttributeValidator(NAME, value, template)

        // when
        val result = validator.validate()

        // then
        assertEquals(1, result.size)
        assertErrorMessage(NAME, value, template, "VALUE_OUTSIDE_ALLOWED_RANGE", result.first())
    }

    private fun assertErrorMessage(
            name: String, value: String, template: Attribute, expectedError: String, message: ErrorMessage
    ) {
        assertEquals(expectedError, message.error)
        assertEquals(name, message.details["name"])
        assertEquals(value, message.details["value"])
        assertEquals(template, message.details["template"])
    }

}