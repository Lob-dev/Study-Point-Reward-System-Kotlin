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
public class JPointHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long          id;
    private final LocalDateTime createAt;
    private final LocalDateTime updateAt;
    private final String        actionType;
    private final String        eventId;
    private final String        eventType;
    private final Long          point;
    private final Long          userId;
    private final Long          associateHistoryId;

    public JPointHistory(JPointHistory value) {
        this.id = value.id;
        this.createAt = value.createAt;
        this.updateAt = value.updateAt;
        this.actionType = value.actionType;
        this.eventId = value.eventId;
        this.eventType = value.eventType;
        this.point = value.point;
        this.userId = value.userId;
        this.associateHistoryId = value.associateHistoryId;
    }

    public JPointHistory(
        Long          id,
        LocalDateTime createAt,
        LocalDateTime updateAt,
        String        actionType,
        String        eventId,
        String        eventType,
        Long          point,
        Long          userId,
        Long          associateHistoryId
    ) {
        this.id = id;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.actionType = actionType;
        this.eventId = eventId;
        this.eventType = eventType;
        this.point = point;
        this.userId = userId;
        this.associateHistoryId = associateHistoryId;
    }

    /**
     * Getter for <code>point.point_history.id</code>.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Getter for <code>point.point_history.create_at</code>.
     */
    public LocalDateTime getCreateAt() {
        return this.createAt;
    }

    /**
     * Getter for <code>point.point_history.update_at</code>.
     */
    public LocalDateTime getUpdateAt() {
        return this.updateAt;
    }

    /**
     * Getter for <code>point.point_history.action_type</code>.
     */
    public String getActionType() {
        return this.actionType;
    }

    /**
     * Getter for <code>point.point_history.event_id</code>.
     */
    public String getEventId() {
        return this.eventId;
    }

    /**
     * Getter for <code>point.point_history.event_type</code>.
     */
    public String getEventType() {
        return this.eventType;
    }

    /**
     * Getter for <code>point.point_history.point</code>.
     */
    public Long getPoint() {
        return this.point;
    }

    /**
     * Getter for <code>point.point_history.user_id</code>.
     */
    public Long getUserId() {
        return this.userId;
    }

    /**
     * Getter for <code>point.point_history.associate_history_id</code>.
     */
    public Long getAssociateHistoryId() {
        return this.associateHistoryId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("JPointHistory (");

        sb.append(id);
        sb.append(", ").append(createAt);
        sb.append(", ").append(updateAt);
        sb.append(", ").append(actionType);
        sb.append(", ").append(eventId);
        sb.append(", ").append(eventType);
        sb.append(", ").append(point);
        sb.append(", ").append(userId);
        sb.append(", ").append(associateHistoryId);

        sb.append(")");
        return sb.toString();
    }
}
