package it.uniroma3.siw.manga.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Manga {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String nome;
	private String descrizione;
	private String pathImmagine;
	private String pathSfondo;
	private Integer annoPubblicazione;

	// Associazione 1 a 1 tra Manga e Autore (lato inverso):
	// un manga è scritto da un solo autore e un autore scrive un solo manga
	@OneToOne(mappedBy = "manga", fetch = FetchType.EAGER)
	@EqualsAndHashCode.Exclude
	private Autore autore;

	// Associazione uno a molti tra Manga e Votazione:
	// un manga può avere più votazioni, ma una votazione appartiene a un solo manga
	@OneToMany(mappedBy = "manga", fetch = FetchType.LAZY)
	@EqualsAndHashCode.Exclude
	private List<Votazione> votazioneList;

	// Associazione uno a molti tra Manga e Commento:
	// un manga può avere più commenti, ma un commento appartiene a un solo manga
	@OneToMany(mappedBy = "manga", fetch = FetchType.LAZY)
	@EqualsAndHashCode.Exclude
	private List<Commento> commentoList;
}
