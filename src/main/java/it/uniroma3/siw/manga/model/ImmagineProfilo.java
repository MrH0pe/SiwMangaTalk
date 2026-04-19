package it.uniroma3.siw.manga.model;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class ImmagineProfilo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Long id;
	
	private String nomeImmagine;
	private String pathImmagine;
	
	@OneToMany (mappedBy = "immagineProfilo" , fetch = FetchType.LAZY)
	private Set<Utente> utente;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeImmagine() {
		return nomeImmagine;
	}

	public void setNomeImmagine(String nomeImmagine) {
		this.nomeImmagine = nomeImmagine;
	}

	public String getPathImmagine() {
		return pathImmagine;
	}

	public void setPathImmagine(String pathImmagine) {
		this.pathImmagine = pathImmagine;
	}

	public Set<Utente> getUtente() {
		return utente;
	}

	public void setUtente(Set<Utente> utente) {
		this.utente = utente;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nomeImmagine, pathImmagine, utente);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImmagineProfilo other = (ImmagineProfilo) obj;
		return Objects.equals(id, other.id) && Objects.equals(nomeImmagine, other.nomeImmagine)
				&& Objects.equals(pathImmagine, other.pathImmagine) && Objects.equals(utente, other.utente);
	}
	
	
}
