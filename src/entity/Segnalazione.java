package entity;

import java.io.Serializable;

public class Segnalazione implements Serializable {

	private Integer id;
	private String email;
	private String nomeUtente;
	private String motivazione;
	private String commento;
	private String risposta;
	private Boolean risolto;
	///////////////////////
	private Boolean mostra;
	
	public Segnalazione() {}
	
	public Segnalazione(Integer id, String email, String nomeUtente, String motivazione, 
			String domanda, String risposta, Boolean risolto) {
		
		this.id = id;
		this.email = email;
		this.nomeUtente = nomeUtente;
		this.motivazione = motivazione;
		this.risposta = risposta;
		this.commento = domanda;
		this.risolto = risolto;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNomeUtente() {
		return nomeUtente;
	}

	public void setNomeUtente(String nomeUtente) {
		this.nomeUtente = nomeUtente;
	}

	public String getMotivazione() {
		return motivazione;
	}

	public void setMotivazione(String motivazione) {
		this.motivazione = motivazione;
	}
	
	public String getCommento() {
		return commento;
	}

	public void setCommento(String commento) {
		this.commento = commento;
	}

	public String getRisposta() {
		return risposta;
	}

	public void setRisposta(String risposta) {
		this.risposta = risposta;
	}

	public Boolean getRisolto() {
		return risolto;
	}

	public void setRisolto(Boolean risolto) {
		this.risolto = risolto;
	}

	public Boolean getMostra() {
		return mostra;
	}

	public void setMostra(Boolean mostra) {
		this.mostra = mostra;
	}
}
