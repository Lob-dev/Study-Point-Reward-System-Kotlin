package demo.point.edge.domain.point

import demo.point.edge.domain.point.models.ActionType
import org.springframework.data.jpa.repository.JpaRepository
import java.time.ZonedDateTime

interface PointHistoryRepository : JpaRepository<PointHistory, Long?> {
    fun findByUserIdAndCreateAtBetweenAndActionType(
        userId: Long,
        start: ZonedDateTime,
        end: ZonedDateTime,
        actionType: ActionType
    ): List<PointHistory>

    fun findByUserId(userId: Long): List<PointHistory>
}