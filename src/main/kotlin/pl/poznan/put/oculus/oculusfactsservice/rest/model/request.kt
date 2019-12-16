package pl.poznan.put.oculus.oculusfactsservice.rest.model

import io.swagger.annotations.ApiModel

@ApiModel
data class AttributesRequest (
        val attributes: Map<String, String>
)

@ApiModel
data class FactsFromMetricsRequest (
        val patientMetricsId: String,
        val jobId: String
)

@ApiModel
data class FactsFromImagesRequest (
        val imageFileId: String,
        val jobId: String
)
