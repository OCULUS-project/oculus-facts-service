package pl.poznan.put.oculus.oculusfactsservice.rest.response

import io.swagger.annotations.ApiModel
import pl.poznan.put.oculus.oculusfactsservice.exception.ErrorMessage

@ApiModel
data class AttributesErrors (
        val attributes: List<AttributeErrors>
)

@ApiModel
data class AttributeErrors (
        val attribute: AttributeMessage,
        val errors: List<ErrorMessage>
)

data class AttributeMessage (
        val name: String,
        val value: String
)
