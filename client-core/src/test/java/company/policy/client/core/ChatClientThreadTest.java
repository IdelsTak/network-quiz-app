package company.policy.client.core;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import org.junit.Test;

public class ChatClientThreadTest {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("i18n/messages", Locale.ITALY);

    @Test
    public void testSomeMethod() {
        System.out.println(MessageFormat.format(BUNDLE.getString("error_getting_input_stream"), "hello"));
    }

}
