package pl.poznan.put.oculus.oculusfactsservice.repository

import org.springframework.data.mongodb.repository.MongoRepository
import pl.poznan.put.oculus.oculusfactsservice.model.fact.SourceFact

interface SourceFactsRepository : MongoRepository<SourceFact, String>
