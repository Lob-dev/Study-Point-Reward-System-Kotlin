package demo.point.edge.common.idempotent

@Target(
    AnnotationTarget.FUNCTION
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Idempotent(
    val prefix: String,
    val suffix: String,
)
