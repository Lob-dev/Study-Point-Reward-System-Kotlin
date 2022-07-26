package demo.point.edge.domain.point

import javax.persistence.*

@Entity
@Table(
    indexes = [
        Index(name = "IDX_POINT_HISTORY_ACTIVITY_HISTORY_ID", columnList = "historyId")
    ]
)
class PointHistoryActivity(
    @Id
    @GeneratedValue
    private var id: Long? = null,
    private var historyId: Long?,
    var activityId: Long?,
) {
}