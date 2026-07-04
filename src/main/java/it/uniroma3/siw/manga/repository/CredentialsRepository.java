package it.uniroma3.siw.manga.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.manga.model.Credentials;

/**
 * Eredita da CrudRepository i metodi base: findById, findAll, save, delete, ecc.
 */

public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

	Credentials findByUsername(String username);

}
