package demo.point.edge.common.shard

data class ShardMetadata(
        val id: Long,
        val minKey: Long,
        val maxKey: Long,
)