package company.policy.server;

import java.io.EOFException;
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

                        LOG.log(Level.INFO, "Received object {0} from client {1}", new Object[]{read, socket});

                        List<Socket> sockets = map.entrySet().stream()
                                .filter(e -> !Objects.equals(e.getKey(), id))
                                .map(Entry::getValue)
                                .toList();

                        for (Socket otherSocket : sockets) {
                            ObjectOutputStream out = new ObjectOutputStream(otherSocket.getOutputStream());
                            out.writeObject(read);
                            out.flush();

                            LOG.log(Level.INFO, "Sent object {0} to client {1}", new Object[]{read, otherSocket});
                        }

                        map.remove(id);
                    } catch (EOFException ex) {
                        // The EOFException is being thrown by the ObjectInputStream
                        // when it tries to read an object from the input stream but
                        // finds that the end of the stream has been reached.
                        // This could happen if the client closes the connection 
                        // abruptly or sends an incomplete message.
                        map.remove(id);
                        LOG.log(Level.WARNING, "Connection closed unexpectedly: {0}", socket);
                    } catch (IOException | ClassNotFoundException ex) {
                        map.remove(id);
                        // Catch any other exceptions and log an error message
                        LOG.log(Level.SEVERE, "Error reading object from client {0}: {1}", new Object[]{socket, ex});
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
