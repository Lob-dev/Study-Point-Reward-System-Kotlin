package demo.point.edge.common.shard

enum class ShardStrategy {
    RANGE, HASH;

    fun `is`(shardStrategy: ShardStrategy): Boolean =
            this == shardStrategy
}