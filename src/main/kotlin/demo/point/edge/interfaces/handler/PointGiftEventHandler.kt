package demo.point.edge.interfaces.handler

import demo.point.edge.application.PointGiftService
import demo.point.edge.common.annotation.EventHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener

@EventHandler
class PointGiftEventHandler(
    private var pointGiftService: PointGiftService
) {

    @RabbitListener(queues = ["point-gift-event-queue"])
    fun subscribe(pointPresentEvent: PointPresentEvent) = pointGiftService.deliveryPresentPoints(pointPresentEvent)
}
