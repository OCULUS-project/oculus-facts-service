package pl.poznan.put.oculus.oculusfactsservice.model.fact

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.data.mongodb.core.mapping.Document
import pl.poznan.put.oculus.oculusfactsservice.model.GrfIrf

@Document
@ApiModel(description = "a fact connected to job and with source specified")
class SourceFact (
        id: String?,
        head: String,
        set: List<String>,
        conjunction: String,
        grfIrf: GrfIrf,
        @ApiModelProperty(value = "the job the fact belongs to")
        val job: String,
        @ApiModelProperty(value = "source of the fact")
        val source: FactSource
) : Fact(id, head, set, conjunction, grfIrf)
