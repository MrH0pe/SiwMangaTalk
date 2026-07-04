package it.uniroma3.siw.manga.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.manga.model.User;

/**
 * Eredita da CrudRepository i metodi base: findById, findAll, save, delete, ecc.
 */
public interface UtenteRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);   //Utilizzo Optional perchè gestisce il NULL non causando NullPointerException

}
