package it.uniroma3.siw.manga.model;

import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

/**
 * Classe che modella le CREDENZIALI di accesso di un utente.
 *
 * Contiene username, password cifrata con BCrypt e ruolo applicativo.
 * È separata da User per tenere distinte le informazioni di profilo
 * da quelle di autenticazione, seguendo il principio di separazione delle responsabilità.
 *
 * Spring Security legge username, password e ruolo da questa tabella
 * tramite le query JDBC configurate in SecurityConfiguration.
 */
@Entity
public class Credentials {

	/** Ruolo assegnato a tutti gli utenti registrati normalmente. */
	public static final String DEFAULT_ROLE = "DEFAULT";

	/** Ruolo assegnato agli amministratori del sito. */
	public static final String ADMIN_ROLE = "ADMIN";

	/** Chiave primaria generata automaticamente dal DB. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/** Username univoco per il login; non può essere null. */
	@Column(nullable = false, unique = true)
	private String username;

	/** Password cifrata con BCrypt; non può essere null. */
	@Column(nullable = false)
	private String password;

	/**
	 * Ruolo dell'utente: "DEFAULT" per utenti normali, "ADMIN" per amministratori.
	 * Usato da Spring Security per i controlli di autorizzazione.
	 */
	private String role;

	/**
	 * Associazione 1:1 tra Credentials e User.
	 * CascadeType.ALL significa che la creazione/eliminazione di Credentials
	 * si propaga automaticamente al relativo User.
	 */
	@OneToOne(cascade = CascadeType.ALL)
	private User utente;

	// --- Getters e Setters ---

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public User getUtente() {
		return utente;
	}

	public void setUtente(User utente) {
		this.utente = utente;
	}

	/** Metodo statico di comodo per accedere alla costante DEFAULT_ROLE. */
	public static String getDefaultRole() {
		return DEFAULT_ROLE;
	}

	/** Metodo statico di comodo per accedere alla costante ADMIN_ROLE. */
	public static String getAdminRole() {
		return ADMIN_ROLE;
	}

	// hashCode e equals basati su id, username, role e utente (tutti EAGER/scalari)
	// la password è esclusa deliberatamente: non ha senso confrontarla per l'identità dell'entità
	@Override
	public int hashCode() {
		return Objects.hash(id, username, role, utente);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Credentials other = (Credentials) obj;
		return Objects.equals(id, other.id)
				&& Objects.equals(username, other.username)
				&& Objects.equals(role, other.role)
				&& Objects.equals(utente, other.utente);
	}
}
