package it.uniroma3.siw.manga.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.manga.model.Credentials;

public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

	// Restituisce le credenziali associate all'username, null se non trovate
	Credentials findByUsername(String username);

}
