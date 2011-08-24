package jdbcnav.app.kit.MetaRef;

import jdbcnav.app.kit.MD_KEYS;

import java.util.EnumMap;

/**
 * (c) Copyright 2006  Glamdring Incorporated Enterprises, Inc.  All rights reserved.

 */
public final class OutboundRef extends MdRefImpl {
    public OutboundRef(EnumMap<MD_KEYS, String> metadata) {
        super(metadata);
    }

    public Object[] getFormulaEntry() {
        return new String[]{getFTable() + '.' + getFCol(), '=' + getJoinTarget()};
    }


    public String toString() {
        return getPCol() + "<-" + getFTable() + '.' + getFCol();
    }

    private String getJoinTarget() {
        return getPTable() + '.' + getPCol();
    }

}
