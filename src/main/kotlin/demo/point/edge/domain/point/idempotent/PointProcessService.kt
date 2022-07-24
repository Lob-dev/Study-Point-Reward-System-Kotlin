package demo.point.edge.domain.point.idempotent

import org.springframework.stereotype.Service

@Service
class PointProcessService(
    private val pointProcessRepository: PointProcessRepository,
) {

    fun checkDuplicate(userId: Long, historyId: Long): Boolean =
        pointProcessRepository.findByUserIdAndHistoryId(userId, historyId).let { true }

    fun createProcessBy(userId: Long, historyId: Long): PointProcess =
        pointProcessRepository.save(PointProcess(userId, historyId))

    fun deleteProcessBy(currentProcess: PointProcess) =
        pointProcessRepository.delete(currentProcess)
}