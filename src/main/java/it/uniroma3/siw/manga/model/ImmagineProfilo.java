package it.uniroma3.siw.manga.model;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * Classe che modella un'IMMAGINE PROFILO riutilizzabile.
 *
 * Ogni immagine può essere condivisa tra più utenti (es. avatar predefiniti).
 * Il path punta a un file statico nella cartella delle risorse dell'applicazione.
 */
@Entity
public class ImmagineProfilo {

	/** Chiave primaria generata automaticamente dal DB. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/** Nome descrittivo dell'immagine (es. "avatar_default"). */
	private String nomeImmagine;

	/** Percorso relativo al file immagine servito staticamente (es. "/images/avatar1.png"). */
	private String pathImmagine;

	/**
	 * Insieme degli utenti che utilizzano questa immagine profilo (lato inverso della relazione).
	 * LAZY: viene caricato dal DB solo quando esplicitamente acceduto nel codice.
	 * Non incluso in hashCode/equals per evitare LazyInitializationException.
	 */
	@OneToMany(mappedBy = "immagineProfilo", fetch = FetchType.LAZY)
	private Set<User> utente;

	// --- Getters e Setters ---

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeImmagine() {
		return nomeImmagine;
	}

	public void setNomeImmagine(String nomeImmagine) {
		this.nomeImmagine = nomeImmagine;
	}

	public String getPathImmagine() {
		return pathImmagine;
	}

	public void setPathImmagine(String pathImmagine) {
		this.pathImmagine = pathImmagine;
	}

	public Set<User> getUtente() {
		return utente;
	}

	public void setUtente(Set<User> utente) {
		this.utente = utente;
	}

	// hashCode e equals basati solo sui campi scalari (id, nome, path);
	// la collezione 'utente' è LAZY e non va inclusa per evitare LazyInitializationException
	@Override
	public int hashCode() {
		return Objects.hash(id, nomeImmagine, pathImmagine);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ImmagineProfilo other = (ImmagineProfilo) obj;
		return Objects.equals(id, other.id)
				&& Objects.equals(nomeImmagine, other.nomeImmagine)
				&& Objects.equals(pathImmagine, other.pathImmagine);
	}
}
