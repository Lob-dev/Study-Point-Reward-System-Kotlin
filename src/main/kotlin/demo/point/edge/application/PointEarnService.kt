package demo.point.edge.application

import demo.point.edge.common.annotation.FacadeService
import demo.point.edge.domain.PointHistoryActivityService
import demo.point.edge.domain.point.PointHistoryActivity
import demo.point.edge.domain.point.PointHistoryService
import demo.point.edge.domain.point.active.PointActivityService
import demo.point.edge.domain.point.total.PointTotal
import demo.point.edge.domain.point.total.PointTotalService
import demo.point.edge.interfaces.handler.PointEarnEvent
import org.springframework.transaction.annotation.Transactional

@FacadeService
class PointEarnService(
    private val pointHistoryService: PointHistoryService,
    private val pointActivityService: PointActivityService,
    private val pointHistoryActivityService: PointHistoryActivityService,
    private val pointTotalService: PointTotalService,
) {

    @Transactional
    fun earnPoints(earnEvent: PointEarnEvent) {
        val newHistoryByUser = pointHistoryService.createHistoryBy(earnEvent.toHistory())
        val newActivityByUser = pointActivityService.createActivityBy(earnEvent.toActivity())

        val pointHistoriesByUser =
            listOf(PointHistoryActivity(historyId = newHistoryByUser.id, activityId = newActivityByUser.id))
        pointHistoryActivityService.createHistoryActivityBy(pointHistoriesByUser)

        val userId = earnEvent.userId
        pointTotalService.findCurrentPointsBy(userId).let {
            it?.updatePointsBy(earnEvent.initialPoint) ?: {
                val currentPointsBy = pointActivityService.findAllCurrentPointsBy(userId)
                val pointTotal = PointTotal(null, userId, currentPointsBy.sumOf { point -> point.currentPoint })
                pointTotalService.createTotalPointBy(pointTotal)
            }
        }
        // TODO: 2022-07-25 May be instead Notification Service
    }
}
