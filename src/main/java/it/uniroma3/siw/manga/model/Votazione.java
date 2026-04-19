package it.uniroma3.siw.manga.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Votazione {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Long id;
	
	private Integer numeroStelle;
	/* private Immagine IconaManga;   invece delle stelline, per dragon ball ci saranno le sfere del drago come icona*/
	
	@ManyToOne (fetch = FetchType.EAGER)
	private Manga manga;
	
	@ManyToOne (fetch = FetchType.EAGER)
	private Utente utente;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumeroStelle() {
		return numeroStelle;
	}

	public void setNumeroStelle(Integer numeroStelle) {
		this.numeroStelle = numeroStelle;
	}

	public Manga getManga() {
		return manga;
	}

	public void setManga(Manga manga) {
		this.manga = manga;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, manga, numeroStelle, utente);
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
				&& Objects.equals(numeroStelle, other.numeroStelle) && Objects.equals(utente, other.utente);
	}
	
	
}
