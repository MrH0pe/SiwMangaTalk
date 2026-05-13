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


    //Restituisce un commento dal id
    @Transactional(readOnly = true)
    public Commento findById(Long id) {
        return this.commentoRepository.findById(id).orElse(null);
    }


    //Restituisce i commenti di un utente
    @Transactional(readOnly = true)
    public List<Commento> getCommenti(User utente) {
        return this.commentoRepository.findByUtente(utente);
    }


    //Restituisce l'elenco dei commenti
    @Transactional(readOnly = true)
    public List<Commento> findAll(){
        return (List<Commento>) this.commentoRepository.findAll();
    }


    //Restituisce l'elenco dei commenti di un utente attraverso il suo id
    @Transactional(readOnly = true)
    public List<Commento> findByUtenteId(Long id){
        return this.commentoRepository.findByUtenteId(id);
    }


    //Restituisce tutti i commenti di un manga attraverso l'id del manga stesso
    @Transactional(readOnly = true)
    public List<Commento> findByMangaId(Long mangaId) {
        return this.commentoRepository.findByMangaId(mangaId);
    }


    //salva il commento
    @Transactional
    public void save(Commento commento) {
        this.commentoRepository.save(commento);
    }
}