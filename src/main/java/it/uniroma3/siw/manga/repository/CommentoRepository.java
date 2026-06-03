package it.uniroma3.siw.manga.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.manga.model.Commento;

/**
 * Repository JPA per l'entità Commento.
 * Spring Data genera automaticamente le implementazioni dei metodi
 * sulla base dei nomi dichiarati nell'interfaccia.
 */
public interface CommentoRepository extends CrudRepository<Commento, Long> {

	/** Restituisce tutti i commenti scritti dall'utente con l'id specificato. */
	List<Commento> findByUtenteId(Long utenteId);

<<<<<<< HEAD
	/**
	 * Restituisce solo i commenti principali (commentoPadre = null) di un manga.
	 * Usato per mostrare la lista di commenti nella pagina del manga,
	 * escludendo le risposte (che sono figlie di altri commenti).
	 */
=======
	List<Commento> findByMangaId(Long mangaId);

	//Restituisce solo i commenti principali (senza padre) di un manga
>>>>>>> 5d5dc9cfc21420119f1c688c386c5ddd2463f799
	List<Commento> findByMangaIdAndCommentoPadreIsNull(Long mangaId);
}
