package it.uniroma3.siw.manga.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.manga.model.Manga;

/**
 * Eredita da CrudRepository i metodi base: findById, findAll, save, delete, ecc.
 */

public interface MangaRepository extends CrudRepository<Manga, Long> {

	// JOIN FETCH su autore: Manga.autore è il lato inverso di un @OneToOne, che non può
	// essere reso davvero LAZY (Hibernate deve comunque interrogare per sapere se è null).
	// Con findAll() standard l'EAGER genererebbe una query per ogni manga (problema 1+N);
	// con il fetch join la lista viene caricata con UNA sola query.
	@Query("SELECT m FROM Manga m LEFT JOIN FETCH m.autore")
	List<Manga> findAllWithAutore();

	Optional<Manga> findByNome(String nome);


}
