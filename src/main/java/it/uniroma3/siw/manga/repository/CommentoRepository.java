package it.uniroma3.siw.manga.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.manga.model.Commento;

/**
 * Repository JPA per l'entità Commento.
 *
 * Spring Data JPA genera automaticamente le implementazioni SQL
 * sulla base dei nomi dichiarati nell'interfaccia (query by method name).
 *
 * È collegato a: CommentoService (che usa questo repository per tutte le operazioni DB)
 */
public interface CommentoRepository extends CrudRepository<Commento, Long> {

	/**
	 * Restituisce tutti i commenti scritti dall'utente con l'id specificato.
	 * Usato nella pagina "I miei commenti" tramite CommentoService.findByUtenteId().
	 */
	List<Commento> findByUtenteId(Long utenteId);

	/**
	 * Restituisce solo i commenti principali (commentoPadre = null) di un manga.
	 * Usato per mostrare la lista di commenti nella pagina del manga,
	 * escludendo le risposte (che sono figlie di altri commenti e già incluse
	 * nella collection 'risposte' di ogni Commento).
	 */
	List<Commento> findByMangaIdAndCommentoPadreIsNull(Long mangaId);
}
