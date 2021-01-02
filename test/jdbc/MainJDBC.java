package jdbc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import entity.Amministratore;
import entity.Impiegato;
import entity.Paziente;
import factory.DaoFactory;
import exception.PersistenceException;
import utils.UtilDao;
import repository.AmministratoreDao;
import repository.ImpiegatoDao;
import repository.UniversitaDao;

public class MainJDBC {

	protected static String[] readCredential() {
		
		final String[] credential = new String[4];
		try {
			FileReader f = new FileReader("../resources/credenziali.txt");
		
			BufferedReader b = new BufferedReader(f);
			String line = b.readLine();
			int i = 0;
			
			while(line != null) {

				if(!line.matches("^[A-Z]+:$")) {
					credential[i ++] = line;
				}
				line = b.readLine();
			}
			b.close();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		return credential;
	}
	
	public static void main(String[] args) throws PersistenceException {

		DaoFactory factory = DaoFactory.getDaoFactory(DaoFactory.POSTGRESQL);
		UtilDao util = factory.getUtilDao();
		util.dropDatabase();
		util.createDatabase();
		
		ImpiegatoDao impiegatoDao = factory.getImpiegatoDao();
		AmministratoreDao amministratoreDao = factory.getAmministratoreDao();
		
		//CREAZIONE AMMINISTRATORI E IMPIEGATI
		int count = 0;
		String[] ruoli = {"sportello", "sistema"};
		
		for(String it : MainJDBC.readCredential()) {
		
			if(count < 2) {
				Amministratore admin = new Amministratore();
				String[] curr = it.split(":");
	
				admin.setUsername(curr[0]);
				admin.setPassword(curr[1]);	
				amministratoreDao.save(admin);
			} else {
				Impiegato imp = new Impiegato();
				String[] curr = it.split(":");
				
				imp.setUsername(curr[0]);
				imp.setPassword(curr[1]);
				imp.setRuolo(ruoli[count % 2]);
				impiegatoDao.save(imp);
			}
			count ++;
		}
		
		//CREAZIONE PAZIENTI UNIVERSITARI
		UniversitaDao universitaDao = factory.getUniversitaDao();
		Paziente p1 = new Paziente();
		p1.setCodiceFiscale("CSNGNN94S27H579E");
		p1.setNome("Giovanni");
		p1.setCognome("Cosentino");
		p1.setMatricola(new Long(161782));
		
		Paziente p2 = new Paziente();
		p2.setCodiceFiscale("LAODVD92S07Z112K");
		p2.setNome("Davide");
		p2.setCognome("Aloia");
		p2.setMatricola(new Long(164889));
		
		Paziente p3 = new Paziente();
		p3.setCodiceFiscale("CNGMHL95H01G317R");
		p3.setNome("Michele");
		p3.setCognome("Cangeri");
		p3.setMatricola(new Long(176543));
		
		Paziente p4 = new Paziente();
		p4.setCodiceFiscale("RSSMRA90A01H501W");
		p4.setNome("Mario");
		p4.setCognome("Rossi");
		p4.setMatricola(new Long(143210));
		
		Paziente p5 = new Paziente();
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
