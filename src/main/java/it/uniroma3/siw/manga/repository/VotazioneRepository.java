package it.uniroma3.siw.manga.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.manga.model.Votazione;

/**
 * Repository JPA per l'entità Votazione.
 *
 * Spring Data JPA genera automaticamente le implementazioni SQL.
 * Usato esclusivamente da VotazioneService per gestire i voti con stelle.
 */
public interface VotazioneRepository extends CrudRepository<Votazione, Long> {

	/**
	 * Cerca il voto di uno specifico utente per uno specifico manga.
	 * Usato in VotazioneService per la logica di upsert (aggiorna se esiste,
	 * crea nuovo se non esiste) e per mostrare il voto dell'utente nella view.
	 */
	Optional<Votazione> findByUtenteIdAndMangaId(Long utenteId, Long mangaId);

	/**
	 * Restituisce tutti i voti ricevuti da un manga.
	 * Usato da VotazioneService per calcolare la media e il conteggio dei voti.
	 */
	List<Votazione> findByMangaId(Long mangaId);

}
