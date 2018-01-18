package model;

public class Paziente {

	private String nome;
	private String cognome;
	private Long matricola;
	private String invalidita;
	
	public Paziente() {}
	
	public Paziente(String nome, String cognome, Long matricola, String invalidita) {
		
		this.nome = nome;
		this.cognome = cognome;
		this.matricola = matricola;
		this.invalidita = invalidita;
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
}