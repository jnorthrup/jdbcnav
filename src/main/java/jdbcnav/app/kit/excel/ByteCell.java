package jdbcnav.app.kit.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;

import java.io.Serializable;

/**
 * (c) Copyright 2006  Glamdring Incorporated Enterprises, Inc.  All rights reserved.

 */
public class ByteCell extends CellImpl<Byte> {
    public ByteCell(HSSFCell cell) {
        super(cell);
    }

    public Serializable getValue() {
        return cell.getNumericCellValue();
    }
}
