package pl.poznan.put.oculus.oculusfactsservice.rest

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.poznan.put.oculus.boot.config.PublicAPI
import pl.poznan.put.oculus.boot.exception.OculusException
import pl.poznan.put.oculus.oculusfactsservice.rest.model.AttributeErrorsResponse
import pl.poznan.put.oculus.oculusfactsservice.rest.model.AttributeResponse
import pl.poznan.put.oculus.oculusfactsservice.rest.model.AttributesErrorsResponse
import pl.poznan.put.oculus.oculusfactsservice.rest.model.AttributesRequest
import pl.poznan.put.oculus.oculusfactsservice.rest.model.AttributesTemplatesResponse
import pl.poznan.put.oculus.oculusfactsservice.service.AttributesService

@PublicAPI
@RestController
@RequestMapping("facts/attributes")
class AttributesController (
        private val service: AttributesService
) {

    @PostMapping("validate")
    @ApiOperation(value = "Validate given attributes")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "validation done"),
        ApiResponse(code = 422, message = "validation error")
    ])
    fun validateAttributes(
            @RequestBody @ApiParam(value = "attributes to validate", required = true) attributes: AttributesRequest
    ): ResponseEntity<AttributesErrorsResponse> {
        val errors = service.validateAttributes(attributes.attributes)
        val invalid = errors.any { it.second.isNotEmpty() }
        val attributesResponses = errors.map {
            AttributeErrorsResponse(
                    AttributeResponse(it.first.first, it.first.second),
                    it.second.map(OculusException::errorMessage)
            )
        }
        val response = AttributesErrorsResponse(attributesResponses)
        return if (invalid) ResponseEntity.unprocessableEntity().body(response)
        else ResponseEntity.ok(response)
    }

    @GetMapping("templates")
    @ApiOperation(value = "Get list of all templates")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "List successfully retrieved"),
        ApiResponse(code = 204, message = "No attributes exist", response = Unit::class)
    ])
    fun getAllTemplates(): ResponseEntity<AttributesTemplatesResponse> {
        val templates = service.allAttributes()
        return if (templates.isNotEmpty()) ResponseEntity.ok(AttributesTemplatesResponse(templates))
        else ResponseEntity.noContent().build()
    }
}
