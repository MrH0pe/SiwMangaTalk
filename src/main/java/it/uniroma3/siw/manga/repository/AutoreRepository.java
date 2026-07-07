package it.uniroma3.siw.manga.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.manga.model.Autore;

/**
 * Eredita da CrudRepository i metodi base: findById, findAll, save, delete, ecc.
 */
public interface AutoreRepository extends CrudRepository<Autore, Long> {

	// JOIN FETCH su manga: con findAll() standard l'EAGER del @OneToOne genererebbe
	// una query per ogni autore (problema 1+N); così la lista è UNA sola query.
	@Query("SELECT a FROM Autore a LEFT JOIN FETCH a.manga")
	List<Autore> findAllWithManga();

}
