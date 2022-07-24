package demo.point.edge.application

import demo.point.edge.common.UUIDKeyGenerator
import demo.point.edge.common.annotation.DistributedLock
import demo.point.edge.common.annotation.FacadeService
import demo.point.edge.domain.PointHistoryActivityService
import demo.point.edge.domain.point.PointHistoryService
import demo.point.edge.domain.point.active.PointActivityService
import demo.point.edge.domain.point.models.OperationType.PLUS
import demo.point.edge.domain.point.total.PointTotalService
import demo.point.edge.interfaces.handler.CancelPointEvent
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
    fun cancelPoints(cancelEvent: CancelPointEvent) {
        val eventId = UUIDKeyGenerator.generate()
        val userId = cancelEvent.userId
        val historyId = cancelEvent.historyId

        val targetHistory = pointHistoryService.findBy(historyId)
        val activityIdsByHistory = pointHistoryActivityService.findAllBy(targetHistory.id)
            .map { it.activityId as Long }
            .toList()

        var restorePoint = targetHistory.point
        pointActivityService.findAllByIds(activityIdsByHistory).forEach {
            restorePoint = it.restorePoint(restorePoint)
        }

        pointHistoryService.createHistoryBy(cancelEvent.toHistory(eventId))
        val currentPoints = pointTotalService.findCurrentPointsBy(userId)
        when {
            currentPoints != null -> currentPoints.updatePointsBy(restorePoint, PLUS)
            else -> {
                val currentPointsBy = pointActivityService.getCurrentPointsBy(userId)
                pointTotalService.createTotalPointBy(userId, currentPointsBy.sumOf { it.currentPoint })
            }
        }
    }
}