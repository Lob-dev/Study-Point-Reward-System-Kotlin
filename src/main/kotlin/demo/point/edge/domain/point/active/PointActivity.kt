package demo.point.edge.domain.point.active

import demo.point.edge.domain.point.models.ActionType
import demo.point.edge.domain.point.models.PointType
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(
    indexes = [
        Index(name = "IDX_POINT_ACTIVITY_USER_ID", columnList = "userId"),
    ]
)
class PointActivity(
    @Id
    @GeneratedValue
    var id: Long? = null,
    private var userId: Long,
    @Column(updatable = false)
    private var initialPoint: Long,
    var currentPoint: Long,
    @Enumerated(EnumType.STRING)
    private var pointType: PointType,
    @Enumerated(EnumType.STRING)
    private var actionType: ActionType,
    @Column(updatable = false)
    private var expireAt: ZonedDateTime,
) {

    fun redeemPoint(usePoint: Long): Long {
        if (currentPoint > usePoint) {
            currentPoint -= usePoint
            return 0
        }

        val remainPoint = usePoint - currentPoint
        currentPoint = 0
        return remainPoint
    }

    fun restorePoint(currentRestorePoint: Long): Long {
        val maxRestorePoint = initialPoint - currentPoint
        if (maxRestorePoint < currentRestorePoint) {
            currentPoint += maxRestorePoint
            return currentRestorePoint - maxRestorePoint
        }

        currentPoint += currentRestorePoint
        return 0L
    }
}