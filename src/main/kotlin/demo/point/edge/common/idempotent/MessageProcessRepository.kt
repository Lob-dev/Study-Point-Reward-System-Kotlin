package demo.point.edge.common.idempotent

import org.springframework.data.repository.CrudRepository

interface MessageProcessRepository: CrudRepository<MessageProcess, String> {
}