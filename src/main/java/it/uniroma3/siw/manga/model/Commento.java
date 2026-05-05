package it.uniroma3.siw.manga.model;

import java.sql.Date;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Commento {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Long id;
	
	private Boolean miPiace;
	private Boolean nonMiPiace;
	private String testo;
	private Date tempoPubblicazione;
	
	@ManyToOne (fetch = FetchType.EAGER)
	private Utente utente;

	@ManyToOne (fetch = FetchType.EAGER)
	private Manga manga;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getMiPiace() {
		return miPiace;
	}

	public void setMiPiace(Boolean miPiace) {
		this.miPiace = miPiace;
	}

	public Boolean getNonMiPiace() {
		return nonMiPiace;
	}

	public void setNonMiPiace(Boolean nonMiPiace) {
		this.nonMiPiace = nonMiPiace;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public Date getTempoPubblicazione() {
		return tempoPubblicazione;
	}

	public void setTempoPubblicazione(Date tempoPubblicazione) {
		this.tempoPubblicazione = tempoPubblicazione;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public Manga getManga() {
		return manga;
	}

	public void setManga(Manga manga) {
		this.manga = manga;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, manga, miPiace, nonMiPiace, tempoPubblicazione, testo, utente);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Commento other = (Commento) obj;
		return Objects.equals(id, other.id) && Objects.equals(manga, other.manga)
				&& Objects.equals(miPiace, other.miPiace) && Objects.equals(nonMiPiace, other.nonMiPiace)
				&& Objects.equals(tempoPubblicazione, other.tempoPubblicazione) && Objects.equals(testo, other.testo)
				&& Objects.equals(utente, other.utente);
	}


	

	
	
}
