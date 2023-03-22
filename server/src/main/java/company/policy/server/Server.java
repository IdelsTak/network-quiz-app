package company.policy.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements a server that listens on port 8080 for incoming client
 * connections.
 * <p>
 * When a client connects, the server generates a UUID for the client and adds
 * it to a map that stores the UUID and corresponding socket for each client.
 * Then, the server submits a new task to an executor to handle the client's
 * requests. The task reads an object from the client's input stream, gets a
 * list of all sockets except the current one from the map, and writes the
 * object to all other sockets. Finally, the task removes the client's UUID from
 * the map and closes the socket.
 * <p>
 * The code uses a fixed thread pool executor to handle multiple clients
 * concurrently. This is achieved by submitting new tasks to the executor for
 * each client that connects to the server. The use of a ConcurrentHashMap to
 * store the UUID and socket for each client ensures thread safety.
 * <p>
 * The code also handles exceptions such as EOFException, IOException, and
 * ClassNotFoundException that may occur when reading objects from the client's
 * input stream or writing objects to the sockets of other clients. The code
 * logs appropriate messages at different levels of severity depending on the
 * exception.
 * <p>
 * It's implementation is basic in that allows multiple clients to communicate
 * with each other using objects, however, there are a few areas for
 * improvement, such as adding more error handling and implementing a more
 * efficient way of sending objects to other clients, such as using a
 * publish-subscribe messaging system.
 * <p>
 * To implement a publish-subscribe messaging system, we can use a library such
 * as Apache ActiveMQ or Eclipse Mosquitto. Here's an example of how we can
 * modify the existing code to use ActiveMQ:
 *
 * @author hiram-k
 */
public class Server {

    private static final Logger LOG = Logger.getLogger(Server.class.getName());
    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("i18n/messages", Locale.getDefault());

    public static void main(String[] args) throws Exception {
        // Create a server socket on the specified port
        ServerSocket serverSocket = new ServerSocket(8080);
        LOG.log(Level.INFO, MESSAGES.getString("server.started"), serverSocket.getLocalPort());

        // Create a thread pool executor with a maximum of 100 threads
        int maxThreads = 100;
        ExecutorService executor = Executors.newFixedThreadPool(maxThreads);

        // Create a map to store the UUID and corresponding socket for each client
        Map<UUID, Socket> map = new ConcurrentHashMap<>();

        while (true) {
            try {
                // Wait for a client to connect and accept the connection
                Socket socket = serverSocket.accept();
                LOG.log(Level.INFO, MESSAGES.getString("client.connected"), socket);

                // Generate a UUID for the client and add it to the map
                UUID id = UUID.randomUUID();
                map.put(id, socket);

                // Submit a new task to the executor to handle the client's requests
                executor.submit(() -> {
                    try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                        // Read an object from the client's input stream
                        Object read = in.readObject();

                        LOG.log(Level.INFO, MESSAGES.getString("received.object"), new Object[]{read, socket});

                        // Get a list of all sockets except the current one
                        List<Socket> sockets = map.entrySet().stream()
                                .filter(e -> !Objects.equals(e.getKey(), id))
                                .map(Entry::getValue)
                                .toList();

                        // Write the object to all other sockets
                        for (Socket otherSocket : sockets) {
                            ObjectOutputStream out = new ObjectOutputStream(otherSocket.getOutputStream());
                            out.writeObject(read);
                            out.flush();

                            LOG.log(Level.INFO, MESSAGES.getString("sent.object"), new Object[]{read, otherSocket});
                        }

                        map.remove(id);
                    } catch (EOFException ex) {
                        // The EOFException is being thrown by the ObjectInputStream
                        // when it tries to read an object from the input stream but
                        // finds that the end of the stream has been reached.
                        // This could happen if the client closes the connection 
                        // abruptly or sends an incomplete message.
                        map.remove(id);
                        LOG.log(Level.WARNING, MESSAGES.getString("connection.closed"), socket);
                    } catch (IOException | ClassNotFoundException ex) {
                        map.remove(id);
                        // Catch any other exceptions and log an error message
                        LOG.log(Level.SEVERE, MESSAGES.getString("error.reading.object"), new Object[]{socket, ex});
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException ex) {
                            LOG.log(Level.SEVERE, null, ex);
                        }
                    }
                });
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, MESSAGES.getString("error.accepting.client.connection"), ex);
            }
        }
    }

}
