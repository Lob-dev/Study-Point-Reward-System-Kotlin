package demo.point.edge.interfaces.api

import demo.point.edge.domain.point.PointHistory
import demo.point.edge.domain.point.models.ActionType
import demo.point.edge.domain.point.models.EventType
import demo.point.edge.domain.point.models.PointType
import demo.point.edge.interfaces.handler.PointCancelEvent
import demo.point.edge.interfaces.handler.PointEarnEvent
import demo.point.edge.interfaces.handler.PointPresentEvent


data class PointPresentRequest(
    val userId: Long,
    val targetUserId: Long,
    val pointType: PointType,
    val eventType: EventType,
    val actionType: ActionType,
    val initialPoint: Long,
) {

    fun toEvent(eventId: String) = PointPresentEvent(
        eventId = eventId,
        userId = userId,
        recipientId = targetUserId,
        pointType = pointType,
        eventType = eventType,
        actionType = actionType,
        initialPoint = initialPoint
    )
}

data class PointEarnRequest(
    val userId: Long,
    val pointType: PointType,
    val eventType: EventType,
    val actionType: ActionType,
    val initialPoint: Long,
) {

    fun toEvent(eventId: String) = PointEarnEvent(
        userId = userId,
        eventId = eventId,
        pointType = pointType,
        eventType = eventType,
        actionType = actionType,
        initialPoint = initialPoint,
    )
}

data class PointCancelRequest(
    val userId: Long,
    val historyId: Long,
    val eventType: EventType,
    val actionType: ActionType,
) {

    fun toEvent(eventId: String) = PointCancelEvent(
        eventId = eventId,
        userId = userId,
        historyId = historyId,
        eventType = eventType,
        actionType = actionType,
    )
}

data class PointUseRequest(
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

data class PointCurrentAvailableFindRequest(
    val userId: Long,
) {}

data class PointAccumulateToDayRequest(
    val userId: Long,
) {}