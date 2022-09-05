package demo.point.edge.common.shard

class ShardKeyResolver(
        private val shardStrategy: ShardStrategy,
        private val shardMetadata: List<ShardMetadata>,
) {

    fun resolveShardKey(shardKey: Long): Long {
        if (shardStrategy.`is`(ShardStrategy.RANGE)) {
            for (metadata in shardMetadata) {
                if (metadata.minKey <= shardKey && shardKey <= metadata.maxKey) {
                    return metadata.id
                }
            }
        }
        return (shardKey % shardMetadata.size)
    }
}