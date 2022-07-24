package demo.point.edge.domain.point.idempotent

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("point-process")
class PointProcess(
    @Id
    var userId: Long,
    private var historyId: Long,
) {

    companion object {
        private const val serialVersionUID: Long = 1
    }
}