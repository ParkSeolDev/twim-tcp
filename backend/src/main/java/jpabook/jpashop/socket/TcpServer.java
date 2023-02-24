package jpabook.jpashop.socket;

import jpabook.jpashop.api.service.ItemService;
import jpabook.jpashop.db.entity.item.Item;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Component
public class TcpServer implements Server, Connection.Listener {
    private static Log logger = LogFactory.getLog(TcpServer.class);

    private ServerSocket serverSocket;
    private volatile boolean isStop;
    private List<Connection> connections = new ArrayList<>();
    private List<Connection.Listener> listeners = new ArrayList<>();

    @Autowired
    private ItemService itemService;

    public void setPort(Integer port) {
        try {
            if (port == null) {
                logger.info("Property tcp.server.port not found. Use default port 9999");
                port = 9999;
            }
            serverSocket = new ServerSocket(port);
            logger.info("Server start at port " + port);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("May be port " + port + " busy.");
        }
    }

    @Override
    public int getConnectionsCount() {
        return connections.size();
    }

    @Override
    public void start() {
        new Thread(() -> {
            while (!isStop) {
                try {
                    Socket socket = serverSocket.accept();
                    if (socket.isConnected()) {
                        TcpConnection tcpConnection = new TcpConnection(socket);
                        tcpConnection.start();
                        tcpConnection.addListener(this);
                        connected(tcpConnection);
                        while (true){
                            Thread.sleep(1000);
                            List<Item> items = itemService.findItems();
                            tcpConnection.send(items);
                            System.out.println("보내는중");
                        }
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void stop() {
        isStop = true;
    }

    @Override
    public List<Connection> getConnections() {
        return connections;
    }

    @Override
    public void addListener(Connection.Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void messageReceived(Connection connection, Object message) {
        logger.trace("Received new message from " + connection.getAddress().getCanonicalHostName());
        logger.trace("Class name: " + message.getClass().getCanonicalName() + ", toString: " + message.toString());
        for (Connection.Listener listener : listeners) {
            listener.messageReceived(connection, message);
        }
    }

    @Override
    public void connected(Connection connection) {
        logger.info("New connection! Ip: " + connection.getAddress().getCanonicalHostName() + ".");
        connections.add(connection);
        logger.info("Current connections count: " + connections.size());
        for (Connection.Listener listener : listeners) {
            listener.connected(connection);
        }
    }

    @Override
    public void disconnected(Connection connection) {
        logger.info("Disconnect! Ip: " + connection.getAddress().getCanonicalHostName() + ".");
        connections.remove(connection);
        logger.info("Current connections count: " + connections.size());
        for (Connection.Listener listener : listeners) {
            listener.disconnected(connection);
        }
    }
}