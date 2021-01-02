package entity;

import java.io.Serializable;
import java.util.Date;

public class Logging implements Serializable {

	private Integer id;
	private String azione;
	private Date data;
	private String orario;
	private String nomeUtente;
	
	public Logging() {}
	
	public Logging(Integer id, String azione, Date data, String orario, String nomeUtente) {
		
		this.id = id;
		this.azione = azione;
		this.data = data;
		this.orario = orario;
		this.nomeUtente = nomeUtente;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAzione() {
		return azione;
	}

	public void setAzione(String azione) {
		this.azione = azione;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getOrario() {
		return orario;
	}

	public void setOrario(String orario) {
		this.orario = orario;
	}

	public String getNomeUtente() {
		return nomeUtente;
	}

	public void setNomeUtente(String nomeUtente) {
		this.nomeUtente = nomeUtente;
	}	
}
