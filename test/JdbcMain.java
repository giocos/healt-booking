import entity.Amministratore;
import entity.Impiegato;
import entity.Paziente;
import exception.PersistenceException;
import jdbc.DaoFactory;
import jdbc.DatabaseManager;
import repository.AmministratoreDao;
import repository.ImpiegatoDao;
import repository.AteneoDao;
import repository.UtilDao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class JdbcMain {

	protected static String[] readCredential() throws IOException {
		FileReader file = null;
		BufferedReader buffer = null;
		String[] credentials = new String[4];
		try {
			file = new FileReader("../resources/credenziali.txt");
			buffer = new BufferedReader(file);
			String line = buffer.readLine();
			int i = 0;
			while (line != null) {
				if (!line.matches("^[A-Z]+:$")) {
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
		DaoFactory factory = DatabaseManager.getInstance().getDaoFactory();
		UtilDao utilDao = factory.getUtilDao();
		utilDao.dropDatabase();
		utilDao.createDatabase();
		
		ImpiegatoDao impiegatoDao = factory.getImpiegatoDao();
		AmministratoreDao amministratoreDao = factory.getAmministratoreDao();
		
		// CREAZIONE AMMINISTRATORI & IMPIEGATI
		int count = 0;
		String[] ruoli = {"sportello", "sistema"};
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
		AteneoDao ateneoDao = factory.getAteneoDao();
		Paziente paziente1 = new Paziente();
		paziente1.setCodiceFiscale("CSNGNN94S27H579E");
		paziente1.setNome("Giovanni");
		paziente1.setCognome("Cosentino");
		paziente1.setMatricola(new Long(161782));

		Paziente paziente2 = new Paziente();
		paziente2.setCodiceFiscale("LAODVD92S07Z112K");
		paziente2.setNome("Davide");
		paziente2.setCognome("Aloia");
		paziente2.setMatricola(new Long(164889));

		Paziente paziente3 = new Paziente();
		paziente3.setCodiceFiscale("CNGMHL95H01G317R");
		paziente3.setNome("Michele");
		paziente3.setCognome("Cangeri");
		paziente3.setMatricola(new Long(176543));

		Paziente paziente4 = new Paziente();
		paziente4.setCodiceFiscale("RSSMRA90A01H501W");
		paziente4.setNome("Mario");
		paziente4.setCognome("Rossi");
		paziente4.setMatricola(new Long(143210));

		Paziente paziente5 = new Paziente();
		paziente5.setCodiceFiscale("VRDGPpaziente92B01F205A");
		paziente5.setNome("Giuseppe");
		paziente5.setCognome("Verdi");
		paziente5.setMatricola(new Long(154321));
		
		ateneoDao.save(paziente1);
		ateneoDao.save(paziente2);
		ateneoDao.save(paziente3);
		ateneoDao.save(paziente4);
		ateneoDao.save(paziente5);
	}
}
