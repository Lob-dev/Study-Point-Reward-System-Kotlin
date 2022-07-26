package demo.point.edge.domain.point.active

import org.springframework.stereotype.Service

@Service
class PointActivityService(
    private val pointActivityRepository: PointActivityRepository,
) {

    fun findAllByIds(ids: List<Long>): MutableList<PointActivity> =
        pointActivityRepository.findAllById(ids)

    fun findAllCurrentPointsBy(userId: Long): List<PointActivity> =
        pointActivityRepository.findAllByUserIdAndCurrentPointGreaterThan(userId, 0)

    fun createActivityBy(pointActivity: PointActivity) =
        pointActivityRepository.save(pointActivity)

    fun updateCurrentPointsBy(pointActivities: List<PointActivity>): MutableList<PointActivity> =
        pointActivityRepository.saveAll(pointActivities)
}