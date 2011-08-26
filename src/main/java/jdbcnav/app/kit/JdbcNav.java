package jdbcnav.app.kit;

import jdbcnav.app.kit.view.*;
import jdbcnav.app.usecase.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 * (c) Copyright 2006  Glamdring Incorporated Enterprises, Inc.  All rights reserved.
 * -Ddb.port=
 * -Ddb.host=
 * -Ddb.user=
 * -Ddb.password=
 * -Djdbc.prefix=
 * -Dtunnel.port=
 * -Dssh.host=
 * -Dssh.password=
 * -Dssh.user=
 */
public final class JdbcNav {
    private static String[] args;
    private static final boolean running = true;

    private static Logger logger = Logger.getAnonymousLogger();
  public static String  JDBC_DRIVER_PREFIX = System.getProperty("jdbc.prefix", System.getenv("JDBC_PREFIX"));  //env
  public static String DB_HOST = System.getProperty("db.host", System.getenv("DB_HOST"));                      //env
  public static String DB_NAME= System.getProperty("db.name", System.getenv("DB_NAME"));                      //env
  public static String  DB_USER = System.getProperty("db.user", System.getenv("DB_USER"));                     //env
  public static String  DB_PASSWORD = System.getProperty("db.password", System.getenv("DB_PASSWORD"));         //env
  public static Integer DB_PORT = Integer.parseInt(System.getProperty("db.port", "DB_PORT"));
   public static String  SSH_USER = System.getProperty("ssh.user", System.getenv("USER"));                      //env
  public static String SSH_HOST = System.getProperty("ssh.host", System.getenv("SSH_HOST"));                 //env
  public static Integer TUNNEL_PORT = Integer.parseInt(System.getProperty("tunnel.port", "TUNNEL_PORT"));         //env

  public static DefaultTableModel getFormulaModel() {
        return (DefaultTableModel) getFormView().getTable().getModel();
    }

    private static final JDesktopPane desktop = new JDesktopPane();
    private static JFrame frame;

    public static void main(String[] arg) throws PropertyVetoException, IOException, InterruptedException, ClassNotFoundException, UnsupportedLookAndFeelException, IllegalAccessException, InstantiationException {
        args = arg;
//        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        initFrame(null, null);
        desktop.setDragMode(JDesktopPane.LIVE_DRAG_MODE);
    }

    private static void createUsecaseView() throws PropertyVetoException {
        JInternalFrame iframe = new JInternalFrame();
        UseCaseView useCaseView = new UseCaseView();
        JMenuBar bar = UseCaseView.getJMenuBar();
        getDesktop().add(iframe);
        iframe.setTitle("UseCaseGen");
        iframe.setContentPane(useCaseView);
        iframe.setClosable(true);
        iframe.setIconifiable(true);
        iframe.setMaximizable(true);
        iframe.setClosable(true);
        iframe.setResizable(true);
        iframe.setVisible(true);
        iframe.setSelected(true);
        iframe.setBounds(new Rectangle(222, 222, 222, 222));
        iframe.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        iframe.setJMenuBar(bar);
    }


    private static void initFrame(String dbcat, String dbuser) throws PropertyVetoException {
        frame = new JFrame();
        getFrame().setBounds(0, 0, 1024, 768);
        getFrame().setVisible(true);
        getFrame().setResizable(true);
        getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane navTabs = new JTabbedPane();

        MenuBar jmb = new MenuBar();
        getFrame().setMenuBar(jmb);


        Menu jfm = new Menu("File");
        jmb.add(jfm);


        JPanel reportPanel = new JPanel(new BorderLayout());
        navTabs.add("Reports", reportPanel);
        reportPanel.add(getDesktop(), BorderLayout.CENTER);
        JToolBar toolBar = new JToolBar();
        ArrayList<AbstractAction> reportActions = createReportActions(getDesktop());

        for (AbstractAction abstractAction : reportActions) {
            toolBar.add(abstractAction);
            MenuItem menuItem = new MenuItem((String) abstractAction.getValue(Action.NAME));
            menuItem.addActionListener(abstractAction);
            jfm.add(menuItem);
        }
        reportPanel.add(toolBar, BorderLayout.NORTH);

        getFrame().setContentPane(reportPanel);
        getFrame().setMenuBar(jmb);
        new FormulaView(getDesktop());
        try {
            MetaTreeView.createInstanceView(new JInternalFrame(), getDesktop(), dbcat, dbuser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        createUsecaseView();
    }

    private static ArrayList<AbstractAction> createReportActions(final JDesktopPane desktop) {
        ArrayList<AbstractAction> jfm = new ArrayList<AbstractAction>();//ArrayList<AbstractAction>();

        jfm.add(new AbstractAction("Publishers") {

            public void actionPerformed(ActionEvent actionEvent) {
                String sqlPodcasters = "select id,name,email from content_owner  order by email";
                try {
                    DataView.create(sqlPodcasters, desktop);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

        });
        jfm.add(new AbstractAction("Advertisers") {

            public void actionPerformed(ActionEvent actionEvent) {

                String sqlPodcasters = "select id,name,email from ad_account  order by email";

                try {
                    DataView.create(sqlPodcasters, desktop);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        jfm.add(new AbstractAction("All Tables") {
            public void actionPerformed(ActionEvent actionEvent) {
                JInternalFrame iframe = new JInternalFrame();
                try {
                  String dbcat = DB_NAME;
                  String dbuser = DB_USER;
                  MetaTreeView.createInstanceView(iframe, desktop, dbcat, dbuser);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        jfm.add(new AbstractAction("Formula") {
            public void actionPerformed(ActionEvent actionEvent) {
                new FormulaView(desktop);
            }
        });
        return jfm;
    }

    public static void prepIFrame(JInternalFrame iframe, String title, JComponent container, Rectangle rectangle) throws PropertyVetoException {
        iframe.setTitle(title);
        iframe.setContentPane(container);
        iframe.setClosable(true);
        iframe.setIconifiable(true);
        iframe.setMaximizable(true);
        iframe.setClosable(true);
        iframe.setResizable(true);
        iframe.setVisible(true);
        iframe.setSelected(true);
        iframe.setBounds(rectangle);
        iframe.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
    }

    private static FormulaView getFormView() {
        return FormulaView.getInstance();
    }


    public static void log(String entry) {
        logger.info(entry);

    }

    public static Logger getLogger() {
        return logger;
    }

    public static String[] getArgs() {
        return args;
    }

 /*   public static Driver getDriver() throws SQLException {
      String dburi = PSqlChannel.getDburi();
      log("db uri: "+dburi);
      virtuoso.jdbc4.Driver.main(new String[0]      );
      return DriverManager.getDriver(dburi);
    }*/

    public static boolean isRunning() {
        return running;
    }


    public static JDesktopPane getDesktop() {
        return desktop;
    }

    public static JFrame getFrame() {
        return frame;
    }
}
