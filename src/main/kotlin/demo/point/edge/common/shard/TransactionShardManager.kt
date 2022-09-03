package demo.point.edge.common.shard

import kotlin.concurrent.getOrSet


class TransactionShardManager {
    companion object {
        private val currentTransactionIsSharding: ThreadLocal<Boolean> = ThreadLocal<Boolean>()
        fun setCurrentTransactionIsSharding(isSharding: Boolean) =
                currentTransactionIsSharding.set(isSharding)

        fun isCurrentTransactionUseSharding(): Boolean =
                currentTransactionIsSharding.getOrSet { false }

        fun refresh(): Unit =
                currentTransactionIsSharding.set(false)
    }
}