package demo.point.edge.config

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MessageQueueConfiguration {

    @Bean
    protected fun pointCancelEventQueue() = Queue("point-cancel-event-queue", true)

    @Bean
    protected fun pointEarnEventQueue() = Queue("point-earn-event-queue", true)

    @Bean
    protected fun pointGiftEventQueue() = Queue("point-gift-event-queue", true)

    @Bean
    protected fun rabbitTemplate(
        connectionFactory: ConnectionFactory,
        jacksonMessageConvertor: Jackson2JsonMessageConverter
    ): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = jacksonMessageConvertor

        return rabbitTemplate
    }
}