package demo.point.edge.domain.point

import demo.point.edge.common.BaseTimeEntity
import demo.point.edge.domain.point.models.ActionType
import demo.point.edge.domain.point.models.EventType

class PointHistory(
    var id: Long? = null,
    private var eventId: String,
    private var userId: Long,
    var associateHistoryId: Long? = null,
    private var eventType: EventType,
    private var actionType: ActionType,
    var point: Long,
) : BaseTimeEntity() {
}