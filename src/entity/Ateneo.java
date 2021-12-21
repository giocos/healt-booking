package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Ateneo implements Serializable {

	private static final long serialVersionUID = 1L;

	private final List<Paziente> iscritti;
	
	public Ateneo() {
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
