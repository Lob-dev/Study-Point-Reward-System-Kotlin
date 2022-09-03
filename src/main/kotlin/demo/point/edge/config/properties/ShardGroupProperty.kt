package demo.point.edge.config.properties

import demo.point.edge.common.shard.ShardStrategy
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "shard-group")
data class ShardGroupProperty(
        val strategy: ShardStrategy,
        val nodes: List<ShardNodeConfig>,
)

data class ShardNodeConfig(
        val groupId: Long,
        val readOnly: Boolean,
        val url: String,
        val username: String,
        val password: String,
        val driverClassName: String,
        val minKey: Long,
        val maxKey: Long,
)