package demo.point.edge.common.shard

class ShardKeyHolder {

    companion object {
        private val keyHolder: ThreadLocal<Long> = ThreadLocal()
        fun getKey(): Long = keyHolder.get()
        fun setKey(key: Long): Unit = keyHolder.set(key)
        fun refresh(): Unit = keyHolder.remove()
    }
}