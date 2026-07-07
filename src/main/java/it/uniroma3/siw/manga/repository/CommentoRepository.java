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

	// JOIN FETCH su manga e commentoPadre (entrambi to-one): la pagina "I miei commenti"
	// e la dashboard admin mostrano il nome del manga e il tipo (commento/risposta).
	// Senza fetch, l'EAGER di default genererebbe una query per ogni manga/padre distinto (N+1).
	@Query("SELECT c FROM Commento c LEFT JOIN FETCH c.manga LEFT JOIN FETCH c.commentoPadre WHERE c.utente.id = :utenteId")
	List<Commento> findByUtenteId(@Param("utenteId") Long utenteId);

	// JOIN FETCH su risposte: carica i commenti principali di un manga insieme alle loro risposte
	// in un'unica query, evitando LazyInitializationException con open-in-view=false.
	// DISTINCT impedisce duplicati: senza di esso ogni commento verrebbe ripetuto
	// tante volte quante sono le sue risposte (comportamento del JOIN).
	// JOIN FETCH anche su c.utente e r.utente (to-one): la pagina mostra l'autore di ogni
	// commento e risposta; senza fetch si avrebbe una query per ogni utente distinto (N+1).
	// Nota: la collezione (bag) fetchata resta UNA sola (risposte), quindi nessuna
	// MultipleBagFetchException.
	@Query("SELECT DISTINCT c FROM Commento c LEFT JOIN FETCH c.risposte r LEFT JOIN FETCH c.utente LEFT JOIN FETCH r.utente WHERE c.manga.id = :mangaId AND c.commentoPadre IS NULL")
	List<Commento> findTopLevelByMangaIdWithRisposte(@Param("mangaId") Long mangaId);
}
