package jdbcnav.app.kit.MetaRef;

import jdbcnav.app.kit.MD_KEYS;

import java.util.EnumMap;

/**
 * (c) Copyright 2006  Glamdring Incorporated Enterprises, Inc.  All rights reserved.

 */
public final class InboundRef extends MdRefImpl {
    public InboundRef(final EnumMap<MD_KEYS, String> metadata) { super(metadata); }

    public Object[] getFormulaEntry() {
        final String fst = new StringBuilder().append(getFTable()).append('.').append(getFCol()).toString();
        final String snd = new StringBuilder().append("=").append(getJoinTarget()).toString();
        return new String[]{fst, snd};
    }

    public String toString() {
        return new StringBuilder().append(getFCol()).append("->").append(getJoinTarget()).toString();
    }

    private String getJoinTarget() {
        return new StringBuilder().append(getPTable()).append('.').append(getPCol()).toString();
    }


}
