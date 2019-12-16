package pl.poznan.put.oculus.oculusfactsservice.config

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import pl.poznan.put.oculus.oculusfactsservice.model.JobEvent
import pl.poznan.put.oculus.oculusfactsservice.model.ResultFactEvent
import pl.poznan.put.oculus.oculusfactsservice.model.SourceFactEvent


@EnableKafka
@Configuration
class KafkaTopicConfig (
        @Value("\${kafka.bootstrapAddress}")
        private val bootstrapAddress: String
) {
    @Bean
    fun kafkaAdmin(): KafkaAdmin = KafkaAdmin(
            mapOf(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress)
    )

    @Bean
    fun sourceFactsTopic(): NewTopic = NewTopic("sourceFacts", 1, 1.toShort())

    @Bean
    fun resultFactsTopic(): NewTopic = NewTopic("resultFacts", 1, 1.toShort())

    private val consumerConfigs = mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress,
            ConsumerConfig.GROUP_ID_CONFIG to "1",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java,
            JsonDeserializer.TRUSTED_PACKAGES to "*"
    )

    @Bean
    fun sourceFactConsumerFactory(): ConsumerFactory<String, SourceFactEvent> = DefaultKafkaConsumerFactory(
            consumerConfigs.plus(JsonDeserializer.VALUE_DEFAULT_TYPE to SourceFactEvent::class.java)
    )

    @Bean
    fun resultFactConsumerFactory(): ConsumerFactory<String, ResultFactEvent> = DefaultKafkaConsumerFactory(
            consumerConfigs.plus(JsonDeserializer.VALUE_DEFAULT_TYPE to ResultFactEvent::class.java)
    )

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, SourceFactEvent> =
            ConcurrentKafkaListenerContainerFactory<String, SourceFactEvent>()
                    .apply { consumerFactory = sourceFactConsumerFactory() }

    @Bean
    fun resultFactListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, ResultFactEvent> =
            ConcurrentKafkaListenerContainerFactory<String, ResultFactEvent>()
                    .apply { consumerFactory = resultFactConsumerFactory() }


    private fun <A, B> producerFactory() =  DefaultKafkaProducerFactory<A, B>(
            mapOf(
                    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress,
                    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java,
                    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java,
                    JsonSerializer.ADD_TYPE_INFO_HEADERS to false
            )
    )

    @Bean
    fun sourceFactsProducer(): ProducerFactory<String, SourceFactEvent> = producerFactory()

    @Bean
    fun jobEventProducer(): ProducerFactory<String, JobEvent> = producerFactory()

    @Bean
    fun kafkaTemplateSourceFacts(): KafkaTemplate<String, SourceFactEvent> = KafkaTemplate(sourceFactsProducer())

    @Bean
    fun kafkaTemplateJobs(): KafkaTemplate<String, JobEvent> = KafkaTemplate(jobEventProducer())
}
