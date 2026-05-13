package it.uniroma3.siw.manga.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.manga.model.Autore;

/**
 * Repository JPA per l'entità Autore.
 * Eredita da CrudRepository i metodi base: findById, findAll, save, delete, ecc.
 * Non sono necessari metodi personalizzati perché si usano solo le operazioni standard.
 */
public interface AutoreRepository extends CrudRepository<Autore, Long> {

}
