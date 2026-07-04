package it.uniroma3.siw.manga.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.manga.model.Manga;
import it.uniroma3.siw.manga.repository.MangaRepository;

@Service
public class MangaService {

	private final MangaRepository mangaRepository;

	public MangaService(MangaRepository mangaRepository) {
		this.mangaRepository = mangaRepository;
	}

	/** Restituisce tutti i manga presenti nel database. */
	@Transactional(readOnly = true)
	public List<Manga> findAll() {
		return (List<Manga>) this.mangaRepository.findAll();
	}

	/** Restituisce il manga con l'id specificato, oppure null se non esiste. */
	@Transactional(readOnly = true)
	public Manga findById(Long id) {
		Manga manga = this.mangaRepository.findById(id).orElse(null);
		return manga;
	}

}
