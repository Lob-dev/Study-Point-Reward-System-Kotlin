package demo.point.edge.interfaces.api

import demo.point.edge.application.PointFacadeService
import demo.point.edge.common.annotation.IdempotentProcess
import demo.point.edge.domain.point.PointHistoryService
import demo.point.edge.domain.point.total.PointTotal
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
    fun giftPoints(request: PointPresentRequest): ResponseEntity<Void> {
        pointFacadeService.giftPoints(request)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @PostMapping
    fun earnPoints(request: PointEarnRequest): ResponseEntity<Void> {
        pointFacadeService.earnPoints(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PutMapping
    fun usePoints(request: PointUseRequest): ResponseEntity<Void> {
        pointFacadeService.usePoints(request)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @IdempotentProcess(prefix = "#request.userId", suffix = "#request.actionType")
    @PatchMapping
    fun cancelPointByHistory(request: PointCancelRequest): ResponseEntity<Void> {
        pointFacadeService.cancelPoints(request)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @GetMapping("/current")
    fun findAvailablePointsByCurrent(request: PointCurrentAvailableFindRequest): ResponseEntity<PointTotal> {
        val findCurrentPoints = pointTotalService.findCurrentPointsBy(request.userId)
        return ResponseEntity.status(HttpStatus.OK).body(findCurrentPoints)
    }

    @GetMapping("/today")
    fun findAccumulatedPointsByToday(request: PointAccumulateToDayRequest): ResponseEntity<Long> {
        val accumulatedPoints = pointHistoryService.findAccumulatedPointsBy(request.userId)
            .sumOf { it.point }
        return ResponseEntity.status(HttpStatus.OK).body(accumulatedPoints)
    }
}