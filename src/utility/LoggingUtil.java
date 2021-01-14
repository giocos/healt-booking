package utility;

import entity.Logging;
import jdbc.DatabaseManager;
import repository.LoggingDao;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class LoggingUtil {

    private static final String FORMAT = "hh:MM:ss";

    protected LoggingUtil() {}

    public static void registraAccesso(String username, String azione) {
        final LoggingDao loggingDao = DatabaseManager.getInstance().getDaoFactory().getLoggingDao();
        final Logging logging = new Logging();
        logging.setAzione(azione);
        Date date = new Date();
        int id = loggingDao.getId() + 1;//generate PK
        logging.setId(id);
        logging.setData(date);
        logging.setOrario(new SimpleDateFormat(FORMAT).format(date));
        logging.setNomeUtente(username);

        loggingDao.save(logging);
    }

    public static void registraDisconnessione(String username, String azione) {
        registraAccesso(username, azione);
    }
}
