package pl.poznan.put.oculus.oculusfactsservice.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import pl.poznan.put.oculus.boot.exception.ErrorMessage
import pl.poznan.put.oculus.oculusfactsservice.repository.AttributesRepository
import pl.poznan.put.oculus.oculusfactsservice.validator.AttributesValidator

@Service
class AttributesService (
        val repository: AttributesRepository
) {
    fun validateAttributes(attributes: Map<String, String>): List<Pair<Pair<String, String>, List<ErrorMessage>>> {
        val templates = allAttributes()
        logger.debug("COOL")
        return AttributesValidator(templates).validate(attributes)
    }

    // TODO: @Cached
    private fun allAttributes() = repository.findAll()

    companion object {
        private val logger = LoggerFactory.getLogger(AttributesService::class.java)
    }
}