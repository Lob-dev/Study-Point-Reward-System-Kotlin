package demo.point.edge.domain.point.active

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PointActivityService(
    private val pointActivityRepository: PointActivityRepository,
) {

    @Transactional(readOnly = true)
    fun findAllByIds(ids: List<Long>): MutableList<PointActivity> =
        pointActivityRepository.findAllById(ids)

    @Transactional(readOnly = true)
    fun findAllCurrentPointsBy(userId: Long): List<PointActivity> =
        pointActivityRepository.findAllByUserId(userId, 0)

    @Transactional
    fun createActivityBy(pointActivity: PointActivity): PointActivity =
        pointActivityRepository.save(pointActivity)

    @Transactional
    fun updateCurrentPointsBy(pointActivities: List<PointActivity>) =
        pointActivityRepository.batchUpdateCurrentPoint(pointActivities)
}