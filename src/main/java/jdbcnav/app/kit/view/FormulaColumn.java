package jdbcnav.app.kit.view;

/**
 * (c) Copyright 2006  Glamdring Incorporated Enterprises, Inc.  All rights reserved.

 */
enum FormulaColumn {
    col,
    where,
    as,
    function,
    memo,
    visible(Boolean.class),
    order_by(Boolean.class),
    ascending(Boolean.class),
    group_by(Boolean.class);

    final Class clazz;

    FormulaColumn() {
        clazz = String.class;
    }

    FormulaColumn(Class clz) {
        this.clazz = clz;
    }

}
