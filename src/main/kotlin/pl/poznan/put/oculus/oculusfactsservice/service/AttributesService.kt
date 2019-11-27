package pl.poznan.put.oculus.oculusfactsservice.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import pl.poznan.put.oculus.oculusfactsservice.model.AttributeTemplate
import pl.poznan.put.oculus.oculusfactsservice.repository.AttributesRepository
import pl.poznan.put.oculus.oculusfactsservice.validator.AttributesValidator

@Service
class AttributesService (
        val repository: AttributesRepository
) {
    fun validateAttributes(attributes: Map<String, String>) =
            AttributesValidator(allAttributes()).validate(attributes)

    // TODO: @Cached
    fun allAttributes(): List<AttributeTemplate> = repository.findAll()
            .also { logger.info("fetching all attributes, found ${it.size}") }

    companion object {
        private val logger = LoggerFactory.getLogger(AttributesService::class.java)
    }
}
