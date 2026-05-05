package it.uniroma3.siw.manga.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.manga.model.Manga;

public interface MangaRepository extends CrudRepository<Manga, Long>{

}
