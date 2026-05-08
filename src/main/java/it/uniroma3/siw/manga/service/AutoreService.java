package it.uniroma3.siw.manga.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.manga.model.Autore;
import it.uniroma3.siw.manga.repository.AutoreRepository;

@Service
public class AutoreService {
	private final AutoreRepository autoreRepository;

	public AutoreService(AutoreRepository autoreRepository) {
		this.autoreRepository = autoreRepository;
	}

	//Restituisce tutti gli autori in una lista
	@Transactional (readOnly = true)
	public List<Autore> findAll() {
		return (List<Autore>) this.autoreRepository.findAll();

	}

	@Transactional(readOnly = true)
	public Autore findById(Long id) {
		return this.autoreRepository.findById(id).orElse(null);
	}

}
