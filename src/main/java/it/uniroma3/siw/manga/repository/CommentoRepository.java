package it.uniroma3.siw.manga.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.manga.model.Commento;
import it.uniroma3.siw.manga.model.User;

public interface CommentoRepository extends CrudRepository<Commento, Long>{
	
	List<Commento> findByUtente(User utente);

	List<Commento> findByUtenteId(Long utenteId);

	List<Commento> findByMangaId(Long mangaId);
}
