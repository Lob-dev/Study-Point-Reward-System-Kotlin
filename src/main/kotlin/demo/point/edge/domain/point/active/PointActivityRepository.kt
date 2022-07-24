package demo.point.edge.domain.point.active

import org.springframework.data.jpa.repository.JpaRepository

interface PointActivityRepository: JpaRepository<PointActivity, Long?> {
    fun findAllByUserIdAndCurrentPointGreaterThan(userId: Long, currentPoint: Long): List<PointActivity>
}