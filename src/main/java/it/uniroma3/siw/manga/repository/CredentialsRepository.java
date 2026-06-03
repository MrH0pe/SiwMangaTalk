package it.uniroma3.siw.manga.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.manga.model.Credentials;

/**
 * Repository JPA per l'entità Credentials.
 *
 * Spring Data JPA genera automaticamente le implementazioni SQL.
 * Usato da CredentialsService per il login e la registrazione,
 * e da DataLoader per creare l'utente admin all'avvio.
 */
public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

	/**
	 * Restituisce le credenziali associate all'username specificato,
	 * oppure null se non trovate.
	 * Usato da CredentialsService.getCredentials(String) in tutti i controller
	 * per identificare l'utente loggato a partire dal suo username.
	 */
	Credentials findByUsername(String username);

}
