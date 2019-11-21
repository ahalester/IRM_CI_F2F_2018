package alma.aat.common.db.utils;

import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.slf4j.LoggerFactory.getLogger;

public class DbHelper extends AbstractDbHelper {

    private static final Logger LOG = getLogger(DbHelper.class);
    private static volatile DbHelper instance;

    protected String url;
    protected String user;
    protected String password;

//    private DbHelper() {
//        super.init();
//    }

    private DbHelper(String url, String user, String password) {
        super.init(url, user, password);
        this.url = url;
        this.user = user;
        this.password = password;

    }

    public static synchronized DbHelper getInstance(String url, String user, String password) {
        if (instance == null) {
            instance = new DbHelper(url, user, password);
        }
        return instance;
    }

//    public static synchronized DbHelper getInstance() {
//        if (instance == null) {
//            instance = new DbHelper();
//        }
//        return instance;
//    }

//    protected Connection getConnection() {
//        Connection connection = null;
//        LOG.info("Create connection to ##" + this.url);
//        LOG.info("Login: " + this.user);
//        try {
//            Class.forName("oracle.jdbc.OracleDriver");
//            connection = DriverManager.getConnection(this.url, this.user, this.password);
//            connection.setAutoCommit(true);
//        } catch (SQLException e) {
//            LOG.error("Connection Failed! Check output console");
//            LOG.error(e.getMessage());
//        } catch (ClassNotFoundException cl) {
//            LOG.error("DB driver not found");
//            cl.printStackTrace();
//        }
//        return connection;
//    }

    protected Connection getConnection(String url, String user, String password) {
        Connection connection = null;
        LOG.info("Create connection to " + url);
        LOG.info("Login: " + user);
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            LOG.error("Connection Failed! Check output console");
            LOG.error(e.getMessage());
        } catch (ClassNotFoundException cl) {
            LOG.error("DB driver not found");
            cl.printStackTrace();
        }
        return connection;
    }

//    public void closeConnection(Connection conn) throws SQLException {
//        conn.close();
//    }

}
