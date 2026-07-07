package it.uniroma3.siw.manga.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.manga.model.Votazione;

/**
 * Eredita da CrudRepository i metodi base: findById, findAll, save, delete, ecc.
 */

public interface VotazioneRepository extends CrudRepository<Votazione, Long> {

	Optional<Votazione> findByUtenteIdAndMangaId(Long utenteId, Long mangaId); //Utilizzo Optional perchè gestisce il NULL non causando NullPointerException

	// Query aggregata: la media viene calcolata direttamente dal database.
	// Evita di caricare le entità Votazione (e i loro utente/manga) solo per fare una media in Java:
	// una sola query, nessun problema N+1. Restituisce null se il manga non ha voti.
	@Query("SELECT AVG(v.valoreStelline) FROM Votazione v WHERE v.manga.id = :mangaId")
	Double mediaByMangaId(@Param("mangaId") Long mangaId);

	// Conteggio calcolato dal database, senza materializzare le entità
	long countByMangaId(Long mangaId);

}
