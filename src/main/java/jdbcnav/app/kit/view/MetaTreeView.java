package jdbcnav.app.kit.view;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import jdbcnav.app.kit.JdbcNav;
import jdbcnav.app.kit.PSqlChannel;

/**
 * (c) Copyright 2006  Glamdring Incorporated Enterprises, Inc.  All rights reserved.
 */
public class MetaTreeView {
  public static void createInstanceView(JInternalFrame iframe, final JDesktopPane desktop, String dbcat, String dbuser) throws PropertyVetoException, IOException, InterruptedException {
    desktop.add(iframe);

    final JTree tree = new JTree();

    JdbcNav.prepIFrame(iframe, "InstanceView", new JScrollPane(tree), new Rectangle(0, 0, 200, desktop.getHeight()));
    DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Dashboard");
    DefaultTreeModel defaultTreeModel = new DefaultTreeModel(rootNode);
    tree.setModel(defaultTreeModel);

    try {

      DatabaseMetaData metaData;
      {
        metaData = PSqlChannel.getSqlConnection().getMetaData();
      }
      ResultSet tables = metaData.getTables(dbcat, dbuser, null,  new String[]{ "TABLE" });
      ResultSetMetaData metaData1 = tables.getMetaData();
//          XStream xStream = new XStream();
//          String s = xStream.toXML(metaData1);
//          System.err.println("md columns: "+s);
      render(rootNode, tables, metaData1);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    //Where the tree is initialized:
    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

    //Listen for when the selection changes.
    tree.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
        TreeNode lastSelectedPathComponent = (TreeNode) tree.getLastSelectedPathComponent();

        if (tree.getModel().getRoot() != lastSelectedPathComponent)
          TablePopupView.createTablePopupView(lastSelectedPathComponent.toString(), desktop, true);
      }
    });
  }

  private static void render(DefaultMutableTreeNode rootNode, ResultSet tables, ResultSetMetaData metaData1) throws SQLException {
    for (int ci = 1; ci < metaData1.getColumnCount() + 1; ci++) {

      if (0 != ci) System.err.print("\t");
      System.err.print(metaData1.getColumnName(ci));

    }
    System.err.println("");
    while (tables.next()) {

      String nm = tables.getString("TABLE_NAME");
      for (int ci = 1; ci < metaData1.getColumnCount() + 1; ci++) {

        if (0 != ci) System.err.print("\t");
        System.err.print(tables.getString(ci));

      }
      System.err.println("");

      rootNode.add(new DefaultMutableTreeNode(nm));
    }
  }
}
