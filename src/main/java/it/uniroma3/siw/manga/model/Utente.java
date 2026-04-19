package it.uniroma3.siw.manga.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Utente {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Long id;
	
	private String username;
	private String email;
	private String password;
	private Boolean admin;   //Da cambiare con l'autenticazione
	
	@ManyToOne (fetch = FetchType.EAGER)
	private ImmagineProfilo immagineProfilo;
	
	@OneToMany (mappedBy = "utente" , fetch = FetchType.LAZY)
	private List<Commento> commentoList;
	
	@OneToMany (mappedBy = "utente" , fetch = FetchType.LAZY)
	private List<Votazione> votazioneList;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public ImmagineProfilo getImmagineProfilo() {
		return immagineProfilo;
	}

	public void setImmagineProfilo(ImmagineProfilo immagineProfilo) {
		this.immagineProfilo = immagineProfilo;
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

	@Override
	public int hashCode() {
		return Objects.hash(admin, commentoList, email, id, immagineProfilo, password, username, votazioneList);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Utente other = (Utente) obj;
		return Objects.equals(admin, other.admin) && Objects.equals(commentoList, other.commentoList)
				&& Objects.equals(email, other.email) && Objects.equals(id, other.id)
				&& Objects.equals(immagineProfilo, other.immagineProfilo) && Objects.equals(password, other.password)
				&& Objects.equals(username, other.username) && Objects.equals(votazioneList, other.votazioneList);
	}
	
	
}
