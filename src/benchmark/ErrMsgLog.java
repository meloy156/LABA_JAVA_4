package benchmark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * Централизированный логгер и счетчик ошибок
 * каждый лог-файл - свой экземпляр и свой Filehander
 */
public class ErrMsgLog {

    /** флаг конфига - logging.properties */
    private static boolean configLoaded = false;

    private final Logger log;
    private final FileHandler fileHandler;
    private final List<Exception> errList = new ArrayList<>();


    /**
     * @param logFilePath путь к файлу лога
     * @throws IOException если не удалось создать файл
      */
    public ErrMsgLog(String logFilePath) throws IOException {

        // читаем конфиг
        if (!configLoaded) {
             LogManager.getLogManager().readConfiguration(
                    ErrMsgLog.class.getResourceAsStream("logging.properties")
            );
            configLoaded = true;
        }

        // уникальное имя логера
        this.log = Logger.getLogger(ErrMsgLog.class.getName() + "."
            + logFilePath);

        // Отключение родитеских хендлеров
        this.log.setUseParentHandlers(false);

        // уровень лога
        this.log.setLevel(Level.FINE);

        // создание FileHandler для конкретного файла
        this.fileHandler = new FileHandler(logFilePath, true);
        this.fileHandler.setFormatter(new SimpleFormatter());
        this.fileHandler.setLevel(Level.FINE);
        this.log.addHandler(this.fileHandler);
    }

    /** Записать строку в лог-файл */
    public void log(String message) {
        this.log.log(Level.FINE, message);
    }

    /** Добавить ошибку в список и записать в лог-файл */
    public void addErrWithLog(Exception e) {
        errList.add(e);
        log.log(Level.FINE,"ERROR: " + e.getMessage());
    }

    /** Сколько ошибок в массиве */
    public int getErrCount() {
        return errList.size();
    }

    /** Закрыть файловый хендлер и удалить его из лога */
    public void close() {
        if (fileHandler != null) {
            fileHandler.close();
            log.removeHandler(fileHandler);
        }
    }
}
