package jdbcnav.app.kit.FileFilter;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by IntelliJ IDEA for Jdbcnav.com
 * User: jim
 * Date: Apr 9, 2007
 * Time: 8:54:28 PM
 * <p/>


 */
public final class IdFileFilter extends FileFilter implements FilenameFilter {
    public boolean accept(File file) {
        return !file.isFile() || file.getName().startsWith("id_");
    }

    public String getDescription() {
        return "openSSH keyfile";
    }

    private static final FilenameFilter instance = (FilenameFilter) new IdFileFilter();

    public static FilenameFilter getInstance() {
        return instance;
    }

    public boolean accept(File dir, String name) {
        File file = new File(dir, name);
        return accept(file);
    }

}
