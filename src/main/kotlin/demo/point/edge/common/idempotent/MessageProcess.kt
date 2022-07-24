package demo.point.edge.common.idempotent

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("message-process")
class MessageProcess(
    @Id
    var key: String
) {

    companion object {
        private const val serialVersionUID: Long = 1
    }
}