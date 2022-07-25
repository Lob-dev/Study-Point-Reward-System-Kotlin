package demo.point.edge.application

import demo.point.edge.common.annotation.DistributedLock
import demo.point.edge.common.annotation.FacadeService
import demo.point.edge.domain.PointHistoryActivityService
import demo.point.edge.domain.point.PointHistoryService
import demo.point.edge.domain.point.active.PointActivityService
import demo.point.edge.domain.point.total.PointTotalService
import demo.point.edge.interfaces.handler.PointCancelEvent
import org.springframework.transaction.annotation.Transactional

@FacadeService
class PointCancelService(
    private val pointHistoryService: PointHistoryService,
    private val pointActivityService: PointActivityService,
    private val pointHistoryActivityService: PointHistoryActivityService,
    private val pointTotalService: PointTotalService,
) {

    @DistributedLock(prefix = "POINT:", key = "#cancelEvent.userId")
    @Transactional
    fun cancelPoints(cancelEvent: PointCancelEvent) {
        val historyId = cancelEvent.historyId

        val targetHistory = pointHistoryService.findBy(historyId)
        val activityIdsByHistory = pointHistoryActivityService.findAllBy(targetHistory.id)
            .map { it.activityId as Long }
            .toList()

        var restorePoint = targetHistory.point
        pointActivityService.findAllByIds(activityIdsByHistory).forEach {
            restorePoint = it.restorePoint(restorePoint)
        }
        pointHistoryService.createHistoryBy(cancelEvent.toHistory())

        val userId = cancelEvent.userId
        pointTotalService.findCurrentPointsBy(userId).let {
            it?.updatePointsBy(restorePoint) ?: {
                val currentPointsBy = pointActivityService.findAllCurrentPointsBy(userId)
                pointTotalService.createTotalPointBy(userId, currentPointsBy.sumOf { point -> point.currentPoint })
            }
        }
    }
}