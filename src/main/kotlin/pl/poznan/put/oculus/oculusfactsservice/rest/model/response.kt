package pl.poznan.put.oculus.oculusfactsservice.rest.model

import io.swagger.annotations.ApiModel
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import pl.poznan.put.oculus.boot.exception.ErrorMessage
import pl.poznan.put.oculus.oculusfactsservice.model.AttributeTemplate
import pl.poznan.put.oculus.oculusfactsservice.model.fact.SourceFact
import pl.poznan.put.oculus.oculusfactsservice.rest.AttributesController

@ApiModel
data class AttributeResponse (
        val name: String,
        val value: String
)

@ApiModel
data class AttributeErrorsResponse (
        val attribute: AttributeResponse,
        val errors: List<ErrorMessage>
)

@ApiModel
data class AttributesErrorsResponse (
        val attributes: List<AttributeErrorsResponse>
) : RepresentationModel<AttributesErrorsResponse>() {
    init {
        add(linkTo(AttributesController::class.java).slash("validate").withSelfRel())
        add(linkTo(methodOn(AttributesController::class.java).getAllTemplates()).withRel(IanaLinkRelations.DESCRIBED_BY))
    }
}

@ApiModel
data class AttributesTemplatesResponse (
        val templates: List<AttributeTemplate>
) : RepresentationModel<AttributesTemplatesResponse>() {
    init {
        add(linkTo(methodOn(AttributesController::class.java).getAllTemplates()).withSelfRel())
        add(linkTo(AttributesController::class.java).slash("validate").withRel("validation"))
    }
}

@ApiModel
data class FactsFromMetricsResponse (
        val patientsMetricsId: String,
        val jobId: String,
        val generatedFacts: List<SourceFact>
) : RepresentationModel<FactsFromMetricsResponse>()
