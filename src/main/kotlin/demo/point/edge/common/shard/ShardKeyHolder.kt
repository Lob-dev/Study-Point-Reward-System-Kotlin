package demo.point.edge.common.shard

import kotlin.concurrent.getOrSet

class ShardKeyHolder {

    companion object {
        private val keyHolder: ThreadLocal<Long> = ThreadLocal()
        fun getKey(): Long = keyHolder.getOrSet { 1 }
        fun setKey(key: Long): Unit = keyHolder.set(key)
        fun refresh(): Unit = keyHolder.remove()
    }
}