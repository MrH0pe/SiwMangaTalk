package it.uniroma3.siw.manga.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.manga.model.User;

/**
 * Repository JPA per l'entità User.
 * Eredita da CrudRepository i metodi base: findById, findAll, save, delete, ecc.
 * Non sono necessari metodi personalizzati: la ricerca per username
 * avviene tramite CredentialsRepository (che fa join con la tabella credentials).
 */
public interface UtenteRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
