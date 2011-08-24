package jdbcnav.app.kit.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;

import java.io.Serializable;

/**
 * (c) Copyright 2006  Glamdring Incorporated Enterprises, Inc.  All rights reserved.

 */
public class StringCell extends jdbcnav.app.kit.excel.CellImpl<String> {
    public StringCell(HSSFCell cell) {
        super(cell);
    }

    public Serializable getValue() {
        return cell.getStringCellValue();
    }
}
