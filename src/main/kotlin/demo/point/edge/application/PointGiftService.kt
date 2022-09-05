package demo.point.edge.application

import demo.point.edge.common.annotation.FacadeService
import demo.point.edge.domain.PointHistoryActivityService
import demo.point.edge.domain.point.PointHistoryActivity
import demo.point.edge.domain.point.PointHistoryService
import demo.point.edge.domain.point.active.PointActivityService
import demo.point.edge.domain.point.total.PointTotal
import demo.point.edge.domain.point.total.PointTotalService
import demo.point.edge.interfaces.handler.PointPresentEvent
import org.springframework.transaction.annotation.Transactional

@FacadeService
class PointGiftService(
    private val pointHistoryService: PointHistoryService,
    private val pointActivityService: PointActivityService,
    private val pointHistoryActivityService: PointHistoryActivityService,
    private val pointTotalService: PointTotalService,
) {

    @Transactional
    fun deliveryPresentPoints(presentEvent: PointPresentEvent) {
        val newHistoryByRecipient = pointHistoryService.createHistoryBy(presentEvent.toRecipientHistory())
        val newActivityByRecipient = pointActivityService.createActivityBy(presentEvent.toActivity())

        val pointHistoriesByRecipient =
            listOf(PointHistoryActivity(historyId = newHistoryByRecipient.id, activityId = newActivityByRecipient.id))
        pointHistoryActivityService.createHistoryActivityBy(pointHistoriesByRecipient)
        pointHistoryService.createEffectHistoryBy(newHistoryByRecipient.id, presentEvent.toSenderHistory())

        val recipientId = presentEvent.recipientId
        pointTotalService.findCurrentPointsBy(recipientId).let {
            it?.updatePointsBy(presentEvent.initialPoint) ?: {
                val currentPointsBy = pointActivityService.findAllCurrentPointsBy(recipientId)
                val pointTotal = PointTotal(null, recipientId, currentPointsBy.sumOf { point -> point.currentPoint })
                pointTotalService.createTotalPointBy(pointTotal)
            }
        }
        // TODO: 2022-07-25 May be instead Notification Service
    }
}
