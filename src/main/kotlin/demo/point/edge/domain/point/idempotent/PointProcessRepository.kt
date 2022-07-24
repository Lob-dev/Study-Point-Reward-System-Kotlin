package demo.point.edge.domain.point.idempotent

import org.springframework.data.repository.CrudRepository

interface PointProcessRepository: CrudRepository<PointProcess, Long?> {
    fun findByUserIdAndHistoryId(userId: Long, historyId: Long)
}