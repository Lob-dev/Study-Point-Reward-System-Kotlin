package demo.point.edge.application

import demo.point.edge.common.annotation.FacadeService
import demo.point.edge.domain.point.PointHistoryService
import demo.point.edge.domain.point.active.PointActivityService
import demo.point.edge.domain.point.total.PointTotalService
import demo.point.edge.interfaces.handler.EarnPointEvent

@FacadeService
class PointEarnService(
    private val pointHistoryService: PointHistoryService,
    private val pointActivityService: PointActivityService,
    private val pointTotalService: PointTotalService,
) {

    fun earnPoints(earnEvent: EarnPointEvent): Any {
        TODO("Not yet implemented")
    }
}