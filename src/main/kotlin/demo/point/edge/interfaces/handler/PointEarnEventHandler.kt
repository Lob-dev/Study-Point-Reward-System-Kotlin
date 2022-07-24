package demo.point.edge.interfaces.handler

import demo.point.edge.application.PointEarnService
import demo.point.edge.common.annotation.EventHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener

@EventHandler
class PointEarnEventHandler(
    private var pointEarnService: PointEarnService,
) {

    @RabbitListener(queues = ["point-earn-event-queue"])
    fun subscribe(earnEvent: EarnPointEvent) = pointEarnService.earnPoints(earnEvent)
}