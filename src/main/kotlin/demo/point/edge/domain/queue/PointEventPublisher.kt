package demo.point.edge.domain.queue

import demo.point.edge.common.annotation.EventPublisher
import org.springframework.amqp.rabbit.core.RabbitTemplate

@EventPublisher
class PointEventPublisher(
    private val rabbitTemplate: RabbitTemplate
) {

    fun sendEvent(queue: String, payload: Any) =
        rabbitTemplate.convertAndSend(queue, payload)
}