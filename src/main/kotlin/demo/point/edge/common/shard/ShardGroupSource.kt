package demo.point.edge.common.shard

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.support.TransactionSynchronizationManager

class ShardGroupSource(
        dataSources: Map<Any, Any>,
        private val shardKeyResolver: ShardKeyResolver,
) : AbstractRoutingDataSource() {

    init {
        val defaultDataSource = dataSources.entries.iterator().next()
        super.setDefaultTargetDataSource(defaultDataSource.value)
        super.setTargetDataSources(dataSources)
    }

    override fun determineCurrentLookupKey(): Any {
        val currentLookupKey = "${getGroupId(ShardKeyHolder.getKey())}_${getReadOption()}"
        logger.debug("current lookup key = $currentLookupKey")
        return currentLookupKey
    }

    private fun getGroupId(shardKey: Long): Long {
        if (TransactionShardManager.isCurrentTransactionUseSharding()) {
            return shardKeyResolver.resolveShardKey(shardKey)
        }
        return DEFAULT_GROUP_ID
    }

    private fun getReadOption(): String {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
                return ReadOption.RO.toString()
            }
            return ReadOption.RW.toString()
        }
        return ReadOption.RW.toString()
    }

    companion object {
        private const val DEFAULT_GROUP_ID = 1L
    }
}
