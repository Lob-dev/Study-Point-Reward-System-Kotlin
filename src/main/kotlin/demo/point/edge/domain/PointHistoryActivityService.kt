package demo.point.edge.domain

import demo.point.edge.domain.point.PointHistoryActivity
import demo.point.edge.domain.point.PointHistoryActivityRepository
import org.springframework.stereotype.Service

@Service
class PointHistoryActivityService(
    private val pointHistoryActivityRepository: PointHistoryActivityRepository,
) {

    fun findAllBy(historyId: Long?): List<PointHistoryActivity> =
        pointHistoryActivityRepository.findAllByHistoryId(historyId)

    fun createHistoryActivityBy(historyActivity: List<PointHistoryActivity>): MutableList<PointHistoryActivity> =
        pointHistoryActivityRepository.saveAll(historyActivity)
}