package it.uniroma3.siw.manga.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
	@EqualsAndHashCode.Exclude
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

	public static String getDefaultRole() {
		return DEFAULT_ROLE;
	}

	public static String getAdminRole() {
		return ADMIN_ROLE;
	}
}
