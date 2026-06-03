package it.uniroma3.siw.manga.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

/**
 * Classe che modella il profilo applicativo di un UTENTE.
 *
 * Contiene le informazioni di profilo (nome, email) distinte
 * dalle credenziali di accesso, che sono gestite dalla classe Credentials.
 *
 * Relazioni:
 * - 1:N con Commento (un utente può scrivere più commenti)
 * - 1:N con Votazione (un utente può esprimere più voti)
 *
 * hashCode e equals sono basati solo su id ed email (campi scalari EAGER)
 * per evitare LazyInitializationException sulle collezioni LAZY.
 */
@Entity
@Table(name = "utente")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	@Column(nullable = false)
	private String name;

	@NotBlank
	@Column(nullable = false, unique = true)
	private String email;

	// Associazione uno a molti tra User e Commento:
	// un utente può scrivere più commenti, ma ogni commento appartiene a un solo utente
	@OneToMany(mappedBy = "utente", fetch = FetchType.LAZY)
	private List<Commento> commentoList;

	// Associazione uno a molti tra User e Votazione:
	// un utente può esprimere più voti, ma ogni voto appartiene a un solo utente
	@OneToMany(mappedBy = "utente", fetch = FetchType.LAZY)
	private List<Votazione> votazioneList;

	// --- Getters e Setters ---

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// Alias di getName() per compatibilità con i template Thymeleaf (${utente.username})
	public String getUsername() {
		return name;
	}

	// Alias di setName() per compatibilità con i form di registrazione
	public void setUsername(String username) {
		this.name = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Commento> getCommentoList() {
		return commentoList;
	}

	public void setCommentoList(List<Commento> commentoList) {
		this.commentoList = commentoList;
	}

	public List<Votazione> getVotazioneList() {
		return votazioneList;
	}

	public void setVotazioneList(List<Votazione> votazioneList) {
		this.votazioneList = votazioneList;
	}

	// hashCode e equals basati su id ed email (campi univoci e non lazy)
	// per evitare LazyInitializationException sulle collezioni LAZY
	@Override
	public int hashCode() {
		return Objects.hash(id, email);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(id, other.id)
				&& Objects.equals(email, other.email);
	}
}
