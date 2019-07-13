package pl.poznan.put.oculus.oculusfactsservice.repository

import org.springframework.data.mongodb.repository.MongoRepository
import pl.poznan.put.oculus.oculusfactsservice.model.Attribute

interface AttributesRepository : MongoRepository<Attribute, String>
