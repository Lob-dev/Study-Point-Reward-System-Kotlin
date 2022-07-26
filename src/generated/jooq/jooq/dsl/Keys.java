/*
 * This file is generated by jOOQ.
 */
package jooq.dsl;


import jooq.dsl.tables.JPointActivity;
import jooq.dsl.tables.JPointHistory;
import jooq.dsl.tables.JPointHistoryActivity;
import jooq.dsl.tables.records.JPointActivityRecord;
import jooq.dsl.tables.records.JPointHistoryActivityRecord;
import jooq.dsl.tables.records.JPointHistoryRecord;

import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * point.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<JPointActivityRecord> KEY_POINT_ACTIVITY_PRIMARY = Internal.createUniqueKey(JPointActivity.POINT_ACTIVITY, DSL.name("KEY_point_activity_PRIMARY"), new TableField[] { JPointActivity.POINT_ACTIVITY.ID }, true);
    public static final UniqueKey<JPointHistoryRecord> KEY_POINT_HISTORY_PRIMARY = Internal.createUniqueKey(JPointHistory.POINT_HISTORY, DSL.name("KEY_point_history_PRIMARY"), new TableField[] { JPointHistory.POINT_HISTORY.ID }, true);
    public static final UniqueKey<JPointHistoryActivityRecord> KEY_POINT_HISTORY_ACTIVITY_PRIMARY = Internal.createUniqueKey(JPointHistoryActivity.POINT_HISTORY_ACTIVITY, DSL.name("KEY_point_history_activity_PRIMARY"), new TableField[] { JPointHistoryActivity.POINT_HISTORY_ACTIVITY.ID }, true);
}
