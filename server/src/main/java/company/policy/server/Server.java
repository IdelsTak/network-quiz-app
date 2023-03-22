package company.policy.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private static final Logger LOG = Logger.getLogger(Server.class.getName());

//    public static void main(String[] args) throws Exception {
//        int port = 8080;
//
//        ServerSocket serverSocket = new ServerSocket(port);
//        LOG.log(Level.INFO, "Server started on port {0}", serverSocket);
//
//        ExecutorService executorService = Executors.newFixedThreadPool(100);
//        Map<UUID, Socket> map = new ConcurrentHashMap<>();
//
////        new Thread(() -> {
//            while (true) {
//                try {
//                    Socket socket = serverSocket.accept();
//
//                    LOG.log(Level.INFO, "Connected: {0}", socket);
//
//                    UUID id = UUID.randomUUID();
//
//                    map.put(id, socket);
//
//                    executorService.submit(() -> {
//                            try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
//
//                                Object read = in.readObject();
//
//                                LOG.log(Level.INFO, "Read: {0}", read);
//
//                                List<Socket> sockets = map.entrySet().stream().filter(e -> !Objects.equals(e.getKey(), id)).map(Entry::getValue).toList();
//
//                                synchronized (sockets) {
//                                    for (Socket otherSocket : sockets) {
//                                        try (ObjectOutputStream out = new ObjectOutputStream(otherSocket.getOutputStream())) {
//                                            out.writeObject(read);
//                                        }
//                                    }
//
//                                    map.clear();
//                                }
//                            } catch (IOException | ClassNotFoundException ex) {
//                                LOG.log(Level.SEVERE, null, ex);
//                            }
//                    });
//                } catch (IOException ex) {
//                    LOG.log(Level.SEVERE, null, ex);
//                }
//            }
////        }).start();
//
//    }
    public static void main(String[] args) throws Exception {
        int port = 8080;

        ServerSocket serverSocket = new ServerSocket(port);
        LOG.log(Level.INFO, "Server started on port {0}", serverSocket);

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        Map<UUID, Socket> map = new ConcurrentHashMap<>();

        while (true) {
            try {
                Socket socket = serverSocket.accept();

                LOG.log(Level.INFO, "Connected: {0}", socket);

                UUID id = UUID.randomUUID();

                map.put(id, socket);

                executorService.submit(() -> {
                    try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                        Object read = in.readObject();

                        LOG.log(Level.INFO, "Read: {0}", read);

                        List<Socket> sockets = map.entrySet().stream()
                                .filter(e -> !Objects.equals(e.getKey(), id))
                                .map(Entry::getValue)
                                .toList();

                        for (Socket otherSocket : sockets) {
                            try {
                                ObjectOutputStream out = new ObjectOutputStream(otherSocket.getOutputStream());
                                out.writeObject(read);
                                out.flush();
                            } catch (IOException ex) {
                                LOG.log(Level.SEVERE, null, ex);
                            }
                        }

                        map.remove(id);
                    } catch (IOException | ClassNotFoundException ex) {
                        LOG.log(Level.SEVERE, null, ex);
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException ex) {
                            LOG.log(Level.SEVERE, null, ex);
                        }
                    }
                });
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }

}
