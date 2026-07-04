package it.uniroma3.siw.manga.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.manga.model.Autore;

/**
 * Eredita da CrudRepository i metodi base: findById, findAll, save, delete, ecc.
 */
public interface AutoreRepository extends CrudRepository<Autore, Long> {
	
}
