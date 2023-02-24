package jpabook.jpashop.socket;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TcpConnection implements Connection {
    private InputStream inputStream;
    private OutputStream outputStream;
    private Socket socket;
    private List<Listener> listeners = new ArrayList<>();
    private BufferedReader br;
    private PrintWriter pw;

    public TcpConnection(Socket socket) {
        this.socket = socket;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public InetAddress getAddress() {
        return socket.getInetAddress();
    }

    @Override
    public void send(Object objectToSend) {
//        if (objectToSend instanceof byte[]) {
//            byte[] data = (byte[]) objectToSend;
//            try {
//                outputStream.write(data);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        br = new BufferedReader(new InputStreamReader(inputStream));
        pw = new PrintWriter(outputStream);
        pw.println(objectToSend);
        pw.flush();
    }

    @Override
    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void start() {
        new Thread(() -> {
            while (true) {
                byte buf[] = new byte[20];
                try {
                    int count = inputStream.read(buf);
                    if (count > 0) {
                        byte[] bytes = Arrays.copyOf(buf, count);
                        for (Listener listener : listeners) {
                            listener.messageReceived(this, bytes);
                        }
                    } else {
                        socket.close();
                        for (Listener listener : listeners) {
                            listener.disconnected(this);
                        }
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    for (Listener listener : listeners) {
                        listener.disconnected(this);
                    }
                    break;
                }
            }
        }).start();
    }

    @Override
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
