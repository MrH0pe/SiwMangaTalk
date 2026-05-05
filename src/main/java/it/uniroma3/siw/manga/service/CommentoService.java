package it.uniroma3.siw.manga.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.manga.model.Commento;
import it.uniroma3.siw.manga.repository.CommentoRepository;

@Service
public class CommentoService {
	//Il service gestisce il repository in automatico
	private CommentoRepository commentoRepository;

	//Costruttore
	public CommentoService(CommentoRepository commentoRepository) {
		this.commentoRepository = commentoRepository;
	}	

	//Metodo per trovare un commento tramite l'ID
	@Transactional (readOnly = true)
	public Commento findById(Long id) {   //Metodi Transactional Read Only
		Commento commento = this.commentoRepository.findById(id).get();
		return commento;
	}

	//Metodo per avere tutti i commenti
	@Transactional (readOnly = true)
	public List<Commento> findAll(){
		List<Commento> commentoList = (List<Commento>) this.commentoRepository.findAll();
		return commentoList;
	}

	//Metodo per salvare nuovi commenti dentro al DB
	public void save(Commento commento) {
		this.commentoRepository.save(commento);
	}

}
