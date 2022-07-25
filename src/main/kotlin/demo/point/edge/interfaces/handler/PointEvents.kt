package demo.point.edge.interfaces.handler

import demo.point.edge.domain.point.PointHistory
import demo.point.edge.domain.point.active.PointActivity
import demo.point.edge.domain.point.models.ActionType
import demo.point.edge.domain.point.models.EventType
import demo.point.edge.domain.point.models.PointType
import java.time.ZonedDateTime

data class PointCancelEvent(
    val eventId: String,
    val userId: Long,
    val historyId: Long,
    val eventType: EventType,
    val actionType: ActionType,
) {

    fun toHistory() = PointHistory(
        eventId = eventId,
        userId = userId,
        associateHistoryId = historyId,
        eventType = eventType,
        actionType = actionType,
        point = 0L,
    )
}

data class PointEarnEvent(
    val eventId: String,
    val userId: Long,
    val pointType: PointType,
    val eventType: EventType,
    val actionType: ActionType,
    val initialPoint: Long,
) {

    fun toHistory() = PointHistory(
        eventId = eventId,
        userId = userId,
        eventType = eventType,
        actionType = actionType,
        point = initialPoint,
    )

    fun toActivity() = PointActivity(
        userId = userId,
        initialPoint = initialPoint,
        currentPoint = initialPoint,
        pointType = pointType,
        actionType = actionType,
        expireAt = ZonedDateTime.now()
    )
}

data class PointPresentEvent(
    val eventId: String,
    val userId: Long,
    val recipientId: Long,
    val pointType: PointType,
    val eventType: EventType,
    val actionType: ActionType,
    val initialPoint: Long,
) {

    fun toSenderHistory() = PointHistory(
        eventId = eventId,
        userId = userId,
        eventType = eventType,
        actionType = actionType,
        point = initialPoint,
    )

    fun toRecipientHistory() = PointHistory(
        eventId = eventId,
        userId = recipientId,
        eventType = eventType,
        actionType = actionType,
        point = initialPoint,
    )

    fun toActivity() = PointActivity(
        userId = recipientId,
        initialPoint = initialPoint,
        currentPoint = initialPoint,
        pointType = pointType,
        actionType = actionType,
        expireAt = ZonedDateTime.now()
    )
}