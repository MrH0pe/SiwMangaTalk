package it.uniroma3.siw.manga.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Autore {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Long id;
	
	private String nome;
	private String cognome;
	private String pathImmagine;
	private String descrizione;
	
	
	@OneToOne (mappedBy = "autore" , fetch = FetchType.EAGER)
	private Manga manga;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getPathImmagine() {
		return pathImmagine;
	}
	public void setPathImmagine(String pathImmagine) {
		this.pathImmagine = pathImmagine;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public Manga getManga() {
		return manga;
	}
	public void setManga(Manga manga) {
		this.manga = manga;
	}
	@Override
	public int hashCode() {
		return Objects.hash(cognome, descrizione, manga, nome, pathImmagine);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Autore other = (Autore) obj;
		return Objects.equals(cognome, other.cognome) && Objects.equals(descrizione, other.descrizione)
				&& Objects.equals(manga, other.manga) && Objects.equals(nome, other.nome)
				&& Objects.equals(pathImmagine, other.pathImmagine);
	}
	
	
}
