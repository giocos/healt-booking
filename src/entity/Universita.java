package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Universita implements Serializable {

	private List<Paziente> iscritti;
	
	public Universita() {
		iscritti = new ArrayList<>();
	}
	
	public List<Paziente> getIscritti() {
		return iscritti;
	}
	
	public Paziente getIscritto(int i) {
		return iscritti.get(i);
	}
	
	public void addIscritto(Paziente p) {
		iscritti.add(p);
	}
}
