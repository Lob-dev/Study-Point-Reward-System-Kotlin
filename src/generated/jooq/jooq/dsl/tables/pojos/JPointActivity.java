/*
 * This file is generated by jOOQ.
 */
package jooq.dsl.tables.pojos;


import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class JPointActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long          id;
    private final String        actionType;
    private final Long          currentPoint;
    private final LocalDateTime expireAt;
    private final Long          initialPoint;
    private final String        pointType;
    private final Long          userId;

    public JPointActivity(JPointActivity value) {
        this.id = value.id;
        this.actionType = value.actionType;
        this.currentPoint = value.currentPoint;
        this.expireAt = value.expireAt;
        this.initialPoint = value.initialPoint;
        this.pointType = value.pointType;
        this.userId = value.userId;
    }

    public JPointActivity(
        Long          id,
        String        actionType,
        Long          currentPoint,
        LocalDateTime expireAt,
        Long          initialPoint,
        String        pointType,
        Long          userId
    ) {
        this.id = id;
        this.actionType = actionType;
        this.currentPoint = currentPoint;
        this.expireAt = expireAt;
        this.initialPoint = initialPoint;
        this.pointType = pointType;
        this.userId = userId;
    }

    /**
     * Getter for <code>point.point_activity.id</code>.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Getter for <code>point.point_activity.action_type</code>.
     */
    public String getActionType() {
        return this.actionType;
    }

    /**
     * Getter for <code>point.point_activity.current_point</code>.
     */
    public Long getCurrentPoint() {
        return this.currentPoint;
    }

    /**
     * Getter for <code>point.point_activity.expire_at</code>.
     */
    public LocalDateTime getExpireAt() {
        return this.expireAt;
    }

    /**
     * Getter for <code>point.point_activity.initial_point</code>.
     */
    public Long getInitialPoint() {
        return this.initialPoint;
    }

    /**
     * Getter for <code>point.point_activity.point_type</code>.
     */
    public String getPointType() {
        return this.pointType;
    }

    /**
     * Getter for <code>point.point_activity.user_id</code>.
     */
    public Long getUserId() {
        return this.userId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("JPointActivity (");

        sb.append(id);
        sb.append(", ").append(actionType);
        sb.append(", ").append(currentPoint);
        sb.append(", ").append(expireAt);
        sb.append(", ").append(initialPoint);
        sb.append(", ").append(pointType);
        sb.append(", ").append(userId);

        sb.append(")");
        return sb.toString();
    }
}
