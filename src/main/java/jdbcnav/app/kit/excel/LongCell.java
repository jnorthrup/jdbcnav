package jdbcnav.app.kit.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;

import java.io.Serializable;

/**
 * (c) Copyright 2006  Glamdring Incorporated Enterprises, Inc.  All rights reserved.

 */
public class LongCell extends CellImpl<Long> {
    public LongCell(HSSFCell cell) {
        super(cell);
        if (cell.getCellType() != HSSFCell.CELL_TYPE_NUMERIC) {
            throw new IllegalArgumentException(cell.getStringCellValue());
        }
    }

    public Serializable getValue() {
        return cell.getNumericCellValue();
    }
}


