package pl.poznan.put.oculus.oculusfactsservice.model.fact

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import pl.poznan.put.oculus.oculusfactsservice.model.GrfIrf

@ApiModel(description = "a fact connected to job and with source specified")
class SourceFact (
        id: String? = null,
        head: String,
        set: List<String>,
        conjunction: Boolean,
        grfIrf: GrfIrf,
        @ApiModelProperty(value = "the job the fact belongs to")
        val job: String,
        @ApiModelProperty(value = "source of the fact")
        val source: FactSource
) : Fact(id, head, set, conjunction, grfIrf) {
        constructor(
                head: String, set: List<String>, conjunction: Boolean, grfIrf: GrfIrf, job: String, source: FactSource
        ) : this(null, head, set, conjunction, grfIrf, job, source)
}

@ApiModel(description = "explains the source from which the fact was infered or generated")
data class FactSource (
        @ApiModelProperty(value = "source type")
        val type: FactSourceType,
        @ApiModelProperty(value = "id of source entity")
        val id: String
)

enum class FactSourceType { METRICS, IMAGE }
