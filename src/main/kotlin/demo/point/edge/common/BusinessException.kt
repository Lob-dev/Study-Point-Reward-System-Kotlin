package demo.point.edge.common

class BusinessException(
    val errorStatus: ErrorStatus,
    val errorCause: String? = null
) : RuntimeException() {

    fun from(status: ErrorStatus): BusinessException {
        return BusinessException(status)
    }

    fun from(status: ErrorStatus, cause: String): BusinessException {
        return BusinessException(status, cause)
    }
}