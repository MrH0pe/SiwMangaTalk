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
public class Thread {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Long id;
	
	private Integer numeroCommenti;
	private Integer numeriLike;
	
	@OneToMany (mappedBy = "thread" , fetch = FetchType.LAZY)
	private List<Commento> commentoList;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Manga manga;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumeroCommenti() {
		return numeroCommenti;
	}

	public void setNumeroCommenti(Integer numeroCommenti) {
		this.numeroCommenti = numeroCommenti;
	}

	public Integer getNumeriLike() {
		return numeriLike;
	}

	public void setNumeriLike(Integer numeriLike) {
		this.numeriLike = numeriLike;
	}

	public List<Commento> getCommentoList() {
		return commentoList;
	}

	public void setCommentoList(List<Commento> commentoList) {
		this.commentoList = commentoList;
	}

	public Manga getManga() {
		return manga;
	}

	public void setManga(Manga manga) {
		this.manga = manga;
	}

	@Override
	public int hashCode() {
		return Objects.hash(commentoList, id, manga, numeriLike, numeroCommenti);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Thread other = (Thread) obj;
		return Objects.equals(commentoList, other.commentoList) && Objects.equals(id, other.id)
				&& Objects.equals(manga, other.manga) && Objects.equals(numeriLike, other.numeriLike)
				&& Objects.equals(numeroCommenti, other.numeroCommenti);
	}
	
	
}
