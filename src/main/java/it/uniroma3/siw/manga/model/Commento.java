package it.uniroma3.siw.manga.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Commento {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


	@NotBlank
	private String testo;


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
	@EqualsAndHashCode.Exclude
	private Commento commentoPadre;

	// Lista delle risposte dirette a questo commento.
	// CascadeType.ALL + orphanRemoval=true: se questo commento viene eliminato,
	// tutte le sue risposte vengono eliminate automaticamente da Hibernate.
	// @OrderBy: le risposte sono ordinate dalla più vecchia alla più recente.
	@OneToMany(mappedBy = "commentoPadre", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("tempoPubblicazione ASC")
	@EqualsAndHashCode.Exclude
	private List<Commento> risposte = new ArrayList<>();
}
