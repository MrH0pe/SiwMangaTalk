package it.uniroma3.siw.manga.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

/**
 * Classe che modella un AUTORE di manga.
 *
 * Ogni autore è associato a esattamente un manga (relazione 1:1).
 * La FK della relazione è gestita sul lato Autore (colonna manga_id in tabella autore).
 */
@Entity
public class Autore {

	/** Chiave primaria generata automaticamente dal DB. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/** Nome dell'autore (es. "Eiichiro"). */
	private String nome;

	/** Cognome dell'autore (es. "Oda"). */
	private String cognome;

	/** Breve biografia o descrizione dell'autore. */
	private String descrizione;

	/**
	 * Associazione 1:1 tra Autore e Manga (lato proprietario della FK).
	 * Un autore scrive un solo manga e un manga è scritto da un solo autore.
	 * Il lato inverso è dichiarato in Manga con mappedBy = "manga".
	 */
	@OneToOne
	private Manga manga;

	// --- Getters e Setters ---

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Manga getManga() {
		return manga;
	}

	public void setManga(Manga manga) {
		this.manga = manga;
	}

	// hashCode e equals basati su tutti i campi scalari + manga (EAGER, sicuro da usare)
	@Override
	public int hashCode() {
		return Objects.hash(id, nome, cognome, descrizione, manga);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Autore other = (Autore) obj;
		return Objects.equals(id, other.id)
				&& Objects.equals(nome, other.nome)
				&& Objects.equals(cognome, other.cognome)
				&& Objects.equals(descrizione, other.descrizione)
				&& Objects.equals(manga, other.manga);
	}
}
