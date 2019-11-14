package pl.poznan.put.oculus.oculusfactsservice.rest

import org.jetbrains.annotations.TestOnly
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.poznan.put.oculus.oculusfactsservice.rest.request.AttributeList
import pl.poznan.put.oculus.oculusfactsservice.rest.response.AttributeErrors
import pl.poznan.put.oculus.oculusfactsservice.rest.response.AttributeMessage
import pl.poznan.put.oculus.oculusfactsservice.rest.response.AttributesErrors
import pl.poznan.put.oculus.oculusfactsservice.service.AttributesService

@RestController
@RequestMapping("facts/attributes")
class AttributesController (
        val service: AttributesService
) {
    @TestOnly
    @PostMapping("validate")
    fun validateFactsCandidates(@RequestBody attributes: AttributeList) = ResponseEntity
            .ok(
                AttributesErrors(
                        service.validateAttributes(attributes.attributes)
                                .map {
                                    AttributeErrors(
                                        AttributeMessage(it.first.first, it.first.second),
                                        it.second
                                    )
                                }
                )
            )

}
