package pl.poznan.put.oculus.oculusfactsservice.model

import io.swagger.annotations.ApiModel
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import pl.poznan.put.oculus.oculusfactsservice.model.fact.Fact
import pl.poznan.put.oculus.oculusfactsservice.model.fact.Premise

@Document
@ApiModel(description = "rule for inference service")
data class Rule (
        @Id
        val id: String?,
        val premises: List<Premise>,
        val conclusion: Fact,
        val grfIrf: GrfIrf
)
