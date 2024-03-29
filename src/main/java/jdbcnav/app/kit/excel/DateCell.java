package jdbcnav.app.kit.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;

import java.io.Serializable;
import java.util.Date;

/**
 * (c) Copyright 2006  Glamdring Incorporated Enterprises, Inc.  All rights reserved.

 */
public class DateCell extends CellImpl<Date> {
    public DateCell(HSSFCell cell) {
        super(cell);

        if (!(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)) {
            throw new IllegalArgumentException(cell.getStringCellValue());
        }
    }

    public Serializable getValue() {
        return cell.getDateCellValue();
    }
}
