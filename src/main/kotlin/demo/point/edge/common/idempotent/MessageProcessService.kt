package demo.point.edge.common.idempotent

import org.springframework.stereotype.Service

@Service
class MessageProcessService(
    private val messageProcessRepository: MessageProcessRepository,
) {

    fun checkDuplicateProcess(key: String): Boolean =
        messageProcessRepository.findById(key).isPresent

    fun createProcessBy(key: String): MessageProcess =
        messageProcessRepository.save(MessageProcess(key))

    fun deleteProcessBy(currentProcess: MessageProcess) =
        messageProcessRepository.delete(currentProcess)
}