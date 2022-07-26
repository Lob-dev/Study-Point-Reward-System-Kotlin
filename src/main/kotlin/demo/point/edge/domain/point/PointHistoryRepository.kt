package demo.point.edge.domain.point

import com.querydsl.core.types.Predicate
import com.querydsl.jpa.impl.JPAQueryFactory
import demo.point.edge.domain.point.models.ActionType
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime

interface PointHistoryRepository : JpaRepository<PointHistory, Long?> {
    fun findByUserIdAndCreateAtBetweenAndActionType(
        userId: Long,
        start: ZonedDateTime,
        end: ZonedDateTime,
        actionType: ActionType
    ): List<PointHistory>
}

interface PointHistoryQueryRepository {
    fun getPage(pageable: Pageable, predicate: Predicate): Page<PointHistory>
}

@Repository
class DefaultPointHistoryQueryRepository(
    private val jpaQueryFactory: JPAQueryFactory
) : PointHistoryQueryRepository {

    override fun getPage(pageable: Pageable, predicate: Predicate): Page<PointHistory> {
        val fetch: List<PointHistory> = jpaQueryFactory.selectFrom(pointHistory)
            .where(predicate)
            .orderBy(pointHistory.updateAt.desc())
            .limit(pageable.pageSize.toLong())
            .offset(pageable.offset)
            .fetch()
        return PageImpl(fetch, pageable, fetch.size.toLong())
    }

    companion object {
        val pointHistory: QPointHistory = QPointHistory.pointHistory
    }
}