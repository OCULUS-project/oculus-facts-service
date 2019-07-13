package pl.poznan.put.oculus.oculusfactsservice.exception

import junit.framework.TestCase.assertEquals
import org.junit.Test

class OculusExceptionTest {

    private val message = "Sample error message"

    @Test
    fun `should return error message`() {
        // given
        val exception = SampleErrorException(message)

        // when
        val msg = exception.errorMessage()

        // then
        assertEquals("SAMPLE_ERROR", msg.error)
        assertEquals(message, msg.message)
        assertEquals(emptyMap<String, Any>(), msg.details)
    }

    @Test
    fun `should return error message with details`() {
        // given
        val exception = SampleErrorWithDetailsException(message)
        val expectedDetails = mapOf("cause" to "some", "user" to "doctor", "time" to "today")

        // when
        val msg = exception.errorMessage()

        // then
        assertEquals("SAMPLE_ERROR_WITH_DETAILS", msg.error)
        assertEquals(message, msg.message)
        assertEquals(expectedDetails, msg.details)
    }

    private class SampleErrorException (
            message: String
    ) : OculusException(message)

    private class SampleErrorWithDetailsException (
            message: String
    ) : OculusException(message) {
        override val details: Map<String, Any>
            get() = mapOf("cause" to "some", "user" to "doctor", "time" to "today")
    }
}
