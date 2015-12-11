package core.db;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Admin on 25.04.2015.
 *
 */
public interface SqlExecutor<T> {
    T run(Connection conn) throws SQLException;
}
