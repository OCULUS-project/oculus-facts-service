package pl.poznan.put.oculus.oculusfactsservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("pl.poznan.put.oculus.boot", "")
class OculusFactsServiceApplication

fun main(args: Array<String>) {
    runApplication<OculusFactsServiceApplication>(*args)
}
