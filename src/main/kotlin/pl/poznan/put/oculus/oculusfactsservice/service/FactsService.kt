package pl.poznan.put.oculus.oculusfactsservice.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import pl.poznan.put.oculus.boot.exception.OculusException
import pl.poznan.put.oculus.oculusfactsservice.model.GrfIrf
import pl.poznan.put.oculus.oculusfactsservice.model.fact.FactSource
import pl.poznan.put.oculus.oculusfactsservice.model.fact.FactSourceType
import pl.poznan.put.oculus.oculusfactsservice.model.fact.SourceFact
import pl.poznan.put.oculus.oculusfactsservice.repository.SourceFactsRepository

@Service
class FactsService (
        private val attributesService: AttributesService,
        private val sourceFactsRepository: SourceFactsRepository,
        private val restTemplate: RestTemplate,
        @Value("\${oculus.patients-db-service}")
        private val patientsServiceHost: String
) {
    fun generateAndSaveFactsFromMetrics(patientMetricsId: String, jobId: String): List<SourceFact> {
        val attributes = getAttributesFromMetrics(patientMetricsId)
        val facts = generateFactsFromAttributes(attributes, patientMetricsId, jobId)
        return sourceFactsRepository.insert(facts)
    }

    private fun getAttributesFromMetrics(patientMetricsId: String) = restTemplate.getForEntity(
            "$patientsServiceHost/patients/metrics",
            PatientsMetricsResponse::class.java,
            mapOf("id" to patientMetricsId)
    ).let {
        when(it.statusCode) {
            HttpStatus.OK -> it.body!!.attributes
            else -> throw object : OculusException("Error fetching metrics $patientMetricsId") {}
        }
    }

    private fun generateFactsFromAttributes(
            attributes: Map<String, String>, patientMetricsId: String, jobId: String
    ): List<SourceFact> {
        attributesService.validateAttributes(attributes)
                .map { it.second }
                .flatten()
                .let { if(it.isNotEmpty()) throw it.first() }

        return attributes.map { SourceFact(
                head = it.key,
                set = listOf(it.value),
                conjunction = false,
                grfIrf = GrfIrf(1.0, 1.0),
                job = jobId,
                source = FactSource(FactSourceType.METRICS, patientMetricsId)
        ) }
    }

    companion object {
        private data class PatientsMetricsResponse (
                val attributes: Map<String, String>
        )
    }

}
