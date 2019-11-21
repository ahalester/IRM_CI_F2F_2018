package alma.aat.archive.db;

import alma.aat.archive.web.utils.PropertiesFileUtil;
import org.slf4j.Logger;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.slf4j.LoggerFactory.getLogger;

public class DbHelper extends AbstractDbHelper {

    private static final Logger LOG = getLogger(DbHelper.class);
    private static DbHelper instance;
    private static String testPhase = System.getProperty("testPhase");

    private DbHelper() {
    }

    public static synchronized DbHelper getInstance() {
        if (instance == null) {
            // Workaround for MacOS/Gradle problem loading jar file at build time
            try {
                File myJar = new File("libs/ojdbc8.jar");
                URL url = myJar.toURI().toURL();

                Class[] parameters = new Class[]{URL.class};

                URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
                Class sysClass = URLClassLoader.class;
                Method method = sysClass.getDeclaredMethod("addURL", parameters);
                method.setAccessible(true);
                method.invoke(sysLoader, new Object[]{url});

            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }

            instance = new DbHelper();
        }
        return instance;
    }

    protected Connection getConnection() {
        String connUrl = PropertiesFileUtil.getProperty("configuartion.properties",testPhase + ".db.connection.url");
        String user = PropertiesFileUtil.getProperty("configuartion.properties",testPhase + ".db.schema.name");
        String password = PropertiesFileUtil.getProperty("configuartion.properties",testPhase + ".db.schema.password");

        Connection connection = null;
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            connection = DriverManager.getConnection(connUrl, user, password);
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            LOG.error("Connection Failed! Check output console");
            LOG.error(e.getMessage());
        } catch (ClassNotFoundException cl) {
            LOG.error("DB driver not found");
            cl.printStackTrace();
        }

        LOG.info("Create connection to " + connUrl + " using user: " + user);
        return connection;
    }

}
