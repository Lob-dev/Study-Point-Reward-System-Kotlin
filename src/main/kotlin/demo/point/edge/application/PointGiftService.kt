package demo.point.edge.application

import demo.point.edge.common.annotation.FacadeService
import demo.point.edge.domain.point.PointHistoryService
import demo.point.edge.domain.point.active.PointActivityService
import demo.point.edge.domain.point.total.PointTotalService
import demo.point.edge.interfaces.handler.GiftPointEvent

@FacadeService
class PointGiftService(
    private val pointHistoryService: PointHistoryService,
    private val pointActivityService: PointActivityService,
    private val pointTotalService: PointTotalService,
) {

    fun deliveryGift(giftPointEvent: GiftPointEvent): Any {
        TODO("Not yet implemented")
    }
}