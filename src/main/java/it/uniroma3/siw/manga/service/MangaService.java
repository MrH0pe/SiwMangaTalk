package it.uniroma3.siw.manga.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.manga.model.Manga;
import it.uniroma3.siw.manga.repository.MangaRepository;

/**
 * Service per la gestione dei manga.
 * Fornisce metodi di lettura dal database; la scrittura dei manga
 * avviene tramite il data loader o l'area admin (non ancora implementata).
 */
@Service
public class MangaService {

	private final MangaRepository mangaRepository;

	/** Costruttore con iniezione del repository tramite Spring. */
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
		return this.mangaRepository.findById(id).orElse(null);
	}

}
