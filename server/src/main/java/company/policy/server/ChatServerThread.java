package company.policy.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import static java.text.MessageFormat.format;
import java.util.Locale;
import java.util.ResourceBundle;

class ChatServerThread extends Thread {

    private final ResourceBundle RB = ResourceBundle.getBundle("i18n/messages", Locale.getDefault());
    private ChatServer server = null;
    private Socket socket = null;
    private int ID = -1;
    private DataInputStream streamIn = null;
    private DataOutputStream streamOut = null;

    ChatServerThread(ChatServer server, Socket socket) {
        super();

        this.server = server;
        this.socket = socket;

        ID = socket.getPort();
    }

    @Override
    public void run() {
        System.out.println(format(RB.getString("server_thread_running"), new Object[]{ID}));
        while (true) {
            try {
                server.handle(ID, streamIn.readUTF());
            } catch (IOException ioe) {
                System.out.println(format(RB.getString("error_reading"), new Object[]{ID, ioe.getMessage()}));
                server.remove(ID);
                stop();
            }
        }

    }

    void send(String msg) {
        try {
            streamOut.writeUTF(msg);
            streamOut.flush();
        } catch (IOException ioe) {
            System.out.println(format(RB.getString("error_sending"), new Object[]{ID, ioe.getMessage()}));
        }
        server.remove(ID);
        stop();
    }

    int getID() {
        return ID;
    }

    void open() throws IOException {
        streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
        if (streamIn != null) {
            streamIn.close();
        }
        if (streamOut != null) {
            streamOut.close();
        }
    }
}
