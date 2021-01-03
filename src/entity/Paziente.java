package entity;

import java.io.Serializable;

public class Paziente implements Serializable {

	private String codiceFiscale;
	private String nome;
	private String cognome;
	private Long matricola;
	private String invalidita;
	private String codiceQR;
	
	public Paziente() {}
	
	public Paziente(String codiceFiscale, String nome, String cognome,
					Long matricola, String invalidita, String codiceQR)
	{
		this.codiceFiscale = codiceFiscale;
		this.nome = nome;
		this.cognome = cognome;
		this.matricola = matricola;
		this.invalidita = invalidita;
		this.codiceQR = codiceQR;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public Long getMatricola() {
		return matricola;
	}

	public void setMatricola(Long matricola) {
		this.matricola = matricola;
	}

	public String getInvalidita() {
		return invalidita;
	}

	public void setInvalidita(String invalidita) {
		this.invalidita = invalidita;
	}

	public String getCodiceQR() {
		return codiceQR;
	}

	public void setCodiceQR(String codiceQR) {
		this.codiceQR = codiceQR;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
}
