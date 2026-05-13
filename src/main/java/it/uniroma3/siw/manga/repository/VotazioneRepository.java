package it.uniroma3.siw.manga.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.manga.model.Votazione;

public interface VotazioneRepository extends CrudRepository<Votazione, Long> {

	// Restituisce il voto di un utente per un determinato manga
	Optional<Votazione> findByUtenteIdAndMangaId(Long utenteId, Long mangaId);

	// Restituisce tutti i voti di un manga
	List<Votazione> findByMangaId(Long mangaId);

}
