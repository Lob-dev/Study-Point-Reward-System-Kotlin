package demo.point.edge.common.lock

import java.util.concurrent.TimeUnit

@Target(
    AnnotationTarget.FUNCTION
)
@Retention(AnnotationRetention.RUNTIME)
annotation class DistributedLock(
    val prefix: String,
    val key: String,
    val suffix: String = ":LOCK",
    val waitTime: Long = 1,
    val leaseTime: Long = 3,
    val timeUnit: TimeUnit = TimeUnit.SECONDS,
)
