package entity;

//import java.util.Date;

import java.io.Serializable;

public class CodiceQR implements Serializable {

	private static final long serialVersionUID = 1L;

	private String esadecimale;
	private Boolean convalida;
	private String scadenza;
//	private Date scadenza;
	

	public CodiceQR() {}
	
	public CodiceQR(String esadecimale, String scadenza, Boolean convalida) {
		this.esadecimale = esadecimale;
		this.convalida = convalida;
		this.scadenza = scadenza;
	}

	public String getCodice() {
		return esadecimale;
	}

	public void setCodice(String esadecimale) {
		this.esadecimale = esadecimale;
	}

	public Boolean isConvalida() {
		return convalida;
	}

	public void setConvalida(Boolean valido) {
		this.convalida = valido;
	}

	public String getScadenza() {
		return scadenza;
	}

	public void setScadenza(String scadenza) {
		this.scadenza = scadenza;
	}
}
