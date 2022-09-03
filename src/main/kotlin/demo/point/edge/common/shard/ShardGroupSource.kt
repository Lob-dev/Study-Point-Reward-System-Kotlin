package demo.point.edge.common.shard

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.support.TransactionSynchronizationManager

class ShardGroupSource(
        dataSources: Map<Any, Any>,
        private val shardKeyResolver: ShardKeyResolver,
) : AbstractRoutingDataSource() {
    private val defaultLookupKey: String

    init {
        val defaultDataSource = dataSources.entries.iterator().next()
        defaultLookupKey = defaultDataSource.key as String
        super.setDefaultTargetDataSource(defaultDataSource.value)
        super.setTargetDataSources(dataSources)
    }

    override fun determineCurrentLookupKey(): Any {
        if (TransactionShardManager.isCurrentTransactionUseSharding()) {
            if (TransactionSynchronizationManager.isActualTransactionActive()) {
                val resolveKey = shardKeyResolver.resolveShardKey(ShardKeyHolder.getKey())
                return if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
                    "${resolveKey}_${ReadOption.RO}"
                } else {
                    "${resolveKey}_${ReadOption.RW}"
                }
            }
        }
        return defaultLookupKey
    }
}