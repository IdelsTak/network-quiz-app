package company.policy.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import static java.text.MessageFormat.format;
import java.util.Locale;
import java.util.ResourceBundle;

public class ChatServer implements Runnable {

    private static final ResourceBundle RB = ResourceBundle.getBundle("i18n/messages", Locale.getDefault());
    private ChatServerThread clients[] = new ChatServerThread[50];
    private ServerSocket server = null;
    private Thread thread = null;
    private int clientCount = 0;

    public ChatServer(int port) {
        try {
            System.out.println(format(RB.getString("binding_to_port"), new Object[]{port}));
            server = new ServerSocket(port);
            System.out.println(format(RB.getString("server_started"), new Object[]{server}));
            start();
        } catch (IOException ioe) {
            System.out.println(format(RB.getString("cannot_bind_to_port"), new Object[]{port, ioe.getMessage()}));
        }
    }

    @Override
    public void run() {
        while (thread != null) {
            try {
                System.out.println(format(RB.getString("waiting_for_a_client")));
                addThread(server.accept());
            } catch (IOException ioe) {
                System.out.println(format(RB.getString("server_accept_error"), new Object[]{ioe}));
                stop();
            }
        }
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        if (thread != null) {
            thread.stop();
            thread = null;
        }
    }

    private int findClient(int ID) {
        for (int i = 0; i < clientCount; i++) {
            if (clients[i].getID() == ID) {
                return i;
            }
        }
        return -1;
    }

    public synchronized void handle(int ID, String input) {
        if (input.equals(".bye")) {
            clients[findClient(ID)].send(".bye");
            remove(ID);
        } else {
            for (int i = 0; i < clientCount; i++) {
                clients[i].send(ID + ": " + input);
            }
        }
    }

    public synchronized void remove(int ID) {
        int pos = findClient(ID);
        if (pos >= 0) {
            ChatServerThread toTerminate = clients[pos];
            System.out.println(format(RB.getString("removing_client_thread"), new Object[]{ID, pos}));
            if (pos < clientCount - 1) {
                for (int i = pos + 1; i < clientCount; i++) {
                    clients[i - 1] = clients[i];
                }
            }
            clientCount--;
            try {
                toTerminate.close();
            } catch (IOException ioe) {
                System.out.println(format(RB.getString("error_closing_thread"), new Object[]{ioe}));
            }
            toTerminate.stop();
        }
    }

    private void addThread(Socket socket) {
        if (clientCount < clients.length) {
            System.out.println(format(RB.getString("client_accepted"), new Object[]{socket}));
            clients[clientCount] = new ChatServerThread(this, socket);
            try {
                clients[clientCount].open();
                clients[clientCount].start();
                clientCount++;
            } catch (IOException ioe) {
                System.out.println(format(RB.getString("error_opening_thread"), new Object[]{ioe}));
            }
        } else {
            System.out.println(format(RB.getString("client_refused"), new Object[]{clients.length}));
        }
    }

    public static void main(String args[]) {
        ChatServer server = null;

        if (args.length != 1) {
            System.out.println(format(RB.getString("usage_java_chatserver")));
            server = new ChatServer(4444);
        } else {
            server = new ChatServer(Integer.parseInt(args[0]));
        }
    }
}
