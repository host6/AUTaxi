package core.db;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Admin on 25.04.2015.
 *
 */
public class Sql {

    static final Logger log = LogManager.getLogger(Sql.class);

    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    private static <T> T execute(SqlExecutor<T> executor) {
        Connection con = null;
        Connection txConn = DB.getCurrentConn();
        try {
            if (txConn == null) {
                con = DB.getConnection();
            }
            return executor.run((txConn == null) ? con : txConn);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (txConn == null) {
                    if (null != con) {
                        con.close();
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    Connection conn = null;

    private static <T> T query(Connection conn, final String sql, final ResultSetHandler<T> rsh, final Object... params) throws SQLException {
        return QUERY_RUNNER.query(conn, sql, rsh, params);
    }

    public static <T> T query(final String sql, final ResultSetHandler<T> rsh, final Object... params) {
        return execute(new SqlExecutor<T>() {
            @Override
            public T run(Connection conn) throws SQLException {
                return query(conn, sql, rsh, params);
            }
        });
    }

    public static <T> T queryValue(final String sql, final Object... params) throws SQLException {
        return query(sql, new ScalarHandler<T>(), params);
    }

    public static Long exec(final String sql, final Object... params) {
        return execute(new SqlExecutor<Long>() {
            @Override
            public Long run(Connection conn) throws SQLException {
                QUERY_RUNNER.update(conn, sql, params);
                BigDecimal ident = queryValue("SELECT TOP(1) @@IDENTITY FROM ENums;");
                if (null != ident) {
                    return ident.longValue();
                } else {
                    return null;
                }
            }
        });
    }

    public static void beginTran() {
        DB.beginTran();
    }

    public static void commit() {
        DB.commit();
    }

    public static void rollback() {
        DB.rollback();
    }
}
