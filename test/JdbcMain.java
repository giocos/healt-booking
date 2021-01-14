import entity.Amministratore;
import entity.Impiegato;
import entity.Paziente;
import exception.PersistenceException;
import jdbc.DaoFactory;
import jdbc.DatabaseManager;
import repository.AmministratoreDao;
import repository.ImpiegatoDao;
import repository.UniversitaDao;
import repository.UtilDao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class JdbcMain {

	protected static String[] readCredential() throws IOException {
		FileReader file = null;
		BufferedReader buffer = null;
		final String[] credentials = new String[4];
		try {
			file = new FileReader("../resources/credenziali.txt");
			buffer = new BufferedReader(file);
			String line = buffer.readLine();
			int i = 0;
			
			while (line != null) {
				if(!line.matches("^[A-Z]+:$")) {
					credentials[i++] = line;
				}
				line = buffer.readLine();
			}
		} finally {
			if (buffer != null) {
				buffer.close();
			}
		}
		return credentials;
	}
	
	public static void main(String[] args) throws PersistenceException, IOException {
		final DaoFactory factory = DatabaseManager.getInstance().getDaoFactory();
		final UtilDao utilDao = factory.getUtilDao();
		utilDao.dropDatabase();
		utilDao.createDatabase();
		
		final ImpiegatoDao impiegatoDao = factory.getImpiegatoDao();
		final AmministratoreDao amministratoreDao = factory.getAmministratoreDao();
		
		// CREAZIONE AMMINISTRATORI & IMPIEGATI
		int count = 0;
		final String[] ruoli = {"sportello", "sistema"};
		
		for (final String it : JdbcMain.readCredential()) {
			final String[] curr = it.split(":");
			if (count < 2) {
				final Amministratore admin = new Amministratore();
				admin.setUsername(curr[0]);
				admin.setPassword(curr[1]);
				amministratoreDao.save(admin);
			} else {
				final Impiegato empl = new Impiegato();
				empl.setUsername(curr[0]);
				empl.setPassword(curr[1]);
				empl.setRuolo(ruoli[count % 2]);
				impiegatoDao.save(empl);
			}
			count ++;
		}
		
		// CREAZIONE PAZIENTI & UNIVERSITARI
		final UniversitaDao universitaDao = factory.getUniversitaDao();
		final Paziente p1 = new Paziente();
		p1.setCodiceFiscale("CSNGNN94S27H579E");
		p1.setNome("Giovanni");
		p1.setCognome("Cosentino");
		p1.setMatricola(new Long(161782));

		final Paziente p2 = new Paziente();
		p2.setCodiceFiscale("LAODVD92S07Z112K");
		p2.setNome("Davide");
		p2.setCognome("Aloia");
		p2.setMatricola(new Long(164889));

		final Paziente p3 = new Paziente();
		p3.setCodiceFiscale("CNGMHL95H01G317R");
		p3.setNome("Michele");
		p3.setCognome("Cangeri");
		p3.setMatricola(new Long(176543));

		final Paziente p4 = new Paziente();
		p4.setCodiceFiscale("RSSMRA90A01H501W");
		p4.setNome("Mario");
		p4.setCognome("Rossi");
		p4.setMatricola(new Long(143210));

		final Paziente p5 = new Paziente();
		p5.setCodiceFiscale("VRDGPP92B01F205A");
		p5.setNome("Giuseppe");
		p5.setCognome("Verdi");
		p5.setMatricola(new Long(154321));
		
		universitaDao.save(p1);
		universitaDao.save(p2);
		universitaDao.save(p3);
		universitaDao.save(p4);
		universitaDao.save(p5);
	}
}
