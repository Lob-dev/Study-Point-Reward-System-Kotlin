package demo.point.edge.domain.point.active

import demo.point.edge.common.BusinessException
import demo.point.edge.common.ErrorStatus
import demo.point.edge.domain.point.models.ActionType
import demo.point.edge.domain.point.models.PointType
import jooq.dsl.tables.JPointActivity
import jooq.dsl.tables.records.JPointActivityRecord
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime

interface PointActivityRepository {
    fun findAllById(ids: List<Long>): MutableList<PointActivity>
    fun findAllByUserId(userId: Long, currentPoint: Long): List<PointActivity>
    fun save(entity: PointActivity): PointActivity
    fun batchUpdateCurrentPoint(entities: List<PointActivity>)
}

@Repository
class PointActivityRepositoryImpl(
        private val query: DSLContext,
) : PointActivityRepository {

    fun findById(id: Long): PointActivity =
            query.select(
                    POINT_ACTIVITY.ID,
                    POINT_ACTIVITY.USER_ID,
                    POINT_ACTIVITY.INITIAL_POINT,
                    POINT_ACTIVITY.CURRENT_POINT,
                    POINT_ACTIVITY.POINT_TYPE,
                    POINT_ACTIVITY.ACTION_TYPE,
                    POINT_ACTIVITY.EXPIRE_AT,
            )
                    .from(POINT_ACTIVITY)
                    .where(POINT_ACTIVITY.ID.eq(id))
                    .fetchOne()
                    ?.map { mapping(it) }
                    ?: throw BusinessException(ErrorStatus.NOT_FOUND, "Activity id = $id not Found.")

    override fun findAllById(ids: List<Long>): MutableList<PointActivity> =
            query.select(
                    POINT_ACTIVITY.ID,
                    POINT_ACTIVITY.USER_ID,
                    POINT_ACTIVITY.INITIAL_POINT,
                    POINT_ACTIVITY.CURRENT_POINT,
                    POINT_ACTIVITY.POINT_TYPE,
                    POINT_ACTIVITY.ACTION_TYPE,
                    POINT_ACTIVITY.EXPIRE_AT,
            )
                    .from(POINT_ACTIVITY)
                    .where(POINT_ACTIVITY.ID.`in`(ids))
                    .stream()
                    .map { mapping(it) }
                    .toList()

    override fun findAllByUserId(userId: Long, currentPoint: Long): List<PointActivity> =
            query.select(
                    POINT_ACTIVITY.ID,
                    POINT_ACTIVITY.USER_ID,
                    POINT_ACTIVITY.INITIAL_POINT,
                    POINT_ACTIVITY.CURRENT_POINT,
                    POINT_ACTIVITY.POINT_TYPE,
                    POINT_ACTIVITY.ACTION_TYPE,
                    POINT_ACTIVITY.EXPIRE_AT,
            )
                    .from(POINT_ACTIVITY)
                    .where(POINT_ACTIVITY.USER_ID.eq(userId).and(POINT_ACTIVITY.CURRENT_POINT.greaterThan(0)))
                    .stream()
                    .map { mapping(it) }
                    .toList()


    override fun save(entity: PointActivity): PointActivity {
        return query.insertInto(POINT_ACTIVITY).values(entity)
                .returning()
                .fetchOne()
                ?.map { mapping(it) } ?: throw RuntimeException("Insert Failed.")
    }

    override fun batchUpdateCurrentPoint(entities: List<PointActivity>) {
        query.batched { config ->
            for (entity in entities) {
                config.dsl().update(POINT_ACTIVITY)
                        .set(POINT_ACTIVITY.CURRENT_POINT, entity.currentPoint)
                        .where(POINT_ACTIVITY.ID.eq(entity.id))
                        .execute();
            }
        }
    }

    companion object {
        val POINT_ACTIVITY: JPointActivity = JPointActivity.POINT_ACTIVITY

        fun mapping(record: Record): PointActivity {
            val activityRecord: JPointActivityRecord = record as JPointActivityRecord

            return PointActivity(
                    id = activityRecord.id,
                    userId = activityRecord.userId,
                    initialPoint = activityRecord.initialPoint,
                    currentPoint = activityRecord.currentPoint,
                    pointType = PointType.valueOf(activityRecord.pointType),
                    actionType = ActionType.valueOf(activityRecord.actionType),
                    expireAt = ZonedDateTime.from(activityRecord.expireAt),
            )
        }
    }
}
