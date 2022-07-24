package demo.point.edge.domain.point.models

import java.util.function.BiFunction

enum class OperationType(
    val function: BiFunction<Long, Long, Long>
) {
    PLUS(Math::addExact),
    MINUS(Math::subtractExact);
}