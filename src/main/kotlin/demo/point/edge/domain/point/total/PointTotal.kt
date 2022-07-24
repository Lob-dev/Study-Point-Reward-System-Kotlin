package demo.point.edge.domain.point.total

import demo.point.edge.domain.point.models.OperationType
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.io.Serializable

@RedisHash("user-total-points")
class PointTotal(
    @Id
    private var id: Long? = null,
    @Indexed
    private var userId: Long,
    private var totalPoint: Long,
) : Serializable {

    fun updatePointsBy(updatePoint: Long, operation: OperationType = OperationType.PLUS) {
        val currentPoint = when (operation) {
            OperationType.PLUS -> OperationType.PLUS.function.apply(this.totalPoint, updatePoint)
            OperationType.MINUS -> OperationType.MINUS.function.apply(this.totalPoint, updatePoint)
        }
        this.totalPoint = currentPoint
    }

    companion object {
        private const val serialVersionUID: Long = 1;
    }
}