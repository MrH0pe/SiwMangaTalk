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
 * Classe che modella la REAZIONE (like o dislike) di un utente a un commento.
 *
 * Il vincolo di unicità su (utente_id, commento_id) garantisce che ogni utente
 * possa avere al massimo una reazione per commento. Se cambia idea, il tipo viene
 * aggiornato; se ri-clicca la stessa reazione, essa viene rimossa (toggle logic
 * gestita in ReazioneCommentoService).
 *
 * Il campo 'tipo' può contenere solo i valori "LIKE" o "DISLIKE".
 *
 * È collegata a: User (chi ha reagito), Commento (commento su cui è la reazione)
 */
@Entity
@Table(uniqueConstraints = {
	@UniqueConstraint(columnNames = {"utente_id", "commento_id"})
})
public class ReazioneCommento {

	/** Chiave primaria generata automaticamente dal DB. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/** Tipo della reazione: "LIKE" oppure "DISLIKE". */
	private String tipo;

	// Associazione molti a uno tra ReazioneCommento e User:
	// una reazione appartiene a un solo utente.
	// EAGER: l'utente viene sempre caricato insieme alla reazione.
	@ManyToOne(fetch = FetchType.EAGER)
	private User utente;

	// Associazione molti a uno tra ReazioneCommento e Commento:
	// una reazione è relativa a un solo commento.
	// EAGER: il commento viene sempre caricato insieme alla reazione.
	@ManyToOne(fetch = FetchType.EAGER)
	private Commento commento;

	// --- Getters e Setters ---

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public User getUtente() {
		return utente;
	}

	public void setUtente(User utente) {
		this.utente = utente;
	}

	public Commento getCommento() {
		return commento;
	}

	public void setCommento(Commento commento) {
		this.commento = commento;
	}

	// Tutti i campi sono EAGER o scalari: nessun rischio di LazyInitializationException
	@Override
	public int hashCode() {
		return Objects.hash(id, tipo, utente, commento);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ReazioneCommento other = (ReazioneCommento) obj;
		return Objects.equals(id, other.id)
				&& Objects.equals(tipo, other.tipo)
				&& Objects.equals(utente, other.utente)
				&& Objects.equals(commento, other.commento);
	}
}
