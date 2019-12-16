package pl.poznan.put.oculus.oculusfactsservice.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import pl.poznan.put.oculus.boot.exception.OculusException
import pl.poznan.put.oculus.oculusfactsservice.model.GrfIrf
import pl.poznan.put.oculus.oculusfactsservice.model.JobEvent
import pl.poznan.put.oculus.oculusfactsservice.model.JobEventType
import pl.poznan.put.oculus.oculusfactsservice.model.ResultFactEvent
import pl.poznan.put.oculus.oculusfactsservice.model.SourceFactEvent
import pl.poznan.put.oculus.oculusfactsservice.model.fact.FactSource
import pl.poznan.put.oculus.oculusfactsservice.model.fact.FactSourceType
import pl.poznan.put.oculus.oculusfactsservice.model.fact.ResultFact
import pl.poznan.put.oculus.oculusfactsservice.model.fact.SourceFact
import pl.poznan.put.oculus.oculusfactsservice.repository.ResultFactsRepository
import pl.poznan.put.oculus.oculusfactsservice.repository.SourceFactsRepository

@Service
class FactsService (
        private val attributesService: AttributesService,
        private val sourceFactsRepository: SourceFactsRepository,
        private val resultFactsRepository: ResultFactsRepository,
        private val restTemplate: RestTemplate,
        private val jobsKafkaTemplate: KafkaTemplate<String, JobEvent>,
        @Value("\${oculus.patients-db-service}")
        private val patientsServiceHost: String
) {
    fun generateAndSaveFactsFromMetrics(patientMetricsId: String, jobId: String): List<SourceFact> {
        val attributes = getAttributesFromMetrics(patientMetricsId)
        val facts = generateAndSaveFactsFromAttributes(attributes, patientMetricsId, jobId)
        return sourceFactsRepository.insert(facts).also {
            logger.info("generated facts from metrics for job $jobId")
        }
    }

    private fun getAttributesFromMetrics(patientMetricsId: String) = restTemplate.getForEntity(
            "http://$patientsServiceHost/patients/metrics?id=$patientMetricsId",
            PatientsMetricsResponse::class.java
    ).let {
        when(it.statusCode) {
            HttpStatus.OK -> it.body!!.attributes
            else -> throw MetricsFetchingException(patientMetricsId)
        }
    }

    private fun generateAndSaveFactsFromAttributes(
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

    @KafkaListener(topics = ["sourceFacts"], groupId = "1", containerFactory = "kafkaListenerContainerFactory")
    fun receiveSourceFacts(event: SourceFactEvent) {
        sourceFactsRepository.insert(SourceFact(event.head, event.set, event.conjunction, event.grfIrf, event.job, event.source))
        if (event.last) processLastSourceFactEvent(event.source.type, event.job)
    }

    private fun processLastSourceFactEvent(sourceType: FactSourceType, jobId: String) {
        when(sourceType) {
            FactSourceType.METRICS -> Unit // won't receive
            FactSourceType.IMAGE -> {
                jobsKafkaTemplate.send("jobs", JobEvent(JobEventType.IMAGE_INFERENCE_ENDED, jobId))
                logger.info("sent IMAGE_INFERENCE_ENDED event for job $jobId")
            }
        }
    }

    @KafkaListener(topics = ["resultFacts"], groupId = "1", containerFactory = "resultFactListenerContainerFactory")
    fun receiveResultFacts(event: ResultFactEvent) {
        event.facts.asSequence()
                .map { ResultFact(it.head, it.set, it.conjunction, it.grfIrf, event.job) }
                .toList()
                .let { resultFactsRepository.insert(it) }

        if (event.last) processLastResultFactEvent( event.job)
    }

    private fun processLastResultFactEvent(jobId: String) {
        jobsKafkaTemplate.send("jobs", JobEvent(JobEventType.INFERENCE_ENDED, jobId))
        logger.info("sent INFERENCE_ENDED event for job $jobId")
    }

    companion object {
        private data class PatientsMetricsResponse (
                val attributes: Map<String, String>
        )

        private class MetricsFetchingException(id: String) : OculusException("Error fetching metrics $id")

        private val logger = LoggerFactory.getLogger(FactsService::class.java)
    }

}
