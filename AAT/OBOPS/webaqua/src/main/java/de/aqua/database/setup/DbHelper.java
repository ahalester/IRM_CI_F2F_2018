package de.aqua.database.setup;

import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.slf4j.LoggerFactory.getLogger;

public class DbHelper extends AbstractDbHelper {

    private static final Logger LOG = getLogger(DbHelper.class);
    private static DbHelper instance;

    private DbHelper() {
    }

    public static synchronized DbHelper getInstance() {
        if (instance == null) {
            instance = new DbHelper();
        }
        return instance;
    }

    protected Connection getConnection() {
        String connUrl = DbConfiguration.DB_CONNECTION_URL; //PropertiesFileUtil.getNavigationURL("db_connection_url");
        String strUserID = DbConfiguration.USER; //PropertiesFileUtil.getProperty("db_user");
        String strPassword = DbConfiguration.PASSWORD; //EncryptUtil.decrypt(PropertiesFileUtil.getProperty("db_password"));
        Connection connection = null;
        LOG.info("Create connection to " + connUrl);
        LOG.info("Login: " + strUserID);
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            connection = DriverManager.getConnection(connUrl, strUserID, strPassword);
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

}
