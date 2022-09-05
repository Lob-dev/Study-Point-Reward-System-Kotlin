package demo.point.edge.domain.point

import demo.point.edge.common.BusinessException
import demo.point.edge.common.ErrorStatus
import demo.point.edge.domain.point.models.ActionType
import demo.point.edge.domain.point.models.EventType
import jooq.dsl.tables.JPointHistory
import jooq.dsl.tables.records.JPointHistoryRecord
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime

interface PointHistoryRepository {
    fun findAllByUserIdAndActionTypeWithRange(
            userId: Long,
            start: ZonedDateTime,
            end: ZonedDateTime,
            actionType: ActionType
    ): List<PointHistory>

    fun findAll(pageable: Pageable, condition: Condition): Page<PointHistory>
    fun findById(id: Long): PointHistory
    fun save(entity: PointHistory): PointHistory
    fun createHistoryBy(newHistory: PointHistory): PointHistory
    fun createEffectHistoryBy(effectHistoryId: Long, newHistory: PointHistory): PointHistory
}

@Repository
class PointHistoryRepositoryImpl(
        private val query: DSLContext,
) : PointHistoryRepository {

    override fun findAllByUserIdAndActionTypeWithRange(
            userId: Long,
            start: ZonedDateTime,
            end: ZonedDateTime,
            actionType: ActionType
    ): List<PointHistory> =
            query.select(
                    POINT_HISTORY.ID,
                    POINT_HISTORY.EVENT_ID,
                    POINT_HISTORY.USER_ID,
                    POINT_HISTORY.ASSOCIATE_HISTORY_ID,
                    POINT_HISTORY.EVENT_TYPE,
                    POINT_HISTORY.ACTION_TYPE,
                    POINT_HISTORY.POINT,
            )
                    .from(POINT_HISTORY)
                    .where(POINT_HISTORY.USER_ID.eq(userId)
                            .and(POINT_HISTORY.CREATE_AT.between(start.toLocalDateTime())
                                    .and(end.toLocalDateTime())
                            )
                            .and(POINT_HISTORY.ACTION_TYPE.eq(actionType.toString())))
                    .stream()
                    .map { mapping(it) }
                    .toList()

    override fun findAll(pageable: Pageable, condition: Condition): Page<PointHistory> {
        val results = query
                .selectFrom(POINT_HISTORY)
                .where(condition)
                .orderBy(POINT_HISTORY.ID)
                .limit(pageable.pageSize).offset(pageable.offset)
                .map { mapping(it) }
                .toList()

        val fetchCount = query.fetchCount(query.select().from(POINT_HISTORY))
        return PageImpl(results, pageable, fetchCount.toLong())
    }


    override fun findById(id: Long): PointHistory =
            query.select(
                    POINT_HISTORY.ID,
                    POINT_HISTORY.EVENT_ID,
                    POINT_HISTORY.USER_ID,
                    POINT_HISTORY.ASSOCIATE_HISTORY_ID,
                    POINT_HISTORY.EVENT_TYPE,
                    POINT_HISTORY.ACTION_TYPE,
                    POINT_HISTORY.POINT,
            )
                    .from(POINT_HISTORY)
                    .where(POINT_HISTORY.ID.eq(id))
                    .fetchOne()
                    ?.map { mapping(it) }
                    ?: throw BusinessException(ErrorStatus.NOT_FOUND, "History id = $id not Found.")

    override fun save(entity: PointHistory): PointHistory =
            query.insertInto(POINT_HISTORY).values(entity)
                    .returning()
                    .fetchOne()
                    ?.map { mapping(it) } ?: throw RuntimeException("Insert Failed.")

    override fun createHistoryBy(newHistory: PointHistory): PointHistory =
            query.insertInto(POINT_HISTORY).values(newHistory)
                    .returning()
                    .fetchOne()
                    ?.map { mapping(it) } ?: throw RuntimeException("Insert Failed.")

    override fun createEffectHistoryBy(effectHistoryId: Long, newHistory: PointHistory): PointHistory {
        newHistory.associateHistoryId = effectHistoryId
        return query.insertInto(POINT_HISTORY).values(newHistory)
                .returning()
                .fetchOne()
                ?.map { mapping(it) } ?: throw RuntimeException("Insert Failed.")
    }

    companion object {
        val POINT_HISTORY: JPointHistory = JPointHistory.POINT_HISTORY

        fun mapping(record: Record): PointHistory {
            val historyRecord: JPointHistoryRecord = record as JPointHistoryRecord

            return PointHistory(
                    id = historyRecord.id,
                    userId = historyRecord.userId,
                    eventId = historyRecord.eventId,
                    associateHistoryId = historyRecord.associateHistoryId,
                    eventType = EventType.valueOf(historyRecord.eventType),
                    actionType = ActionType.valueOf(historyRecord.actionType),
                    point = historyRecord.point,
            )
        }
    }
}
