package demo.point.edge.common.annotation

@Target(
    AnnotationTarget.FUNCTION
)
@Retention(AnnotationRetention.RUNTIME)
annotation class IdempotentProcess(
    val prefix: String,
    val suffix: String,
)
