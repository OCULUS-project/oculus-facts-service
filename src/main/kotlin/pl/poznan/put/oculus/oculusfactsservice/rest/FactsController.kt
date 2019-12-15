package pl.poznan.put.oculus.oculusfactsservice.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.poznan.put.oculus.boot.config.PublicAPI
import pl.poznan.put.oculus.oculusfactsservice.rest.model.FactsFromImagesRequest
import pl.poznan.put.oculus.oculusfactsservice.rest.model.FactsFromMetricsRequest
import pl.poznan.put.oculus.oculusfactsservice.rest.model.FactsFromMetricsResponse
import pl.poznan.put.oculus.oculusfactsservice.service.FactsService

@RestController
@RequestMapping("/facts")
@PublicAPI
class FactsController (
        private val service: FactsService
) {

    @PostMapping("/fromMetrics")
    fun generateFactsFromMetrics(
            @RequestBody request: FactsFromMetricsRequest
    ): ResponseEntity<FactsFromMetricsResponse> {
        val facts = service.generateAndSaveFactsFromMetrics(request.patientMetricsId, request.jobId)
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(FactsFromMetricsResponse(request.patientMetricsId, request.jobId, facts))
    }

    @PostMapping("/fromImages")
    fun generateFactsFromImage(
            @RequestBody request: FactsFromImagesRequest
    ) = "TODO"

}
