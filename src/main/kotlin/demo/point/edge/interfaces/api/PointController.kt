package demo.point.edge.interfaces.api

import demo.point.edge.application.PointFacadeService
import demo.point.edge.domain.point.PointHistoryService
import demo.point.edge.domain.point.total.PointTotalService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/points")
class PointController(
    private val pointFacadeService: PointFacadeService,
    private val pointHistoryService: PointHistoryService,
    private val pointTotalService: PointTotalService,
) {

    @PostMapping("/gift")
    fun giftPoints(giftPointRequest: GiftPointRequest): ResponseEntity<Void> {
        pointFacadeService.giftPoints(giftPointRequest)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @PostMapping
    fun earnPoints(earnPointRequest: EarnPointRequest): ResponseEntity<Void> {
        pointFacadeService.earnPoints(earnPointRequest)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PutMapping
    fun usePoints(usePointRequest: UsePointRequest): ResponseEntity<Void> {
        pointFacadeService.usePoints(usePointRequest)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @PatchMapping
    fun cancelPointByHistory(cancelPointRequest: CancelPointRequest) =
        pointFacadeService.cancelPoints(cancelPointRequest)

    @GetMapping("/current")
    fun findAvailablePointsByCurrent(currentPointRequest: CurrentPointRequest) =
        pointTotalService.findCurrentPointsBy(currentPointRequest.userId)

    @GetMapping("/today")
    fun findAccumulatedPointsByToday(accumulatedPointRequest: AccumulatedPointRequest) =
        pointHistoryService.findAccumulatedPointsBy(accumulatedPointRequest.userId)
}