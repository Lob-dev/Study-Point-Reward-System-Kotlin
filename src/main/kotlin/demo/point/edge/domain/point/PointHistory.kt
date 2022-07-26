package demo.point.edge.domain.point

import demo.point.edge.common.BaseTimeEntity
import demo.point.edge.domain.point.models.ActionType
import demo.point.edge.domain.point.models.EventType
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(
    indexes = [
        Index(name = "IDX_POINT_HISTORY_USER_ID", columnList = "userId"),
    ]
)
class PointHistory(
    @Id
    @GeneratedValue
    var id: Long? = null,
    private var eventId: String,
    private var userId: Long,
    var associateHistoryId: Long? = null,
    @Enumerated(EnumType.STRING)
    private var eventType: EventType,
    @Enumerated(EnumType.STRING)
    private var actionType: ActionType,
    var point: Long,
) : BaseTimeEntity() {
}