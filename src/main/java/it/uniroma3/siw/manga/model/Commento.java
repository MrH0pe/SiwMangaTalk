package it.uniroma3.siw.manga.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.validation.constraints.NotBlank;

// Classe che modella un COMMENTO
@Entity
public class Commento {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String testo;

	private LocalDateTime tempoPubblicazione;

	// Associazione molti a uno tra Commento e User:
	// un commento è scritto da un solo utente, ma un utente può scrivere più commenti
	@ManyToOne(fetch = FetchType.EAGER)
	private User utente;

	// Associazione molti a uno tra Commento e Manga:
	// un commento si riferisce a un solo manga, ma un manga può avere più commenti
	@ManyToOne(fetch = FetchType.EAGER)
	private Manga manga;

<<<<<<< HEAD
	// Riferimento al commento padre: null per i commenti principali, valorizzato per le risposte
	@ManyToOne(fetch = FetchType.EAGER)
	private Commento commentoPadre;

	// Lista delle risposte dirette a questo commento;
	// CascadeType.ALL + orphanRemoval propagano creazione e cancellazione alle risposte
=======
	//Riferimento al commento padre: null per commenti principali, valorizzato per le risposte
	@ManyToOne(fetch = FetchType.EAGER)
	private Commento commentoPadre;

	//Lista delle risposte dirette a questo commento
>>>>>>> 5d5dc9cfc21420119f1c688c386c5ddd2463f799
	@OneToMany(mappedBy = "commentoPadre", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("tempoPubblicazione ASC")
	private List<Commento> risposte = new ArrayList<>();

<<<<<<< HEAD
	// Lista delle reazioni (like/dislike) a questo commento;
	// CascadeType.ALL garantisce la cancellazione automatica delle reazioni quando il commento viene eliminato
	@OneToMany(mappedBy = "commento", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ReazioneCommento> reazioni = new ArrayList<>();

	// --- Getters e Setters ---

=======
>>>>>>> 5d5dc9cfc21420119f1c688c386c5ddd2463f799
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Commento getCommentoPadre() {
		return commentoPadre;
	}

	public void setCommentoPadre(Commento commentoPadre) {
		this.commentoPadre = commentoPadre;
	}

	public List<Commento> getRisposte() {
		return risposte;
	}

	public void setRisposte(List<Commento> risposte) {
		this.risposte = risposte;
	}

<<<<<<< HEAD
	public List<ReazioneCommento> getReazioni() {
		return reazioni;
	}

	public void setReazioni(List<ReazioneCommento> reazioni) {
		this.reazioni = reazioni;
	}

=======
>>>>>>> 5d5dc9cfc21420119f1c688c386c5ddd2463f799
	@Override
	public int hashCode() {
		return Objects.hash(id, manga, tempoPubblicazione, testo, utente);
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
		return Objects.equals(id, other.id)
				&& Objects.equals(manga, other.manga)
				&& Objects.equals(tempoPubblicazione, other.tempoPubblicazione)
				&& Objects.equals(testo, other.testo)
				&& Objects.equals(utente, other.utente);
	}
}
