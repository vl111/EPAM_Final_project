package controller.Database;

import controller.exceptions.MaximumPoolSizeException;
import controller.resource_loader.ResourceLoader;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {

    private static final Logger LOG = Logger.getLogger(ConnectionPool.class.getSimpleName());
    private volatile static ConnectionPool connectionPool = null;
    private final int MAX_POOL_SIZE = Integer.valueOf(ResourceLoader
            .getProperyByKey("maxPoolSize").toString());

    private volatile List<DB_Connection> usedConnections;
    private volatile List<DB_Connection> connections;

    private ConnectionPool() {
        usedConnections = new ArrayList<>();
        connections = new ArrayList<>();
        for (int i = 0; i < MAX_POOL_SIZE; i++) {
            connections.add(new DB_Connection());
            connections.get(connections.size() - 1).openConnection();
        }
    }

    public static ConnectionPool getConnectionPool() {
        if (connectionPool == null)
            synchronized (ConnectionPool.class) {
                if (connectionPool == null) {
                    connectionPool = new ConnectionPool();
                    LOG.info("New connectionPool of " + ConnectionPool.class.getSimpleName()
                            + " created.");
                }
            }
        return connectionPool;
    }

    public DB_Connection getConnection() throws MaximumPoolSizeException {

        synchronized (connectionPool) {
            if (!(usedConnections.size() < MAX_POOL_SIZE)) {
                String message = "Maximum pool size reached, no available connections";
                LOG.error(message);
                throw new MaximumPoolSizeException(message);
            }

            DB_Connection connection = connections
                    .remove(connections.size() - 1);
            usedConnections.add(connection);
            return connection;
        }
    }

    public void retrieveConnection(DB_Connection conn) {
        synchronized (connectionPool) {
            usedConnections.remove(conn);
            connections.add(conn);
        }
    }


}
