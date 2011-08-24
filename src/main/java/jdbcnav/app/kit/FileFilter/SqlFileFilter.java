package jdbcnav.app.kit.FileFilter;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created by IntelliJ IDEA for Jdbcnav.com
 * User: jim
 * Date: Apr 5, 2007
 * Time: 7:25:04 PM
 * <p/>
 */
public final class SqlFileFilter extends FileFilter {
    private static final SqlFileFilter instance = new SqlFileFilter();


    public boolean accept(File file) {
        return file.getName().endsWith(".sql") || file.isDirectory();
    }

    public String getDescription() {
        return "SQL Sauce";
    }

    public static SqlFileFilter createSqlFileFilter() {
        return instance;
    }

    public static FileFilter getInstance() {
        return instance;
    }
}
