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

/**
 * Classe che modella un MANGA nel catalogo dell'applicazione.
 *
 * Relazioni:
 * - 1:1 con Autore (lato inverso, la FK è nella tabella autore)
 * - 1:N con Votazione (un manga può ricevere molti voti)
 * - 1:N con Commento (un manga può avere molti commenti)
 *
 * Le relazioni con Votazione e Commento sono LAZY: vengono caricate dal DB
 * solo quando esplicitamente accedute (non incluse in hashCode/equals
 * per evitare LazyInitializationException).
 *
 * È collegato a: AutoreController, MangaController, AdminController
 *               e MangaService (che lo recupera dal DB).
 */
@Entity
public class Manga {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;
	private String descrizione;
	private String pathImmagine;
	private String pathSfondo;

	// Associazione 1 a 1 tra Manga e Autore (lato inverso):
	// un manga è scritto da un solo autore e un autore scrive un solo manga
	@OneToOne(mappedBy = "manga", fetch = FetchType.EAGER)
	private Autore autore;

	// Associazione uno a molti tra Manga e Votazione:
	// un manga può avere più votazioni, ma una votazione appartiene a un solo manga
	@OneToMany(mappedBy = "manga", fetch = FetchType.LAZY)
	private List<Votazione> votazioneList;

	// Associazione uno a molti tra Manga e Commento:
	// un manga può avere più commenti, ma un commento appartiene a un solo manga
	@OneToMany(mappedBy = "manga", fetch = FetchType.LAZY)
	private List<Commento> commentoList;

	// --- Getters e Setters ---

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

	public String getPathSfondo() {
		return pathSfondo;
	}

	public void setPathSfondo(String pathSfondo) {
		this.pathSfondo = pathSfondo;
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

	public List<Commento> getCommentoList() {
		return commentoList;
	}

	public void setCommentoList(List<Commento> commentoList) {
		this.commentoList = commentoList;
	}

	// hashCode e equals basati solo sui campi non-lazy (id, nome, descrizione, path)
	// per evitare LazyInitializationException quando le collezioni non sono caricate
	@Override
	public int hashCode() {
		return Objects.hash(id, nome, descrizione, pathImmagine, pathSfondo);
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
		return Objects.equals(id, other.id)
				&& Objects.equals(nome, other.nome)
				&& Objects.equals(descrizione, other.descrizione)
				&& Objects.equals(pathImmagine, other.pathImmagine)
				&& Objects.equals(pathSfondo, other.pathSfondo);
	}
}
