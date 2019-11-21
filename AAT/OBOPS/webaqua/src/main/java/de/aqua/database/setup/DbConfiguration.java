package de.aqua.database.setup;

import de.aqua.web.utils.EncryptUtil;
import de.aqua.web.utils.PropertiesFileUtil;

public abstract class DbConfiguration {

    public static final String DB_CONNECTION_URL = PropertiesFileUtil.getNavigationURL("db_connection_url");
    public static final String USER = PropertiesFileUtil.getProperty("db_user");
    public static final String PASSWORD = EncryptUtil.decrypt(PropertiesFileUtil.getProperty("db_password"));

}
