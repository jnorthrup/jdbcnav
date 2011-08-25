package jdbcnav.app.kit;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import jdbcnav.app.kit.FileFilter.IdFileFilter;
import jdbcnav.app.util.Pair;
import virtuoso.jdbc4.Driver;

/**
 * Created by IntelliJ IDEA.
 * User: jim
 * Date: Mar 13, 2007
 * Time: 3:28:55 PM
 */
public final class PSqlChannel {

public static String  JDBC_DRIVER_PREFIX = System.getProperty("jdbc.prefix", System.getenv("JDBC_PREFIX"));  //env
public static String DB_HOST = System.getProperty("db.host", System.getenv("DB_HOST"));                      //env
public static String  DB_USER = System.getProperty("db.user", System.getenv("DB_USER"));                     //env
public static String  DB_PASSWORD = System.getProperty("db.password", System.getenv("DB_PASSWORD"));         //env
public static Integer DB_PORT = Integer.parseInt(System.getProperty("db.port", "DB_PORT"));
public static String  SSH_PASSWORD = System.getProperty("ssh.password", System.getenv("SSH_PASSWORD"));      //env
public static String  SSH_USER = System.getProperty("ssh.user", System.getenv("USER"));                      //env
public static String SSH_HOST = System.getProperty("ssh.host", System.getenv("SSH_HOST"));                 //env
public static Integer TUNNEL_PORT = Integer.parseInt(System.getProperty("tunnel.port", "TUNNEL_PORT"));         //env
// -Ddb.port=
// -Ddb.host=
// -Ddb.user=
// -Ddb.password=
// -Djdbc.prefix=
// -Dtunnel.port=
// -Dssh.host=
// -Dssh.password=
// -Dssh.user=


                                                                           

  static final String JDBCNAV_PRODUCTION_USER_JDBCNAV = "/";
  static java.sql.Connection sqlConnection;
  static final String HTML_PROMPT_MSG = "<html>" + "<img src='http://app.com/podcaster/jdbcnav.app.dws.images/img_demo.gif'>" + "<p>Please Enter<blink> SSH </blink>Username.\n";
  static Connection sshConnection;

  private static boolean auth;

  static Process process;

  private static Pair<String, String> getAuthInfo() {
    final ButtonGroup group = new ButtonGroup();

    Pair<String, String> stuff;

    System.out.println(System.getProperties());
    JLabel nameLabel = new JLabel(HTML_PROMPT_MSG);
    String user = SSH_USER;
    JTextField nameField = new JTextField(user);
//Group the radio buttons.
    JToolBar bbar = new JToolBar();
    bbar.setOrientation(JToolBar.VERTICAL);
    bbar.setFloatable(false);

    for (final AuthMethods s : AuthMethods.values()) {
      JRadioButton radioButton = new JRadioButton(s.getPrompt()) {
        {
          setActionCommand(s.name());
        }
      };
      group.add(radioButton);
      bbar.add(radioButton);
    }


    JPanel namePanel = new JPanel(new BorderLayout());
    namePanel.add(nameLabel, BorderLayout.NORTH);
    namePanel.add(nameField, BorderLayout.CENTER);
    namePanel.add(bbar, BorderLayout.SOUTH);
    JOptionPane.showMessageDialog(null, namePanel, "SSH User ID", JOptionPane.PLAIN_MESSAGE);

    stuff = new Pair<String, String>(nameField.getText(), group.getSelection().getActionCommand());
    return stuff;
  }

  private static java.sql.Connection createSqlConnection() throws SQLException {

//      setSqlConnection(JdbcNav.getDriver().connect(getDburi(), new Properties()));
    Driver.main(new String[0]);
    String dbuser = DB_USER;
    String dbpassword = DB_PASSWORD;
    setSqlConnection(DriverManager.getConnection(getDburi() + "/", dbuser, dbpassword));
    Statement statement = sqlConnection.createStatement();
    boolean execute = statement.execute("USE " + dbuser);


    sqlConnection.setReadOnly(true);

    return sqlConnection;
  }

