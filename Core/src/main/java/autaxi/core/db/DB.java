package autaxi.core.db;

/**
 * Created by Admin on 25.04.2015.
 *
 */

import autaxi.core.config.ConfigFactory;
import autaxi.core.config.sources.IConfigSource;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private static DBSettings dbs = null;
    private static DataSource ds = null;

    private static final ThreadLocal<Connection> TRANSACT_CONN = new ThreadLocal<Connection>();

    private static GenericObjectPool connectionPool = null;

    public static void setUp(IConfigSource cfgSource, int PoolSize) {
        try {
            if (dbs == null) {
                dbs = ConfigFactory.getFromSource(DBSettings.class, cfgSource);
            }
            DriverManager.registerDriver((Driver) Class.forName(
                    dbs.getDbDriver()).newInstance());
            connectionPool = new GenericObjectPool();
            connectionPool.setMaxActive(PoolSize);

            ConnectionFactory cf = new DriverManagerConnectionFactory(dbs.getUrl(), dbs.getUser(), dbs.getPassword());

            PoolableConnectionFactory pcf = new PoolableConnectionFactory(cf, connectionPool, null, null, false, true);
            ds = new PoolingDataSource(connectionPool);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getCurrentConn() {
        return TRANSACT_CONN.get();
    }

    public static  void beginTran()  {
        Connection conn = TRANSACT_CONN.get();
        if (conn != null) {
            throw new RuntimeException("Start second transaction in one thread");
        }
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
        } catch (SQLException e) {
           throw new RuntimeException(e);
        }
        TRANSACT_CONN.set(conn);
    }

    public static void commit() {
        try {
            if (TRANSACT_CONN.get() != null) {
                TRANSACT_CONN.get().commit();
                TRANSACT_CONN.get().close();
                TRANSACT_CONN.set(null);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void rollback() {
        try {
            if (TRANSACT_CONN.get() != null) {
                TRANSACT_CONN.get().rollback();
                TRANSACT_CONN.get().close();
                TRANSACT_CONN.set(null);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
