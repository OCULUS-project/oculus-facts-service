package pl.poznan.put.oculus.oculusfactsservice.validator

import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.verify
import org.junit.jupiter.api.Test
import pl.poznan.put.oculus.oculusfactsservice.model.Attribute
import pl.poznan.put.oculus.oculusfactsservice.model.AttributeRange
import pl.poznan.put.oculus.oculusfactsservice.model.AttributeType
import pl.poznan.put.oculus.oculusfactsservice.model.AttributeUnit

class AttributesValidatorTest {

    private val templates = listOf(
            Attribute("weight", AttributeUnit.KILOGRAM, AttributeType.UFLOAT),
            Attribute("height", AttributeUnit.KILOGRAM, AttributeType.UINT),
            Attribute("initials", AttributeUnit.NONE, AttributeType.STRING, regex = "[A-Z][A-Z]"),
            Attribute("sex", AttributeUnit.NONE, AttributeType.STRING, values = listOf("MAN", "WOMAN")),
            Attribute("score", AttributeUnit.NONE, AttributeType.FLOAT, range = AttributeRange("-1", "1"))
    )

    private val validator = AttributesValidator(templates)

    @Test
    fun `should validate given attributes`() = mockkConstructor(AttributeValidator::class) {
        // given
        val attributes = mapOf(
                "weight" to "1",
                "height" to "1",
                "initials" to "AB"
        )
        every { anyConstructed<AttributeValidator>().validate() } returns emptyList()

        // when
        validator.validate(attributes)

        // then
        verify (exactly = 3) { anyConstructed<AttributeValidator>().validate() }
    }

    @Test
    fun `should validate given attribute`() = mockkConstructor(AttributeValidator::class) {
        // given
        val attribute = "weight" to "1"
        every { anyConstructed<AttributeValidator>().validate() } returns emptyList()

        // when
        validator.validate(attribute)

        // then
        verify (exactly = 1) { anyConstructed<AttributeValidator>().validate() }
    }
}
