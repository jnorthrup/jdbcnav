package jdbcnav.app.kit.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;

import java.io.Serializable;

/**
 * (c) Copyright 2006  Glamdring Incorporated Enterprises, Inc.  All rights reserved.

 */
public class FormulaCell extends CellImpl<Number> {
    public FormulaCell(HSSFCell cl) {
        super(cl);
    }


    public Serializable getValue() {
        return cell.getNumericCellValue();
    }

    public Number getTypedValue() {
        return (Number) getValue();
    }
}
