package entity;

import java.io.Serializable;

public class Prenotazione implements Serializable {

	private String codiceVisita;
	private String nomePaziente;
	private String cognomePaziente;
	private String orarioVisita;
	private Double importo;
	
	public Prenotazione() {}
	
	public Prenotazione(String codiceVisita, String nomePaziente, String cognomePaziente, String orarioVisita, Double importo) {
		
		this.codiceVisita = codiceVisita;
		this.nomePaziente = nomePaziente;
		this.cognomePaziente = cognomePaziente;
		this.orarioVisita = orarioVisita;
		this.importo = importo;
	}

	public String getCodiceVisita() {
		return codiceVisita;
	}

	public void setCodiceVisita(String codiceVisita) {
		this.codiceVisita = codiceVisita;
	}

	public String getNomePaziente() {
		return nomePaziente;
	}

	public void setNomePaziente(String nomePaziente) {
		this.nomePaziente = nomePaziente;
	}

	public String getCognomePaziente() {
		return cognomePaziente;
	}

	public void setCognomePaziente(String cognomePaziente) {
		this.cognomePaziente = cognomePaziente;
	}

	public String getOrarioVisita() {
		return orarioVisita;
	}

	public void setOrarioVisita(String orarioVisita) {
		this.orarioVisita = orarioVisita;
	}

	public Double getImporto() {
		return importo;
	}

	public void setImporto(Double importo) {
		this.importo = importo;
	}
}
