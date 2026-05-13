package it.uniroma3.siw.manga.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

//Classe che modella una VOTAZIONE
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"utente_id", "manga_id"})
})
public class Votazione {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	// Valore da 0.5 a 5.0 con mezzi punti
	private Double valoreStelline;
	
	//Associazione molti a uno tra Votazione e Manga, una votazione è associata ad un solo manga ma un manga può avere più votazioni
	@ManyToOne (fetch = FetchType.EAGER)
	private Manga manga;
	
	//Associazione molti a uno tra Votazione e User, una votazione è scritta da un solo utente ma un utente può scrivere più votazioni
	@ManyToOne (fetch = FetchType.EAGER)
	private User utente;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getValoreStelline() {
		return valoreStelline;
	}

	public void setValoreStelline(Double valoreStelline) {
		this.valoreStelline = valoreStelline;
	}

	public Manga getManga() {
		return manga;
	}

	public void setManga(Manga manga) {
		this.manga = manga;
	}

	public User getUtente() {
		return utente;
	}

	public void setUtente(User utente) {
		this.utente = utente;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, manga, valoreStelline, utente);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Votazione other = (Votazione) obj;
		return Objects.equals(id, other.id) && Objects.equals(manga, other.manga)
				&& Objects.equals(valoreStelline, other.valoreStelline) && Objects.equals(utente, other.utente);
	}
	
	
}
