package pl.poznan.put.oculus.oculusfactsservice.model

import io.swagger.annotations.ApiModel
import org.springframework.data.mongodb.core.mapping.Document

@Document
@ApiModel(description = "an entity that can be converted into fact")
data class Attribute (
        val name: String,
        val unit: AttributeUnit,
        val type: AttributeType,
        val regex: String,
        val values: List<String>,
        val range: AttributeRange
)

@ApiModel(description = "unit in which the attribute is given")
enum class AttributeUnit {
    // lenght
    METER,
    CENTIMETER,

    // weight
    KILOGRAM,
    GRAM,

    // angle
    DEGREE,

    // other
    UNIT,
    PERCENT,
    NONE
}

@ApiModel(description = "data type of the value of the attribute")
enum class AttributeType {
    INT, UINT, FLOAT, UFLOAT, STRING
}

@ApiModel(description = "possible range for numeric attributes")
data class AttributeRange (
        val min: String,
        val max: String
)
