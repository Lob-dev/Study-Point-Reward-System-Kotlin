package demo.point.edge.common

import java.time.ZonedDateTime

open class BaseTimeEntity {
    var createAt: ZonedDateTime? = null
    var updateAt: ZonedDateTime? = null
}