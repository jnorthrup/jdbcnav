package jdbcnav.app.kit.excel;

import java.io.Serializable;

/**
 * (c) Copyright 2006  Glamdring Incorporated Enterprises, Inc.  All rights reserved.

 */
public interface Cell {
    String toString();

    Serializable getValue();
}
