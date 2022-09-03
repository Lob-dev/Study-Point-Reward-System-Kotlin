package demo.point.edge.common.shard

@Target(
        AnnotationTarget.FUNCTION
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Sharding(
        val key: String,
)
