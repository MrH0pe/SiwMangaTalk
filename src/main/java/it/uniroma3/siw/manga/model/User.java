package it.uniroma3.siw.manga.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

//Classe che modella un USER
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
	
	//Associazione molti a uno tra User e ImmagineProfilo, un utente ha una sola immagine profilo ma un'immagine profilo può essere associata a più utenti
	@ManyToOne (fetch = FetchType.EAGER)
	private ImmagineProfilo immagineProfilo;
	
	//Associazione uno a molti tra User e Commento, un utente può scrivere più commenti ma un commento è scritto da un solo utente
	@OneToMany (mappedBy = "utente" , fetch = FetchType.LAZY)
	private List<Commento> commentoList;
	
	//Associazione uno a molti tra User e Votazione, un utente può scrivere più votazioni ma una votazione è scritta da un solo utente
	@OneToMany (mappedBy = "utente" , fetch = FetchType.LAZY)
	private List<Votazione> votazioneList;

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

	public String getUsername() {
		return name;
	}

	public void setUsername(String username) {
		this.name = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
		return Objects.hash(commentoList, email, id, immagineProfilo, name, votazioneList);
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
		return Objects.equals(commentoList, other.commentoList) && Objects.equals(email, other.email)
				&& Objects.equals(id, other.id) && Objects.equals(immagineProfilo, other.immagineProfilo)
				&& Objects.equals(name, other.name) && Objects.equals(votazioneList, other.votazioneList);
	}


	
}
