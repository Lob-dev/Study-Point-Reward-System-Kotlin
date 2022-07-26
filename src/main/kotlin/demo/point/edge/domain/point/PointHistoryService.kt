package demo.point.edge.domain.point

import com.querydsl.core.types.Predicate
import demo.point.edge.common.BusinessException
import demo.point.edge.common.Constant.Companion.BEGIN_THE_DAY
import demo.point.edge.common.Constant.Companion.DEFAULT_ZONE_ID
import demo.point.edge.common.Constant.Companion.END_THE_DAY
import demo.point.edge.common.Constant.Companion.TODAY_DATE
import demo.point.edge.common.ErrorStatus
import demo.point.edge.domain.point.models.ActionType.SAVE
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@Service
class PointHistoryService(
    private val pointHistoryRepository: PointHistoryRepository,
    private val pointHistoryQueryRepository: PointHistoryQueryRepository,
) {

    @Transactional(readOnly = true)
    fun getPage(pageable: Pageable, predicate: Predicate): Page<PointHistory> =
        pointHistoryQueryRepository.getPage(pageable, predicate)

    @Transactional(readOnly = true)
    fun findAllPointHistoriesByToday(userId: Long): List<PointHistory> {
        val beginTheDay = ZonedDateTime.of(TODAY_DATE, BEGIN_THE_DAY, DEFAULT_ZONE_ID)
        val endTheDay = ZonedDateTime.of(TODAY_DATE, END_THE_DAY, DEFAULT_ZONE_ID)
        return pointHistoryRepository.findByUserIdAndCreateAtBetweenAndActionType(userId, beginTheDay, endTheDay, SAVE)
    }

    fun findBy(historyId: Long): PointHistory =
        pointHistoryRepository.findById(historyId).orElseThrow {
            BusinessException(ErrorStatus.NOT_FOUND, "history id = $historyId not Found.")
        }

    fun createHistoryBy(newHistory: PointHistory): PointHistory =
        pointHistoryRepository.save(newHistory)

    @Transactional
    fun createEffectHistoryBy(effectHistoryId: Long?, newHistory: PointHistory): PointHistory =
        pointHistoryRepository.save(newHistory.also {
            newHistory.associateHistoryId = effectHistoryId
        })
}