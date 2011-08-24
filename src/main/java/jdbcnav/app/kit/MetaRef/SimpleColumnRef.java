package jdbcnav.app.kit.MetaRef;

/**
 * (c) Copyright 2006  Glamdring Incorporated Enterprises, Inc.  All rights reserved.

 */
public final class SimpleColumnRef extends MdRefImpl {
    private final String table_name;
    private final String column_name;

    public SimpleColumnRef(String tableName, String columnName) {
        this.table_name = tableName;
        this.column_name = columnName;
    }

    public String toString() {
        return column_name;
    }

    public Object[] getFormulaEntry() {
        return new Object[]{table_name + '.' + column_name, "", "", "", "", true};
    }
}
