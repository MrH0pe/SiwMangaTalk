package it.uniroma3.siw.manga.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.manga.model.Votazione;

/**
/**
 * Eredita da CrudRepository i metodi base: findById, findAll, save, delete, ecc.
 */
 
public interface VotazioneRepository extends CrudRepository<Votazione, Long> {

	Optional<Votazione> findByUtenteIdAndMangaId(Long utenteId, Long mangaId); //Utilizzo Optional perchè gestisce il NULL non causando NullPointerException


	List<Votazione> findByMangaId(Long mangaId);

}
