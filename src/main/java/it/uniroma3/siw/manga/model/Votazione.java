package it.uniroma3.siw.manga.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@Entity
@Table(uniqueConstraints = {
	@UniqueConstraint(columnNames = {"utente_id", "manga_id"})  //Chiavi uniche, sulle FK vero Manga e User
})
@Getter
@Setter
@EqualsAndHashCode
public class Votazione {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


	private Double valoreStelline;

	//Associazione molti a uno
	//Un manga può avere tanti voti
	// LAZY (deviazione motivata dal default EAGER di @ManyToOne): nessuna vista naviga
	// da una Votazione verso il manga o l'utente. Media e conteggio usano query aggregate
	// (AVG/COUNT) che non caricano le entità; con EAGER ogni caricamento di Votazione
	// avrebbe generato query aggiuntive inutili (problema N+1).
	// equals/hashCode NON usano manga e utente: essendo LAZY, toccarli su un'entità
	// detached (fuori transazione) causerebbe una LazyInitializationException.
	@ManyToOne(fetch = FetchType.LAZY)
	@EqualsAndHashCode.Exclude
	private Manga manga;

	//Associazione molti a uno
	//Tanti utenti posso votare un singolo manga
	@ManyToOne(fetch = FetchType.LAZY)
	@EqualsAndHashCode.Exclude
	private User utente;
}
