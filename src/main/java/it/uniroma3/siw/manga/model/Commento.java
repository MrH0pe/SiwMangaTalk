package it.uniroma3.siw.manga.model;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Commento {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Long id;
	
	private Boolean miPiace;
	private Boolean nonMiPiace;
	@NotBlank
	private String testo;
	private LocalDateTime tempoPubblicazione;
	
	@ManyToOne (fetch = FetchType.EAGER)
	private User utente;

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

	public LocalDateTime getTempoPubblicazione() {
		return tempoPubblicazione;
	}

	public void setTempoPubblicazione(LocalDateTime tempoPubblicazione) {
		this.tempoPubblicazione = tempoPubblicazione;
	}

	public User getUtente() {
		return utente;
	}

	public void setUtente(User utente) {
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
