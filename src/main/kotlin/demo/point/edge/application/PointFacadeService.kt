package demo.point.edge.application

import demo.point.edge.common.BusinessException
import demo.point.edge.common.ErrorStatus
import demo.point.edge.common.UUIDKeyGenerator
import demo.point.edge.common.annotation.DistributedLock
import demo.point.edge.common.annotation.FacadeService
import demo.point.edge.domain.PointHistoryActivityService
import demo.point.edge.domain.point.PointHistoryActivity
import demo.point.edge.domain.point.PointHistoryService
import demo.point.edge.domain.point.active.PointActivityService
import demo.point.edge.domain.point.models.OperationType
import demo.point.edge.domain.point.total.PointTotalService
import demo.point.edge.domain.queue.PointEventPublisher
import demo.point.edge.interfaces.api.CancelPointRequest
import demo.point.edge.interfaces.api.EarnPointRequest
import demo.point.edge.interfaces.api.GiftPointRequest
import demo.point.edge.interfaces.api.UsePointRequest
import org.springframework.transaction.annotation.Transactional

@FacadeService
class PointFacadeService(
    private val pointEventPublisher: PointEventPublisher,
    private val pointHistoryService: PointHistoryService,
    private val pointActivityService: PointActivityService,
    private val pointHistoryActivityService: PointHistoryActivityService,
    private val pointTotalService: PointTotalService,
) {

    fun giftPoints(giftPointRequest: GiftPointRequest) =
        pointEventPublisher.sendEvent(
            "point-gift-event-queue",
            giftPointRequest.toEvent(UUIDKeyGenerator.generate())
        )

    fun earnPoints(earnPointRequest: EarnPointRequest) =
        pointEventPublisher.sendEvent(
            "point-earn-event-queue",
            earnPointRequest.toEvent(UUIDKeyGenerator.generate())
        )

    @DistributedLock(prefix = "POINT:", key = "#cancelEvent.userId")
    @Transactional
    fun usePoints(usePointRequest: UsePointRequest) {
        val eventId = UUIDKeyGenerator.generate()
        val userId = usePointRequest.userId
        val usePoint = usePointRequest.usePoint

        var bucketByPoint = usePointRequest.usePoint
        val currentPointsByUser = pointActivityService.getCurrentPointsBy(userId)
        currentPointsByUser.forEach {
            bucketByPoint = it.redeemPoint(bucketByPoint)
        }

        if (usePoint > 0L) {
            throw BusinessException(
                ErrorStatus.POINT_OVERFLOW,
                "it was point overflow. user id = $userId, remain point = $bucketByPoint"
            )
        }
        val updatedCurrentPointsByUser = pointActivityService.updateCurrentPointsBy(currentPointsByUser)

        val newHistoryId = pointHistoryService.createHistoryBy(usePointRequest.toHistory(eventId)).id
        val historyActivities = updatedCurrentPointsByUser
            .map { PointHistoryActivity(null, newHistoryId, it.id) }
            .toList()
        pointHistoryActivityService.createHistoryActivityBy(historyActivities)

        val currentPoints = pointTotalService.findCurrentPointsBy(userId)
        when {
            currentPoints != null -> currentPoints.updatePointsBy(usePoint, OperationType.MINUS)
            else -> {
                val currentPointsBy = pointActivityService.getCurrentPointsBy(userId)
                pointTotalService.createTotalPointBy(userId, currentPointsBy.sumOf { it.currentPoint })
            }
        }
    }

    fun cancelPoints(cancelPointRequest: CancelPointRequest) =
        pointEventPublisher.sendEvent(
            "point-cancel-event-queue",
            cancelPointRequest.toEvent(UUIDKeyGenerator.generate())
        )
}