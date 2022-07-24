package demo.point.edge.common

import jodd.net.HttpStatus

enum class ErrorStatus(
    val status: HttpStatus
) {
    POINT_OVERFLOW(HttpStatus.error400()),
    LOCK_TIMEOUT(HttpStatus.error500()),
    DUPLICATE_REQUEST(HttpStatus.error409()),
    NOT_FOUND(HttpStatus.error404()),
    ;
}