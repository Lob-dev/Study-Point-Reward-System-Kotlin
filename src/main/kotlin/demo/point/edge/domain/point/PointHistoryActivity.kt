package demo.point.edge.domain.point

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class PointHistoryActivity(
    @Id
    @GeneratedValue
    private var id: Long? = null,
    private var historyId: Long?,
    var activityId: Long?,
) {
}