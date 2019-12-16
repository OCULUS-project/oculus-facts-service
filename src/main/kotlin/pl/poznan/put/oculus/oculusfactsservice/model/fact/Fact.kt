package pl.poznan.put.oculus.oculusfactsservice.model.fact

import io.swagger.annotations.ApiModel
import pl.poznan.put.oculus.oculusfactsservice.model.GrfIrf

@ApiModel(description = "fact used by inference service")
abstract class Fact (
        id: String? = null,
        head: String,
        set: List<String>,
        conjunction: Boolean,
        val grfIrf: GrfIrf
) : Premise(id, head, set, conjunction)
