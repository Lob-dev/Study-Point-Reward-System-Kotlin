package demo.point.edge.domain.point.total

import demo.point.edge.domain.point.models.OperationType
import demo.point.edge.domain.point.models.OperationType.*
import demo.point.edge.interfaces.api.CurrentPointRequest
import org.springframework.stereotype.Service

@Service
class PointTotalService(
    private val pointTotalRepository: PointTotalRepository
) {

    fun createTotalPointBy(userId: Long, initialPoint: Long) =
        pointTotalRepository.save(PointTotal(null, userId, initialPoint))

    fun findCurrentPointsBy(userId: Long): PointTotal? {
        return pointTotalRepository.findByUserId(userId)
    }
}