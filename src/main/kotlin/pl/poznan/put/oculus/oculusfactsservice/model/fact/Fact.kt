package pl.poznan.put.oculus.oculusfactsservice.model.fact

import io.swagger.annotations.ApiModel
import pl.poznan.put.oculus.oculusfactsservice.model.GrfIrf

@ApiModel(description = "fact used by inference service")
open class Fact (
        id: String?,
        head: String,
        set: List<String>,
        conjunction: String,
        val grfIrf: GrfIrf
) : Premise(id, head, set, conjunction)
