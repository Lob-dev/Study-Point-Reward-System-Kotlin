package demo.point.edge.domain.point

import demo.point.edge.common.BusinessException
import demo.point.edge.common.ErrorStatus
import demo.point.edge.domain.point.models.ActionType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

@Service
class PointHistoryService(
    private val pointHistoryRepository: PointHistoryRepository
) {

    fun createHistoryBy(newHistory: PointHistory): PointHistory =
        pointHistoryRepository.save(newHistory)

    fun createEffectHistoryBy(effectHistoryId: Long?, newHistory: PointHistory): PointHistory {
        newHistory.associateHistoryId = effectHistoryId
        return pointHistoryRepository.save(newHistory)
    }

    fun findBy(historyId: Long): PointHistory {
        return pointHistoryRepository.findById(historyId).orElseThrow {
            BusinessException(
                ErrorStatus.NOT_FOUND,
                "history id = $historyId not Found."
            )
        }
    }

    @Transactional(readOnly = true)
    fun findAccumulatedPointsBy(userId: Long): List<PointHistory> {
        val start = ZonedDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0), ZoneId.systemDefault())
        val end = ZonedDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59), ZoneId.systemDefault())
        return pointHistoryRepository.findByUserIdAndCreateAtBetweenAndActionType(userId, start, end, ActionType.SAVE)
    }

    @Transactional(readOnly = true)
    fun findAllIdsByUserId(userId: Long): List<Long> {
        return pointHistoryRepository.findByUserId(userId)
            .map { it.id as Long }
            .toList()
    }
}