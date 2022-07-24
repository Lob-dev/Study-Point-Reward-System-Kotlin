package demo.point.edge.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfiguration {

    @Bean
    protected fun jacksonCustomizer() = Jackson2ObjectMapperBuilderCustomizer {
        it.featuresToDisable(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            DeserializationFeature.ACCEPT_FLOAT_AS_INT,
            SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
        )
    }

    @Bean
    protected fun jacksonMessageConvertor() = Jackson2JsonMessageConverter(ObjectMapper())
}