package demo.point.edge.common

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

class Constant {
    companion object {
        val TODAY_DATE: LocalDate = LocalDate.now()
        val BEGIN_THE_DAY: LocalTime = LocalTime.of(0, 0, 0)
        val END_THE_DAY: LocalTime = LocalTime.of(23, 59, 59)
        val DEFAULT_ZONE_ID: ZoneId =  ZoneId.systemDefault()
    }
}