  public static String getDburi() {
    String JDBC_PREFIX = JDBC_DRIVER_PREFIX;
    return JdbcNav.getArgs().length > 0 ? JdbcNav.getArgs()[0] : JDBC_PREFIX /*+ JDBC_HOST + ":" + JDBC_PORT + JDBC_LOCATION*/;
  }

  private static void sshTunnel(boolean hookMeUpDawg, File sshDir) throws IOException, InterruptedException {
    sshConnection.setTCPNoDelay(false);
    sshConnection.createLocalPortForwarder(TUNNEL_PORT, DB_HOST, DB_PORT);
    Session session = sshConnection.openSession();
    new StreamGobbler(session.getStdout());
    new StreamGobbler(session.getStderr());
    if (hookMeUpDawg && auth) {
      JOptionPane.showConfirmDialog(null, new JLabel("<html><b>This authorization did succeed.</b><p>attempting to grab your keys."));
      SCPClient scpClient = new SCPClient(sshConnection);
      scpClient.get(new String[]{".ssh/id_dsa", ".ssh/id_dsa.pub", ".ssh/id_rsa", ".ssh/id_rsa.pub"}, sshDir.getAbsolutePath());
    }
    Class<?> aClass;
    aClass = PSqlChannel.class;
    synchronized (aClass) {
      aClass.wait();
    }
  }


  private static void passLogin(String username) throws IOException {
    JPasswordField passwordField = new JPasswordField();
    JOptionPane.showConfirmDialog(null, passwordField, "", JOptionPane.DEFAULT_OPTION);
    auth = sshConnection.authenticateWithPassword(username, String.valueOf(passwordField.getPassword()));
  }

  public static ResultSet executeSql(String sql) throws IOException, InterruptedException {
    while (true) {
      Statement statement;
      ResultSet resultSet = null;
      try {
        getSqlConnection().setAutoCommit(false);
        statement = getSqlConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        JdbcNav.getLogger().info("sql: " + sql);
        statement.setFetchDirection(ResultSet.FETCH_UNKNOWN);
        statement.setFetchSize(100);
        resultSet = statement.executeQuery(sql);
      } catch (SQLException e) {
        int ok = doSqlErrorMessage(e);
        if (ok == 0) try {


          getSqlConnection().close();
          createConnection();

          continue;

        } catch (SQLException e1) {
          e1.printStackTrace();
        }
      }
      return resultSet;
    }
  }

