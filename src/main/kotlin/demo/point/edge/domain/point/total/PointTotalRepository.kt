package demo.point.edge.domain.point.total

import org.springframework.data.repository.CrudRepository

interface PointTotalRepository: CrudRepository<PointTotal, Long> {
    fun findByUserId(userId: Long?): PointTotal?
}