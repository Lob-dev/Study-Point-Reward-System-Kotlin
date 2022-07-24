package demo.point.edge.interfaces.handler

import demo.point.edge.domain.point.PointHistory
import demo.point.edge.domain.point.models.ActionType
import demo.point.edge.domain.point.models.EventType

data class CancelPointEvent(
    val eventId: String,
    val userId: Long,
    val historyId: Long,
    val eventType: EventType,
    val actionType: ActionType,
) {

    fun toHistory(eventId: String) = PointHistory(
        eventId = eventId,
        userId = userId,
        effectHistoryId = historyId,
        eventType = eventType,
        actionType = actionType,
        point = 0L,
    )
}

data class EarnPointEvent(
    val eventId: String,
    val userId: Long,
    val eventType: EventType,
    val actionType: ActionType,
    val initialPoint: Long,
) {

    fun toHistory(eventId: String) = PointHistory(
        eventId = eventId,
        userId = userId,
        eventType = eventType,
        actionType = actionType,
        point = initialPoint,
    )
}

data class GiftPointEvent(
    val eventId: String,
    val userId: Long,
    val targetUserId: Long,
    val eventType: EventType,
    val actionType: ActionType,
    val initialPoint: Long,
) {

    fun toHistory(eventId: String) = PointHistory(
        eventId = eventId,
        userId = userId,
        eventType = eventType,
        actionType = actionType,
        point = initialPoint,
    )
}