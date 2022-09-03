package demo.point.edge.domain.point.active

import demo.point.edge.domain.point.models.ActionType
import demo.point.edge.domain.point.models.PointType
import java.time.ZonedDateTime

class PointActivity(
    var id: Long? = null,
    private var userId: Long,
    private var initialPoint: Long,
    var currentPoint: Long,
    private var pointType: PointType,
    private var actionType: ActionType,
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