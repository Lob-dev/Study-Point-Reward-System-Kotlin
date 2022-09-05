CREATE DATABASE point /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE point;

-- `point`.point_activity definition

CREATE TABLE point_activity
(
    id            bigint(20) NOT NULL,
    action_type   varchar(255) DEFAULT NULL,
    current_point bigint(20) NOT NULL,
    expire_at     datetime(6)  DEFAULT NULL,
    initial_point bigint(20)   DEFAULT NULL,
    point_type    varchar(255) DEFAULT NULL,
    user_id       bigint(20) NOT NULL,
    PRIMARY KEY (id),
    KEY IDX_POINT_ACTIVITY_USER_ID (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- `point`.point_history definition

CREATE TABLE point_history
(
    id                   bigint(20)  NOT NULL,
    create_at            datetime(6) NOT NULL,
    update_at            datetime(6) NOT NULL,
    action_type          varchar(255) DEFAULT NULL,
    event_id             varchar(255) DEFAULT NULL,
    event_type           varchar(255) DEFAULT NULL,
    point                bigint(20)  NOT NULL,
    user_id              bigint(20)  NOT NULL,
    associate_history_id bigint(20)   DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY IDX_POINT_HISTORY_USER_ID (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- `point`.point_history_activity definition

CREATE TABLE point_history_activity
(
    id          bigint(20) NOT NULL,
    activity_id bigint(20) DEFAULT NULL,
    history_id  bigint(20) DEFAULT NULL,
    PRIMARY KEY (id),
    KEY IDX_POINT_HISTORY_ACTIVITY_HISTORY_ID (history_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
