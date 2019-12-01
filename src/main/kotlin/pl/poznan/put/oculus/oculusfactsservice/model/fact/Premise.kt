package pl.poznan.put.oculus.oculusfactsservice.model.fact

import io.swagger.annotations.ApiModel
import org.springframework.data.annotation.Id

@ApiModel(description = "premise for a rule")
open class Premise(
        @Id
        val id: String?,
        val head: String,
        val set: List<String>,
        val conjunction: Boolean
)
