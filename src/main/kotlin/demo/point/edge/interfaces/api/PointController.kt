package demo.point.edge.interfaces.api

import demo.point.edge.application.PointFacadeService
import demo.point.edge.common.idempotent.Idempotent
import demo.point.edge.domain.point.PointHistory
import demo.point.edge.domain.point.PointHistoryService
import demo.point.edge.domain.point.total.PointTotal
import demo.point.edge.domain.point.total.PointTotalService
import jooq.dsl.tables.JPointHistory
import org.jooq.Condition
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
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

    @GetMapping("/histories")
    fun getHistoryPage(
        @PageableDefault pageable: Pageable,
        request: PointHistoriesFindRequest
    ): ResponseEntity<Page<PointHistory>> {
        val condition: Condition = JPointHistory.POINT_HISTORY.USER_ID.eq(request.userId)
        val pointHistoriesByUser = pointHistoryService.getPage(pageable, condition)
        return ResponseEntity.status(HttpStatus.OK).body(pointHistoriesByUser)
    }

    @GetMapping("/current")
    fun findCurrentAvailablePoints(request: PointCurrentAvailableFindRequest): ResponseEntity<PointTotal> {
        val currentPointsByUser = pointTotalService.findCurrentPointsBy(request.userId)
        return ResponseEntity.status(HttpStatus.OK).body(currentPointsByUser)
    }

    @GetMapping("/today")
    fun findAccumulatedPoints(request: PointAccumulateToDayRequest): ResponseEntity<Long> {
        val accumulatedPointsByUser = pointHistoryService.findAllPointHistoriesByToday(request.userId)
            .sumOf { it.point }
        return ResponseEntity.status(HttpStatus.OK).body(accumulatedPointsByUser)
    }

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

    @Idempotent(prefix = "#request.userId", suffix = "#request.actionType")
    @PatchMapping
    fun cancelPointByHistory(request: PointCancelRequest): ResponseEntity<Void> {
        pointFacadeService.cancelPoints(request)
        return ResponseEntity.status(HttpStatus.OK).build()
    }
}