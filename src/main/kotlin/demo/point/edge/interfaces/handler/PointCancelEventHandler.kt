package demo.point.edge.interfaces.handler

import demo.point.edge.application.PointCancelService
import demo.point.edge.common.annotation.EventHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener

@EventHandler
class PointCancelEventHandler(
    private var pointCancelService: PointCancelService
) {

    @RabbitListener(queues = ["point-cancel-event-queue"])
    fun subscribe(cancelEvent: CancelPointEvent) = pointCancelService.cancelPoints(cancelEvent)
}