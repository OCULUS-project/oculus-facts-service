package pl.poznan.put.oculus.oculusfactsservice.exception

abstract class OculusException (
        message: String
) : Exception(message) {

    fun errorMessage() = ErrorMessage(name, message!!, details)

    private val name: String
        get() = this::class.simpleName!!
                .replace("Exception", "")
                .replace(Regex("([a-z])([A-Z])"), "$1_$2")
                .toUpperCase()

    protected open val details: Map<String, Any>
        get() = emptyMap()
}

data class ErrorMessage (
        val error: String,
        val message: String,
        val details: Map<String, Any>
)
