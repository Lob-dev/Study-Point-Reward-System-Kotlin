package demo.point.edge.domain.point

import org.springframework.data.jpa.repository.JpaRepository

interface PointHistoryActivityRepository: JpaRepository<PointHistoryActivity, Long?> {
    fun findAllByHistoryId(historyId: Long?): List<PointHistoryActivity>
}