package demo.point.edge.interfaces.api

import demo.point.edge.domain.point.PointHistory
import demo.point.edge.domain.point.models.ActionType
import demo.point.edge.domain.point.models.EventType
import demo.point.edge.interfaces.handler.GiftPointEvent
import demo.point.edge.interfaces.handler.EarnPointEvent
import demo.point.edge.interfaces.handler.CancelPointEvent


data class GiftPointRequest(
    val userId: Long,
    val targetUserId: Long,
    val eventType: EventType,
    val actionType: ActionType,
    val initialPoint: Long,
) {

    fun toEvent(eventId: String) =
        GiftPointEvent(
            eventId = eventId,
            userId = userId,
            targetUserId = targetUserId,
            eventType = eventType,
            actionType = actionType,
            initialPoint = initialPoint
        )
}

data class EarnPointRequest(
    val userId: Long,
    val eventType: EventType,
    val actionType: ActionType,
    val initialPoint: Long,
) {

    fun toEvent(eventId: String) =
        EarnPointEvent(
            userId = userId,
            eventId = eventId,
            eventType = eventType,
            actionType = actionType,
            initialPoint = initialPoint,
        )
}

data class CancelPointRequest(
    val userId: Long,
    val historyId: Long,
    val eventType: EventType,
    val actionType: ActionType,
) {

    fun toEvent(eventId: String) =
        CancelPointEvent(
            eventId = eventId,
            userId = userId,
            historyId = historyId,
            eventType = eventType,
            actionType = actionType,
        )
}

data class UsePointRequest(
    val userId: Long,
    val eventType: EventType,
    val actionType: ActionType,
    val usePoint: Long,
) {

    fun toHistory(eventId: String) = PointHistory(
        id = null,
        eventId = eventId,
        userId = userId,
        eventType = eventType,
        actionType = actionType,
        point = usePoint,
    )
}

data class CurrentPointRequest(
    val userId: Long,
) {}

data class AccumulatedPointRequest(
    val userId: Long,
) {}