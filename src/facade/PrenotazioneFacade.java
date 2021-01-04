package facade;

import buffer.BufferFactory;
import entity.CodiceQR;
import entity.Paziente;
import entity.Prenotazione;
import factory.DataBaseManager;
import org.json.JSONException;
import org.json.JSONObject;
import repository.CodiceQRDao;
import repository.PazienteDao;
import repository.PrenotazioneDao;
import repository.UniversitaDao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PrenotazioneFacade {

    private static final int LIMITE_PRENOTAZIONI = 50;
    private static final int TEMPO_EFFETTIVO = 15;
    private static final int TEMPO_VISITA = 10;
    private static final int CONVALIDA = 10;

    private static final String ORARIO_INIZIO = "00:00:00";
    private static final String ORARIO_FINE = "23:59:00";

    private static PrenotazioneFacade facade = null;

    private String response;

    private PrenotazioneFacade() {
        response = "";
    }

    public static PrenotazioneFacade getInstance() {
        if (facade == null) {
            facade = new PrenotazioneFacade();
        }
        return facade;
    }

    public String checkPrenotazione() {
        final String dateFormat = "HH:mm";
        final String currentTime = new SimpleDateFormat(dateFormat).format(new Date());

        final Calendar start = setTimeToCalendar(dateFormat, ORARIO_INIZIO);
        final Calendar end = setTimeToCalendar(dateFormat, ORARIO_FINE);
        final Calendar now = setTimeToCalendar(dateFormat, currentTime);
        now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + TEMPO_EFFETTIVO);

        final PrenotazioneDao prenotazioneDao = DataBaseManager.getInstance().getDaoFactory().getPrenotazioneDao();
        final int visiteTotali = prenotazioneDao.getTotalVisits();

        final Date current = new Date(now.getTimeInMillis() + (visiteTotali * TEMPO_VISITA * 60000));
        if (!(current.after(start.getTime()) && current.before(end.getTime()))) {
            response = "false;Orario non valido per effettuare una prenotazione!";
            return response;
        }

        if (visiteTotali >= LIMITE_PRENOTAZIONI) {
            response = "redirect;Attenzione: Limite Prenotazioni raggiunto";
            return response;
        }
        final String probabileVisita = new SimpleDateFormat(dateFormat).format(current);
        response = "true;" + (visiteTotali + 1) + ";" + probabileVisita;
        return response;
    }

    public String effettuaPrenotazione(InputStream input) {
        BufferedReader reader = null;
        StringBuffer jsonReceived = null;
        try {
            jsonReceived = BufferFactory.getStringBuffer();
            reader = BufferFactory.getBufferReader(input);
            String line = reader.readLine();

            while (line != null) {
                jsonReceived.append(line);
                line = reader.readLine();
            }

            final JSONObject json = new JSONObject(jsonReceived.toString());

            final PazienteDao pazienteDao = DataBaseManager.getInstance().getDaoFactory().getPazienteDao();
            final PrenotazioneDao prenotazioneDao = DataBaseManager.getInstance().getDaoFactory().getPrenotazioneDao();
            final CodiceQRDao codiceQRDao = DataBaseManager.getInstance().getDaoFactory().getCodiceQRDao();
            final UniversitaDao universitaDao = DataBaseManager.getInstance().getDaoFactory().getUniversitaDao();

            Long matricola = null;
            if (!json.getString("matricola").equals("")) {
                matricola = (Long.parseLong(json.getString("matricola")));
            }

            if (pazienteDao.findByPrimaryKey(json.getString("codiceFiscale")) != null) {
                response = "false;Paziente con codice fiscale '" + json.getString("codiceFiscale") + "' gia' presente";
                return response;
            } else {
                if (pazienteDao.exists(matricola)) {
                    response = "false;La Matricola '" + json.getString("matricola") + "' e' stata associata ad un altro Paziente";
                    return response;
                }
            }
            final Paziente paziente = new Paziente();
            paziente.setCodiceFiscale(json.getString("codiceFiscale"));
            paziente.setNome(json.getString("nome"));
            paziente.setCognome(json.getString("cognome"));
            paziente.setInvalidita(json.getString("invalidita"));

            double imp = 25d;
            if (matricola != null) {
                final Paziente pazienteByPK = universitaDao.findByPrimaryKey(matricola);
                if (pazienteByPK != null) {
                    if (pazienteByPK.getNome().equals(paziente.getNome()) && pazienteByPK.getCognome().equals(paziente.getCognome())) {
                        imp = 0d;
                        paziente.setMatricola(matricola);
                    } else {
                        paziente.setMatricola(null);
                    }
                } else {
                    paziente.setMatricola(null);
                }
            }

            if (!paziente.getInvalidita().equalsIgnoreCase("Nessuna")) {
                imp = 0d;
            }
            final int visiteTotali = prenotazioneDao.getTotalVisits();
            final Calendar now = Calendar.getInstance();
            now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + TEMPO_EFFETTIVO);

            final Date date1 = new Date(now.getTimeInMillis() + (visiteTotali * TEMPO_VISITA * 60000));
            final Date date2 = new Date(now.getTimeInMillis() + (visiteTotali * TEMPO_VISITA * 60000) - (CONVALIDA * 60000));

            final String dateFormat = "HH:mm";
            final String visita = new SimpleDateFormat(dateFormat).format(date1);
            final String scadenza = new SimpleDateFormat(dateFormat).format(date2);

            final CodiceQR codiceQR = new CodiceQR();
            codiceQR.setCodice(json.getString("hexcode"));
            codiceQR.setScadenza(scadenza);
            codiceQR.setConvalida(false);

            paziente.setCodiceQR(codiceQR.getCodice());

            final Prenotazione prenotazione = new Prenotazione();
            prenotazione.setCodiceVisita(codiceQR.getCodice());
            prenotazione.setNomePaziente(paziente.getNome());
            prenotazione.setCognomePaziente(paziente.getCognome());
            prenotazione.setOrarioVisita(visita);
            prenotazione.setImporto(imp);

            codiceQRDao.save(codiceQR);
            pazienteDao.save(paziente);
            prenotazioneDao.save(prenotazione);

            final JSONObject pazienteJson = new JSONObject(paziente);
            response = "true;" + pazienteJson.toString() + ";" + String.valueOf(prenotazione.getImporto());

        } catch (final JSONException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    private Calendar setTimeToCalendar(String dateFormat, String date) {
        Date time = null;
        Calendar calendar = null;
        try {
            time = new SimpleDateFormat(dateFormat).parse(date);
            calendar = Calendar.getInstance();
            calendar.setTime(time);

        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }
}
