package demo.point.edge.application

import demo.point.edge.common.BusinessException
import demo.point.edge.common.ErrorStatus
import demo.point.edge.common.UUIDKeyGenerator
import demo.point.edge.common.lock.DistributedLock
import demo.point.edge.common.annotation.FacadeService
import demo.point.edge.domain.PointHistoryActivityService
import demo.point.edge.domain.point.PointHistoryActivity
import demo.point.edge.domain.point.PointHistoryService
import demo.point.edge.domain.point.active.PointActivityService
import demo.point.edge.domain.point.models.OperationType.MINUS
import demo.point.edge.domain.point.total.PointTotal
import demo.point.edge.domain.point.total.PointTotalService
import demo.point.edge.domain.queue.PointEventPublisher
import demo.point.edge.interfaces.api.PointCancelRequest
import demo.point.edge.interfaces.api.PointEarnRequest
import demo.point.edge.interfaces.api.PointPresentRequest
import demo.point.edge.interfaces.api.PointUseRequest
import org.springframework.transaction.annotation.Transactional

@FacadeService
class PointFacadeService(
    private val pointEventPublisher: PointEventPublisher,
    private val pointHistoryService: PointHistoryService,
    private val pointActivityService: PointActivityService,
    private val pointHistoryActivityService: PointHistoryActivityService,
    private val pointTotalService: PointTotalService,
) {

    fun giftPoints(presentRequest: PointPresentRequest) =
        pointEventPublisher.sendEvent(
            "point-gift-event-queue",
            presentRequest.toEvent(UUIDKeyGenerator.generate())
        )

    fun earnPoints(earnRequest: PointEarnRequest) =
        pointEventPublisher.sendEvent(
            "point-earn-event-queue",
            earnRequest.toEvent(UUIDKeyGenerator.generate())
        )

    @DistributedLock(prefix = "POINT:", key = "#cancelEvent.userId")
    @Transactional
    fun usePoints(useRequest: PointUseRequest) {
        val eventId = UUIDKeyGenerator.generate()
        val userId = useRequest.userId
        val usePoint = useRequest.usePoint

        var bucketByPoint = useRequest.usePoint
        val currentPointsByUser = pointActivityService.findAllCurrentPointsBy(userId)
        currentPointsByUser.forEach {
            bucketByPoint = it.redeemPoint(bucketByPoint)
        }

        if (bucketByPoint > 0L) {
            throw BusinessException(
                ErrorStatus.POINT_OVERFLOW,
                "it was point overflow. user id = $userId, remain point = $bucketByPoint"
            )
        }
        pointActivityService.updateCurrentPointsBy(currentPointsByUser)

        val newHistoryId = pointHistoryService.createHistoryBy(useRequest.toHistory(eventId)).id
        val historyActivities = currentPointsByUser
            .map { PointHistoryActivity(null, newHistoryId, it.id) }
            .toList()
        pointHistoryActivityService.createHistoryActivityBy(historyActivities)

        pointTotalService.findCurrentPointsBy(userId).let {
            it?.updatePointsBy(usePoint, MINUS) ?: {
                val currentPointsBy = pointActivityService.findAllCurrentPointsBy(userId)
                val pointTotal = PointTotal(null, userId, currentPointsBy.sumOf { point -> point.currentPoint })
                pointTotalService.createTotalPointBy(pointTotal)
            }
        }
        // TODO: 2022-07-25 May be instead Notification Service
    }

    fun cancelPoints(cancelRequest: PointCancelRequest) =
        pointEventPublisher.sendEvent(
            "point-cancel-event-queue",
            cancelRequest.toEvent(UUIDKeyGenerator.generate())
        )
}