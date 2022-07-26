package demo.point.edge.domain.point

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Predicate
import demo.point.edge.interfaces.api.PointHistoriesFindRequest

class PointHistoryQueryBuilder {
    companion object {
        private val pointHistory: QPointHistory = QPointHistory.pointHistory

        fun build(request: PointHistoriesFindRequest): Predicate =
            BooleanBuilder().also {
                it.and(pointHistory.userId.eq(request.userId))
            }
    }
}