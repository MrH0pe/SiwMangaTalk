package it.uniroma3.siw.manga.model;


import java.time.LocalDate;

// Le annotazioni JPA che dicono a Hibernate come mappare questa classe su una tabella del database.
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Autore {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


	private String nome;
	private String cognome;
	private String descrizione;
	private LocalDate dataDiNascita;


	@OneToOne   //Un autore ha scritto un solo manga
	private Manga manga;
}
