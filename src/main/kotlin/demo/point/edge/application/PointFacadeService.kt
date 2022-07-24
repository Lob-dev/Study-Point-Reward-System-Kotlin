package demo.point.edge.application

import demo.point.edge.common.BusinessException
import demo.point.edge.common.ErrorStatus
import demo.point.edge.common.UUIDKeyGenerator
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
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@FacadeService
class PointFacadeService(
    private val pointEventPublisher: PointEventPublisher,
    private val pointHistoryService: PointHistoryService,
    private val pointActivityService: PointActivityService,
    private val pointHistoryActivityService: PointHistoryActivityService,
    private val pointTotalService: PointTotalService,
    private val redissonClient: RedissonClient,
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

    @Transactional
    fun usePoints(usePointRequest: UsePointRequest) {
        val eventId = UUIDKeyGenerator.generate()
        val userId = usePointRequest.userId
        val actionType = usePointRequest.actionType

        val usePoint = usePointRequest.usePoint
        val lockName = "POINT:$userId:LOCK"
        val lock: RLock = redissonClient.getLock(lockName)
        if (lock.isLocked) {
            throw BusinessException(
                ErrorStatus.DUPLICATE_REQUEST,
                "it was duplicate request. user id = $userId, action type = $actionType"
            )
        }

        try {
            if (!lock.tryLock(1, 5, TimeUnit.SECONDS)) {
                throw BusinessException(ErrorStatus.LOCK_TIMEOUT, "it was tryLock timeout. user id = $userId")
            }

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
        } finally {
            if (lock.isLocked) {
                lock.unlock()
            }
        }
    }

    fun cancelPoints(cancelPointRequest: CancelPointRequest) =
        pointEventPublisher.sendEvent(
            "point-cancel-event-queue",
            cancelPointRequest.toEvent(UUIDKeyGenerator.generate())
        )
}