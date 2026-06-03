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

/**
 * Classe che modella un COMMENTO scritto da un utente su un manga.
 *
 * Struttura ad albero:
 * - Commenti principali: commentoPadre = null
 * - Risposte: commentoPadre = riferimento al commento genitore
 *
 * È collegata a: User (autore), Manga (su cui è scritto),
 *                Commento (padre e lista risposte), ReazioneCommento (like/dislike)
 */
@Entity
public class Commento {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	// Il testo del commento; @NotBlank garantisce che non sia vuoto o solo spazi
	@NotBlank
	private String testo;

	// Data e ora di pubblicazione, impostata nel controller al momento del salvataggio
	private LocalDateTime tempoPubblicazione;

	// Associazione molti a uno tra Commento e User:
	// un commento è scritto da un solo utente, ma un utente può scrivere più commenti.
	// EAGER: l'utente viene caricato subito insieme al commento (serve per mostrare il nome)
	@ManyToOne(fetch = FetchType.EAGER)
	private User utente;

	// Associazione molti a uno tra Commento e Manga:
	// un commento si riferisce a un solo manga, ma un manga può avere più commenti.
	// EAGER: il manga viene caricato subito (serve per i redirect nel controller)
	@ManyToOne(fetch = FetchType.EAGER)
	private Manga manga;

	// Riferimento al commento padre: null per i commenti principali, valorizzato per le risposte.
	// EAGER: il padre viene caricato subito (serve per la logica di cancellazione a cascata)
	@ManyToOne(fetch = FetchType.EAGER)
	private Commento commentoPadre;

	// Lista delle risposte dirette a questo commento.
	// CascadeType.ALL + orphanRemoval=true: se questo commento viene eliminato,
	// tutte le sue risposte vengono eliminate automaticamente da Hibernate.
	// @OrderBy: le risposte sono ordinate dalla più vecchia alla più recente.
	@OneToMany(mappedBy = "commentoPadre", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("tempoPubblicazione ASC")
	private List<Commento> risposte = new ArrayList<>();

	// Lista delle reazioni (like/dislike) a questo commento.
	// CascadeType.ALL + orphanRemoval=true: garantisce la cancellazione automatica
	// di tutte le reazioni quando il commento viene eliminato.
	@OneToMany(mappedBy = "commento", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ReazioneCommento> reazioni = new ArrayList<>();

	// --- Getters e Setters ---

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

	public List<ReazioneCommento> getReazioni() {
		return reazioni;
	}

	public void setReazioni(List<ReazioneCommento> reazioni) {
		this.reazioni = reazioni;
	}

	// hashCode e equals basati sui campi principali (id, manga, testo, utente, data)
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
