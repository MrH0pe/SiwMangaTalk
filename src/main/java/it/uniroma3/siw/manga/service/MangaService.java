package it.uniroma3.siw.manga.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.manga.model.Manga;
import it.uniroma3.siw.manga.repository.MangaRepository;

@Service
public class MangaService {
	private MangaRepository mangaRepository;

	public MangaService(MangaRepository mangaRepository) {
		this.mangaRepository = mangaRepository;
	}
	
	//Restituisce tutti i manga in una lista
	@Transactional (readOnly = true)
	public List<Manga> findAll() {
		return (List<Manga>) this.mangaRepository.findAll();

	}
	
	@Transactional(readOnly = true)
	public Manga findById(Long id) {
		return this.mangaRepository.findById(id).orElse(null);
	}
}
