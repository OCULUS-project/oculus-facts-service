package pl.poznan.put.oculus.oculusfactsservice.model.fact

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(description = "explains the source from which the fact was infered or generated")
data class FactSource (
        @ApiModelProperty(value = "source type")
        val type: FactSourceType,
        @ApiModelProperty(value = "id of source entity")
        val id: String
)

enum class FactSourceType {
    METRICS, IMAGE
}
