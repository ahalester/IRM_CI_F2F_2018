package cl.apa.database.setup;

import org.flywaydb.core.internal.util.jdbc.JdbcTemplate;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class AbstractDbHelper {

    private static final String ERROR_MESSAGE = "Invalid SQL syntax! ";
    private static final String LOG_MESSAGE = "Execute query - ";
    private static final Logger LOG = getLogger(AbstractDbHelper.class);
    private JdbcTemplate template;

    public AbstractDbHelper() {
        init();
    }

    private JdbcTemplate getJdbcTemplate() {
        return template;
    }

    public List<Map<String, String>> getRecordList(String query) {
        try {
            List<Map<String, String>> result = getJdbcTemplate().queryForList(query);
            LOG.info(LOG_MESSAGE + query);
            return result;
        } catch (SQLException e) {
            LOG.error(ERROR_MESSAGE + query);
            LOG.error(e.getMessage());
        }
        return null;
    }

    public Map<String, String> getSingleRecord(String query) {
        try {
            Map<String, String> result = getJdbcTemplate().queryForList(query).get(0);
            LOG.info(LOG_MESSAGE + query);
            return result;
        } catch (SQLException e) {
            LOG.error(ERROR_MESSAGE + query);
            LOG.error(e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            LOG.error("Empty response! " + query);
            LOG.error(e.getMessage());
        }
        return null;
    }

    public String getSingleValue(String query) {
        try {
            Map<String, String> resultMap = getJdbcTemplate().queryForList(query).get(0);
            String columnName = query.trim().split(" ")[1];
            if (columnName.contains("(")) {
                //get column name if there is any sorting
                columnName = columnName.split("\\(")[0];
            }
            String result = resultMap.get(columnName);
            LOG.info(LOG_MESSAGE + query + " ---> " + result);
            return result;
        } catch (SQLException e) {
            LOG.error(ERROR_MESSAGE + query);
            LOG.error(e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            LOG.error("Empty response! " + query);
            LOG.error(e.getMessage());
        }
        return null;
    }

    public void executeQuery(String query, Object... params) {
        try {
            getJdbcTemplate().update(query, params);
        } catch (SQLException e) {
            LOG.error(ERROR_MESSAGE + query);
            LOG.error(e.getMessage());
        }
    }

    public void init() {
        this.template = new JdbcTemplate(getConnection(), 0);
    }

    protected abstract Connection getConnection();
}
