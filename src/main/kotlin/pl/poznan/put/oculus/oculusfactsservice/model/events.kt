package pl.poznan.put.oculus.oculusfactsservice.model

import pl.poznan.put.oculus.oculusfactsservice.model.fact.FactSource

data class SourceFactEvent (
        val head: String,
        val set: List<String>,
        val conjunction: Boolean,
        val grfIrf: GrfIrf,
        val job: String,
        val source: FactSource,
        val last: Boolean
)

data class JobEvent (
        val type: JobEventType,
        val jobId: String
)

enum class JobEventType { IMAGE_INFERENCE_ENDED, INFERENCE_ENDED }
