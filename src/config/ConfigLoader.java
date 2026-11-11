package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    private final Properties property = new Properties();

    public ConfigLoader(String fileName) throws IOException {
        // Просто открываем файл и читаем его
        try (InputStream is = new FileInputStream(fileName)) {
            property.load(is);
        }
    }

    // ===== Пользователь =====
    public String getLogin() {
        return property.getProperty("USER_LOGIN", "guest");
    }

    public String getPassword() {
        return property.getProperty("USER_PASSWORD", "");
    }

    public String getGroup() {
        return property.getProperty("USER_GROUP", "user");
    }

    public boolean isRoot() {
        return "root".equalsIgnoreCase(getGroup());
    }

    // ===== Режимы =====
    public boolean isDebug() {
        return Boolean.parseBoolean(property.getProperty("DEBUG", "false"));
    }

    public boolean isAutotests() {
        return Boolean.parseBoolean(property.getProperty("AUTOTESTS", "false"));
    }

    // ===== Пути =====
    public String getDbFile() {
        return property.getProperty("DB_FILE", "data/cargo.db");
    }

    public String getLogFile() {
        return property.getProperty("LOG_FILE", "data/app.log");
    }
}