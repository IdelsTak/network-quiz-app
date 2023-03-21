package company.policy.client.staff;

import java.util.Locale;
import java.util.ResourceBundle;

public enum ResourceBundles {
    DEFAULT("i18n/messages", Locale.getDefault()), ITALIAN("i18n/messages", Locale.ITALIAN);

    private final ResourceBundle resourceBundle;

    private ResourceBundles(String baseName, Locale locale) {
        resourceBundle = ResourceBundle.getBundle(baseName, locale);
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

}
