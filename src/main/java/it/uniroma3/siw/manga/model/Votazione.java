package it.uniroma3.siw.manga.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Classe che modella una VOTAZIONE (voto con stelle) di un utente su un manga.
 *
 * Il vincolo di unicità su (utente_id, manga_id) garantisce che ogni utente
 * possa esprimere al massimo un voto per manga. Se l'utente vota di nuovo,
 * il record esistente viene aggiornato (upsert gestito da VotazioneService).
 *
 * Il valore è un Double da 0.5 a 5.0 con incrementi di 0.5 (mezze stelle).
 *
 * È collegata a: Manga (manga votato), User (utente che ha votato)
 */
@Entity
@Table(uniqueConstraints = {
	@UniqueConstraint(columnNames = {"utente_id", "manga_id"})
})
public class Votazione {

	/** Chiave primaria generata automaticamente dal DB. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/** Valore del voto: da 0.5 (minimo) a 5.0 (massimo), a mezzi punti. */
	private Double valoreStelline;

	// Associazione molti a uno tra Votazione e Manga:
	// una votazione riguarda un solo manga, ma un manga può ricevere molte votazioni.
	// EAGER: il manga viene sempre caricato insieme alla votazione.
	@ManyToOne(fetch = FetchType.EAGER)
	private Manga manga;

	// Associazione molti a uno tra Votazione e User:
	// una votazione appartiene a un solo utente, ma un utente può votare molti manga.
	// EAGER: l'utente viene sempre caricato insieme alla votazione.
	@ManyToOne(fetch = FetchType.EAGER)
	private User utente;

	// --- Getters e Setters ---

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getValoreStelline() {
		return valoreStelline;
	}

	public void setValoreStelline(Double valoreStelline) {
		this.valoreStelline = valoreStelline;
	}

	public Manga getManga() {
		return manga;
	}

	public void setManga(Manga manga) {
		this.manga = manga;
	}

	public User getUtente() {
		return utente;
	}

	public void setUtente(User utente) {
		this.utente = utente;
	}

	// Tutti i campi sono EAGER o scalari: nessun rischio di LazyInitializationException
	@Override
	public int hashCode() {
		return Objects.hash(id, manga, valoreStelline, utente);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Votazione other = (Votazione) obj;
		return Objects.equals(id, other.id)
				&& Objects.equals(manga, other.manga)
				&& Objects.equals(valoreStelline, other.valoreStelline)
				&& Objects.equals(utente, other.utente);
	}
}
