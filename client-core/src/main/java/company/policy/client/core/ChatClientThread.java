package company.policy.client.core;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import static java.text.MessageFormat.format;
import java.util.Locale;
import java.util.ResourceBundle;

public class ChatClientThread extends Thread {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("i18n/messages", Locale.getDefault());
    private final Socket socket;
    private final ChatClient client;
    private DataInputStream streamIn;

    public ChatClientThread(ChatClient client, Socket socket) {
        this.client = client;
        this.socket = socket;

        open();
        start();
    }

    public void open() {
        try {
            streamIn = new DataInputStream(socket.getInputStream());
        } catch (IOException ioe) {
            System.out.println(format(BUNDLE.getString("error_getting_input_stream"), ioe));
            client.stop();
        }
    }

    public void close() {
        try {
            if (streamIn != null) {
                streamIn.close();
            }
        } catch (IOException ioe) {
            System.out.println(format(BUNDLE.getString("error_closing_input_stream"), ioe));
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                client.handle(streamIn.readUTF());
            } catch (IOException ioe) {
                System.out.println(format(BUNDLE.getString("listening_error"), ioe.getMessage()));
                client.stop();
            }
        }
    }

}
