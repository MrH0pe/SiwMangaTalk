package it.uniroma3.siw.manga.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.manga.model.Autore;
import it.uniroma3.siw.manga.repository.AutoreRepository;


@Service
public class AutoreService {

	@Autowired
	private AutoreRepository autoreRepository;

	/** Restituisce tutti gli autori presenti nel database, con il manga già caricato
	 *  tramite JOIN FETCH (una sola query invece di 1+N). */
	@Transactional(readOnly = true)
	public List<Autore> findAll() {
		return this.autoreRepository.findAllWithManga();
	}

	/** Restituisce l'autore con l'id specificato, oppure null se non esiste. */
	@Transactional(readOnly = true)
	public Autore findById(Long id) {
		return this.autoreRepository.findById(id).orElse(null);
	}

}
