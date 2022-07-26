package demo.point.edge.domain.point.total

import org.springframework.stereotype.Service

@Service
class PointTotalService(
    private val pointTotalRepository: PointTotalRepository,
) {

    fun findCurrentPointsBy(userId: Long): PointTotal? {
        return pointTotalRepository.findByUserId(userId)
    }

    fun createTotalPointBy(pointTotal: PointTotal) =
        pointTotalRepository.save(pointTotal)
}