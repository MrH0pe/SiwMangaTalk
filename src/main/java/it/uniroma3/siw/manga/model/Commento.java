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
	
	private Boolean like;
	private Boolean dislike;
	private String testo;
	private Date tempoPubblicazione;
	
	@ManyToOne (fetch = FetchType.EAGER)
	private Utente utente;
	
	@ManyToOne (fetch = FetchType.EAGER)
	private Thread thread;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getLike() {
		return like;
	}

	public void setLike(Boolean like) {
		this.like = like;
	}

	public Boolean getDislike() {
		return dislike;
	}

	public void setDislike(Boolean dislike) {
		this.dislike = dislike;
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

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dislike, id, like, tempoPubblicazione, testo, thread, utente);
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
		return Objects.equals(dislike, other.dislike) && Objects.equals(id, other.id)
				&& Objects.equals(like, other.like) && Objects.equals(tempoPubblicazione, other.tempoPubblicazione)
				&& Objects.equals(testo, other.testo) && Objects.equals(thread, other.thread)
				&& Objects.equals(utente, other.utente);
	}
	
	
}
