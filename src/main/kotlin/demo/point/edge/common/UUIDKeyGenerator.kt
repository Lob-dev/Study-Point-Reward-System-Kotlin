package demo.point.edge.common

import java.util.*

class UUIDKeyGenerator {
    companion object {
        fun generate(): String = UUID.randomUUID().toString()
    }
}