package it.uniroma3.siw.manga.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Manga {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Long id;
	
	private String nome;
	private String descrizione;
	private String pathImmagine;
	
	@OneToOne (mappedBy = "manga" , fetch = FetchType.EAGER)
	private Autore autore;
	
	@OneToMany (mappedBy = "manga" , fetch = FetchType.EAGER)
	private List<Votazione> votazioneList;
	
	@OneToMany (mappedBy = "manga" , fetch = FetchType.EAGER)
	private List<Thread> threadList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getPathImmagine() {
		return pathImmagine;
	}

	public void setPathImmagine(String pathImmagine) {
		this.pathImmagine = pathImmagine;
	}

	public Autore getAutore() {
		return autore;
	}

	public void setAutore(Autore autore) {
		this.autore = autore;
	}

	public List<Votazione> getVotazioneList() {
		return votazioneList;
	}

	public void setVotazioneList(List<Votazione> votazioneList) {
		this.votazioneList = votazioneList;
	}

	public List<Thread> getThreadList() {
		return threadList;
	}

	public void setThreadList(List<Thread> threadList) {
		this.threadList = threadList;
	}

	@Override
	public int hashCode() {
		return Objects.hash(autore, descrizione, id, nome, pathImmagine, threadList, votazioneList);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Manga other = (Manga) obj;
		return Objects.equals(autore, other.autore) && Objects.equals(descrizione, other.descrizione)
				&& Objects.equals(id, other.id) && Objects.equals(nome, other.nome)
				&& Objects.equals(pathImmagine, other.pathImmagine) && Objects.equals(threadList, other.threadList)
				&& Objects.equals(votazioneList, other.votazioneList);
	}
	
	
}
