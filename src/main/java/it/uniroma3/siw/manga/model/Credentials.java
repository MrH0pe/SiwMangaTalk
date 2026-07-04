package it.uniroma3.siw.manga.model;

import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;


@Entity
public class Credentials {

	public static final String DEFAULT_ROLE = "ROLE_DEFAULT";

	public static final String ADMIN_ROLE = "ROLE_ADMIN";


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


	@Column(nullable = false, unique = true)
	private String username;

	/** Password cifrata con BCrypt; non può essere null. */
	@Column(nullable = false)
	private String password;

	//DEFAULT o ADMIN
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

	public static String getDefaultRole() {
		return DEFAULT_ROLE;
	}


	public static String getAdminRole() {
		return ADMIN_ROLE;
	}


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
