package jdbcnav.app.kit.MetaRef;

import static jdbcnav.app.kit.PSqlChannel.getSqlConnection;
import static jdbcnav.app.kit.PSqlChannel.guardConnection;
import jdbcnav.app.kit.MD_KEYS;

import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;

/**
 * Property of Jdbcnav
 * User: jim
 * Date: Jun 9, 2007
 * Time: 11:29:55 AM
 */
public class TableMetaModel {
    private static final String COLUMN__NAME = "column_name";
    private final List<InboundRef> incomingKeys = new ArrayList<InboundRef>();
    private final List<OutboundRef> outOfContextRefs = new ArrayList<OutboundRef>();
    private final List<MetaDataRef> metaDataRefs = new ArrayList<MetaDataRef>();
    private ResultSet colResultSet;


    private TableMetaModel(String tableName, boolean oid) {
        init(tableName, oid);
    }

    private void init(final String tableName, boolean oid) {
        try {
            guardConnection();
            final DatabaseMetaData metaData = getSqlConnection().getMetaData();
            ResultSet ik = metaData.getImportedKeys(null, null, tableName);
          dump(ik);
          ik = metaData.getImportedKeys(null, null, tableName);//rewind
          ResultSet ob = metaData.getExportedKeys(null, null, tableName);

          dump(ob);
          ob = metaData.getExportedKeys(null, null, tableName);              //rewind

          ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName);
          dump(primaryKeys);
          ResultSet versionColumns = metaData.getVersionColumns(null, null, tableName);
          dump(versionColumns);
          ResultSet columns = metaData.getColumns(null, null, tableName, null);
          dump(columns);

          while (ik.next()) {
                EnumMap<MD_KEYS, String> attrMap = new EnumMap<MD_KEYS, String>(MD_KEYS.class);
                for (MD_KEYS MD : MD_KEYS.values())
                    attrMap.put(MD, ik.getString(MD.name()));
                incomingKeys.add(new InboundRef(attrMap));
            }
            while (ob.next()) {
                EnumMap<MD_KEYS, String> attrMap = new EnumMap<MD_KEYS, String>(MD_KEYS.class);
                for (MD_KEYS MD : MD_KEYS.values())
                    attrMap.put(MD, ob.getString(MD.name()));

                if (attrMap.get(MD_KEYS.PKTABLE_NAME).equals(tableName))
                    outOfContextRefs.add(new OutboundRef(attrMap));
            }


            this.colResultSet = metaData.getColumns(null, null, tableName, null);

            if (oid)
                metaDataRefs.add(new OidColumnRef(tableName));

            while (colResultSet.next())
                metaDataRefs.add(new SimpleColumnRef(tableName, colResultSet.getString(COLUMN__NAME)));

            metaDataRefs.addAll(incomingKeys);
            metaDataRefs.addAll(outOfContextRefs);
        }

        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static TableMetaModel createTableGraph(String tableName, boolean oid) {
        return createTableMetaModel(tableName, oid);
    }

    public ResultSet getColResultSet() {
        return colResultSet;
    }

    public Collection<InboundRef> getIncomingKeyArr() {
        return incomingKeys;
    }

    public Collection<OutboundRef> getOutOfContextArr() {
        return outOfContextRefs;
    }

    static public void main(String[] args) {

    }

    public List<MetaDataRef> getMetaDataRefs() {
        return metaDataRefs;
    }

    public static TableMetaModel createTableMetaModel(String tableName, boolean oid) {
        return new TableMetaModel(tableName, oid);
    }
 private static void dump(ResultSet tables) throws SQLException {
   ResultSetMetaData metaData1 = tables.getMetaData();
   for (int ci = 1; ci < metaData1.getColumnCount() + 1; ci++) {

      if (0 != ci) System.err.print("\t");
      System.err.print(metaData1.getColumnName(ci));

    }
    System.err.println("");
    while (tables.next()) {


      for (int ci = 1; ci < metaData1.getColumnCount() + 1; ci++) {

        if (0 != ci) System.err.print("\t");
        System.err.print(tables.getString(ci));

      }
      System.err.println("");


    }
  }
}
