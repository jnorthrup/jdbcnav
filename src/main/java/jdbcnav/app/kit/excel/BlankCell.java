package jdbcnav.app.kit.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;

import java.io.Serializable;

/**
 * singleton blank cell -- returns empty string.
 * <p/>
 * Cell Type "Skip" does not produce these nodes,  or any other nodes, but almost any other cell type which ends up
 * NULL or blank will return our "INSTANCE" singleton, which enforces a String celltype.
 * <p/>
 * (c) Copyright 2006  Glamdring Incorporated Enterprises, Inc.  All rights reserved.

 */
public class BlankCell extends CellImpl<String> {
    private static final BlankCell INSTANCE = new BlankCell(null);

    private BlankCell(HSSFCell cell) {
        super(cell);
    }


    public static BlankCell instance() {
        return INSTANCE;
    }

    public Serializable getValue() {
        return "";
    }
}
