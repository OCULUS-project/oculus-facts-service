package pl.poznan.put.oculus.oculusfactsservice.model

import io.swagger.annotations.ApiModel

@ApiModel(description = "GrfIrf of the fact or rule")
data class GrfIrf(
        val grf: Double,
        val irf: Double
)
