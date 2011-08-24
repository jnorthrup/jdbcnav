package jdbcnav.app.kit.MetaRef;

/**
 * (c) Copyright 2006  Glamdring Incorporated Enterprises, Inc.  All rights reserved.

 */
public final class OidColumnRef extends MdRefImpl {
    private final String table_name;
    private static final String OID = "id";

    public OidColumnRef(String tableName) {
        this.table_name = tableName;
    }

    private static String getColumn_name() {
        return OID;
    }

    public String toString() {
        return OID;
    }

    public Object[] getFormulaEntry() {
        return new Object[]{table_name + '.' + OID};
    }
}
