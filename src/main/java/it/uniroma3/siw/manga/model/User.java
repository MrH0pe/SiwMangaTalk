package it.uniroma3.siw.manga.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "utente")  //"User" è una parola riservata di SQL
@Getter
@Setter
@EqualsAndHashCode
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	@Column(nullable = false)
	@EqualsAndHashCode.Exclude
	private String name;

	@Email
	@NotBlank
	@Column(nullable = false, unique = true)
	private String email;

	// Associazione uno a molti tra User e Commento:
	// un utente può scrivere più commenti, ma ogni commento appartiene a un solo utente
	@OneToMany(mappedBy = "utente", fetch = FetchType.LAZY)
	@EqualsAndHashCode.Exclude
	private List<Commento> commentoList;

	// Associazione uno a molti tra User e Votazione:
	// un utente può esprimere più voti, ma ogni voto appartiene a un solo utente
	@OneToMany(mappedBy = "utente", fetch = FetchType.LAZY)
	@EqualsAndHashCode.Exclude
	private List<Votazione> votazioneList;
}
