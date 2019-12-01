package pl.poznan.put.oculus.oculusfactsservice.model.fact

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import pl.poznan.put.oculus.oculusfactsservice.model.GrfIrf

@ApiModel(description = "a fact generated by the job")
class ResultFact (
        id: String?,
        head: String,
        set: List<String>,
        conjunction: Boolean,
        grfIrf: GrfIrf,
        @ApiModelProperty(value = "the job the fact belongs to")
        val job: String
) : Fact(id, head, set, conjunction, grfIrf)
