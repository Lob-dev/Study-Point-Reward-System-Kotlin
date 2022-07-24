package demo.point.edge.application

import demo.point.edge.common.BusinessException
import demo.point.edge.common.ErrorStatus
import demo.point.edge.common.UUIDKeyGenerator
import demo.point.edge.common.annotation.FacadeService
import demo.point.edge.domain.PointHistoryActivityService
import demo.point.edge.domain.point.PointHistoryService
import demo.point.edge.domain.point.active.PointActivityService
import demo.point.edge.domain.point.idempotent.PointProcessService
import demo.point.edge.domain.point.models.OperationType.PLUS
import demo.point.edge.domain.point.total.PointTotalService
import demo.point.edge.interfaces.handler.CancelPointEvent
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@FacadeService
class PointCancelService(
    private val pointHistoryService: PointHistoryService,
    private val pointActivityService: PointActivityService,
    private val pointHistoryActivityService: PointHistoryActivityService,
    private val pointProcessService: PointProcessService,
    private val pointTotalService: PointTotalService,
    private val redissonClient: RedissonClient,
) {

    @Transactional
    fun cancelPoints(cancelEvent: CancelPointEvent) {
        val userId = cancelEvent.userId
        val historyId = cancelEvent.historyId
        val actionType = cancelEvent.actionType
        if (pointProcessService.checkDuplicate(userId, historyId)) {
            throw BusinessException(
                ErrorStatus.DUPLICATE_REQUEST,
                "it was duplicate request. user id = $userId, action type = $actionType"
            )
        }

        val eventId = UUIDKeyGenerator.generate()
        val lockName = "POINT:$userId:LOCK"
        val lock: RLock = redissonClient.getLock(lockName)
        try {
            if (!lock.tryLock(1, 5, TimeUnit.SECONDS)) {
                throw BusinessException(ErrorStatus.LOCK_TIMEOUT, "it was tryLock timeout. user id = $userId")
            }

            val currentProcess = pointProcessService.createProcessBy(userId, historyId)
            try {
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
            } finally {
                pointProcessService.deleteProcessBy(currentProcess)
            }
        } finally {
            if (lock.isLocked) {
                lock.unlock()
            }
        }
    }
}