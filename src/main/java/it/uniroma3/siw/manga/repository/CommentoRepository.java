package it.uniroma3.siw.manga.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.manga.model.Commento;

/**
 * Eredita da CrudRepository i metodi base: findById, findAll, save, delete, ecc.
 */
public interface CommentoRepository extends CrudRepository<Commento, Long> {

	List<Commento> findByUtenteId(Long utenteId);

	// JOIN FETCH su risposte: carica i commenti principali di un manga insieme alle loro risposte
	// in un'unica query, evitando LazyInitializationException con open-in-view=false.
	// DISTINCT impedisce duplicati: senza di esso ogni commento verrebbe ripetuto
	// tante volte quante sono le sue risposte (comportamento del JOIN).
	@Query("SELECT DISTINCT c FROM Commento c LEFT JOIN FETCH c.risposte WHERE c.manga.id = :mangaId AND c.commentoPadre IS NULL")
	List<Commento> findTopLevelByMangaIdWithRisposte(@Param("mangaId") Long mangaId);
}
