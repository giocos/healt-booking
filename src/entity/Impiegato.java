package entity;

import java.io.Serializable;

public class Impiegato implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	private String ruolo;
	
	public Impiegato() {}
	
	public Impiegato(String username, String password, String ruolo) {
		this.username = username;
		this.password = password;
		this.ruolo = ruolo;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRuolo() {
		return ruolo;
	}
	
	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}
}
