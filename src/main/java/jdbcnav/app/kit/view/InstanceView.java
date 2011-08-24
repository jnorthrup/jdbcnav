package jdbcnav.app.kit.view;

import jdbcnav.app.kit.JdbcNav;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;

/**
 * (c) Copyright 2006  Glamdring Incorporated Enterprises, Inc.  All rights reserved.

 */
class InstanceView {
    InstanceView(Rectangle rectangle) {
        try {
            JInternalFrame iframe = new JInternalFrame();
            JdbcNav.prepIFrame(iframe, "Instance", new JScrollPane(new JTree()), rectangle);

        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }
}