  public static int doSqlErrorMessage(SQLException e) {
    int errorCode = e.getErrorCode();
    String message = e.getMessage();
    String sqlState = e.getSQLState();
    StringWriter stringWriter = new StringWriter();
    e.printStackTrace(new PrintWriter(stringWriter));
    return JOptionPane.showConfirmDialog(null, ":" + errorCode + ':' + message + ':' + sqlState + ':' + stringWriter.toString(), "SQLException", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
  }

  public static void guardConnection() throws IOException, InterruptedException {
    try {
      if (!(getSqlConnection() != null || !getSqlConnection().isClosed())) createConnection();
    } catch (SQLException e) {
      if (doSqlErrorMessage(e) == 1) {
        guardConnection();
      }
    }
  }

  public static java.sql.Connection getSqlConnection() {
    try {
      if (null == sqlConnection || sqlConnection.isClosed())sqlConnection= createConnection();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return sqlConnection;
  }

  public static void setSqlConnection(java.sql.Connection sqlConnection) {
    PSqlChannel.sqlConnection = sqlConnection;
    try {
      sqlConnection.setReadOnly(true);
    } catch (SQLException e) {
      StringBuilder builder = new StringBuilder();

      do {
        builder.append(MessageFormat.format("{0}:{1} -- {2}\n", e.getErrorCode(), e.getSQLState(), e.getLocalizedMessage()));

        e = e.getNextException();

      } while (e != null);


      throw new Error(builder.toString());
    }
  }


  enum AuthMethods {
    NO_AUTH("just go!"),
    DSA("select your openssh id_dsa or id_rsa file. look in $HOME/.ssh/"),
    PASSWORD("enter your app unix server password."),
    CYGWIN("Cygwin SSH-key file will be used instead of a password.(last resort)");
    private String prompt;

    AuthMethods(String prompt) {
      this.prompt = prompt;
    }

    private String getPrompt() {
      return prompt;
    }
  }

  static java.sql.Connection createConnection() throws InterruptedException, IOException, SQLException {


    if (!auth) {


      boolean hookMeUpDawg;
      {
        Pair<String, String> stuff = getAuthInfo();


        Thread runJavaSsh;
        String[] strings;
        strings = new String[]{"ssh", "-N", "-L" + DB_PORT + ":" + DB_HOST + ":" + TUNNEL_PORT + stuff.getFirst() + '@' + SSH_HOST};
        ProcessBuilder pb;
        pb = new ProcessBuilder(strings);

        AuthMethods cmd;
        cmd = null;
        String command = stuff.getSecond();
        for (AuthMethods authMethod : AuthMethods.values()) {
          if (command.equals(authMethod.name())) {
            cmd = authMethod;
            break;
          }
        }
        hookMeUpDawg = false;
        File homedir = new File(System.getProperty("user.home"));
        final File sshDir = new File(homedir, ".ssh");
        File[] files = sshDir.listFiles(IdFileFilter.getInstance());


        switch (cmd) {
          case CYGWIN: {
            if (process != null) process.destroy();
            JdbcNav.getLogger().info(pb.toString());

            process = pb.start();
            Runtime.getRuntime().addShutdownHook(new Thread() {
              public void run() {
                try {
                  process.getErrorStream().close();
                  process.getInputStream().close();
                  process.getOutputStream().close();
                } catch (IOException e) {
                  e.printStackTrace();
                }
                process.destroy();
                JdbcNav.getLogger().info("background process has been terminated");
              }
            });
            Thread.sleep(10000);
          }
          break;

          case DSA:
            hookMeUpDawg = hookMeUpDawg(hookMeUpDawg, sshDir);

            File dsafile;
            if (null == files || files.length < 1) {
              JFileChooser pubkeyChooser = new JFileChooser("please select the public key id_dsa.pub");
              pubkeyChooser.setCurrentDirectory(sshDir.getAbsoluteFile());
              pubkeyChooser.setVisible(true);

              if (JFileChooser.APPROVE_OPTION == pubkeyChooser.showOpenDialog(null)) {
                dsafile = pubkeyChooser.getSelectedFile();
                auth = sshConnection.authenticateWithPublicKey(stuff.getFirst(), dsafile, null);
              }
            } else
//                            dsafile = files[0];

              RunThread(hookMeUpDawg, sshDir);
            break;

          case PASSWORD:
            hookMeUpDawg(hookMeUpDawg, sshDir);
            passLogin(stuff.getFirst());
            if (!auth) {
              JOptionPane.showConfirmDialog(null, new JLabel("<html><b>This authorization did not succeed.</b><p>one more chance."));
              passLogin(stuff.getFirst());

            }
            RunThread(hookMeUpDawg, sshDir);
            break;
          case NO_AUTH:
            auth = true;

          default:
            DB_PORT = TUNNEL_PORT;
            DB_HOST = "tron.app.com";
            break;
        }

        if (!auth) {
          JOptionPane.showConfirmDialog(null, new JLabel("<html><b>This authorization did not succeed.</b><p>Exiting."));
          System.exit(1);
        } else return createSqlConnection();
      }
    }
    return getSqlConnection();
  }

  private static void RunThread(final boolean hookMeUpDawg, final File sshDir) {
    Thread runJavaSsh;
    runJavaSsh = new Thread() {

      public void run() {
        try {
          sshTunnel(hookMeUpDawg, sshDir);
        } catch (IOException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
          e.printStackTrace();  //is this informative enough?
        }
      }
    };
    runJavaSsh.start();
  }

  private static boolean hookMeUpDawg(boolean hookMeUpDawg, File sshDir) {
    if (!sshDir.exists()) {
      hookMeUpDawg = true;
      if (!sshDir.mkdirs()) new Error("couldn't create $HOME/.ssh and you don't have one!!");
      JOptionPane.showMessageDialog(null, new JLabel("<html><b>Hookin you up dawg!<p>we have created " + sshDir.getAbsolutePath()));
    }
    sshConnection = new Connection(SSH_HOST);
    try {
      sshConnection.connect();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return hookMeUpDawg;
  }
}