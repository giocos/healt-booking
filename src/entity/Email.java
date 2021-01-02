package entity;

import java.io.Serializable;

public class Email implements Serializable {
	
	public String admin;
	public String messaggio;
	public String emittente;
	public String destinatario;
	
	public Email() {}
	
	public Email(String admin, String messaggio, String emittente, String destinatario) {
		
		this.admin = admin;
		this.messaggio = messaggio;
		this.emittente = emittente;
		this.destinatario = destinatario;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}

	public String getEmittente() {
		return emittente;
	}

	public void setEmittente(String emittente) {
		this.emittente = emittente;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
}
