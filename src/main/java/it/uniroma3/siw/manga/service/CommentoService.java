package it.uniroma3.siw.manga.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.manga.model.Commento;
import it.uniroma3.siw.manga.model.User;
import it.uniroma3.siw.manga.repository.CommentoRepository;

@Service
public class CommentoService {
    
    private final CommentoRepository commentoRepository;

    public CommentoService(CommentoRepository commentoRepository) {
        this.commentoRepository = commentoRepository;
    }	

    @Transactional(readOnly = true)
    public Commento findById(Long id) {   
        return this.commentoRepository.findById(id).orElse(null); // Meglio usare orElse(null) invece di .get() per evitare eccezioni se non lo trova
    }

    // Rinominato in getCommenti per chiarezza e cambiato il tipo di ritorno
    @Transactional(readOnly = true)
    public List<Commento> getCommenti(User utente) {
        return this.commentoRepository.findByUtente(utente);
    }
	
    @Transactional(readOnly = true)
    public List<Commento> findAll(){
        return (List<Commento>) this.commentoRepository.findAll();
    }
	
    @Transactional(readOnly = true)
    public List<Commento> findByUtenteId(Long id){
        return this.commentoRepository.findByUtenteId(id);
    }
	
    @Transactional
    public void save(Commento commento) {
        this.commentoRepository.save(commento);
    }
}