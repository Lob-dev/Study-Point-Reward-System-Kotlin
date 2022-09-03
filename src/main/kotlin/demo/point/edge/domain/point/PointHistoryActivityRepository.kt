package demo.point.edge.domain.point

import jooq.dsl.tables.JPointHistoryActivity
import jooq.dsl.tables.records.JPointHistoryActivityRecord
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository

interface PointHistoryActivityRepository {
    fun findAllByHistoryId(historyId: Long?): List<PointHistoryActivity>
    fun saveAll(historyActivity: List<PointHistoryActivity>): MutableList<PointHistoryActivity>
}

@Repository
class PointHistoryActivityRepositoryImpl(
        private val query: DSLContext,
) : PointHistoryActivityRepository {

    override fun findAllByHistoryId(historyId: Long?): List<PointHistoryActivity> =
            query.select(
                    POINT_HISTORY_ACTIVITY.ID,
                    POINT_HISTORY_ACTIVITY.HISTORY_ID,
                    POINT_HISTORY_ACTIVITY.ACTIVITY_ID,
            )
                    .from(POINT_HISTORY_ACTIVITY)
                    .where(POINT_HISTORY_ACTIVITY.HISTORY_ID.eq(historyId))
                    .stream().map { mapping(it) }
                    .toList()

    override fun saveAll(entities: List<PointHistoryActivity>): MutableList<PointHistoryActivity> {
        val insertRows = entities.stream()
                .map { DSL.row(it.historyId, it.activityId) }
                .toList()

        return query.insertInto(
                POINT_HISTORY_ACTIVITY,
                POINT_HISTORY_ACTIVITY.HISTORY_ID,
                POINT_HISTORY_ACTIVITY.ACTIVITY_ID,
        )
                .valuesOfRows(insertRows)
                .returning()
                .fetch()
                .map { mapping(it) }
    }

    companion object {
        val POINT_HISTORY_ACTIVITY: JPointHistoryActivity = JPointHistoryActivity.POINT_HISTORY_ACTIVITY

        fun mapping(record: Record): PointHistoryActivity {
            val historyActivityRecord: JPointHistoryActivityRecord = record as JPointHistoryActivityRecord

            return PointHistoryActivity(
                    id = historyActivityRecord.id,
                    historyId = historyActivityRecord.historyId,
                    activityId = historyActivityRecord.activityId,
            )
        }
    }
}
