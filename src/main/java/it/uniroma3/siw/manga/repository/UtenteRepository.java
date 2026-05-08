package it.uniroma3.siw.manga.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.manga.model.User;

public interface UtenteRepository extends CrudRepository<User, Long>{

}
