package WSD.farsnet.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

public class SqlLiteDbUtility {
    private static Connection connection = null;

    public SqlLiteDbUtility() {
    }

    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        } else {
            try {
                File dbfile = new File(".");
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + dbfile.getAbsolutePath() + "/farsnet3.sqlite3");
                System.out.println(dbfile.getAbsolutePath());
            } catch (Exception var1) {
                var1.printStackTrace();
            }

            return connection;
        }
    }
}
