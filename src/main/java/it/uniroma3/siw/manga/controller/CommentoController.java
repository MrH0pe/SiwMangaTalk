package it.uniroma3.siw.manga.controller;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.manga.model.Commento;
import it.uniroma3.siw.manga.model.Manga;
import it.uniroma3.siw.manga.model.User;
import it.uniroma3.siw.manga.service.CommentoService;
import it.uniroma3.siw.manga.service.CredentialsService;
import it.uniroma3.siw.manga.service.MangaService;
import jakarta.validation.Valid;

// Controller per la gestione dei commenti sui manga
@Controller
public class CommentoController {

    private final CommentoService commentoService;
    private final MangaService mangaService;
    private final CredentialsService credentialsService;

    public CommentoController(CommentoService commentoService, MangaService mangaService, CredentialsService credentialsService) {
        this.commentoService = commentoService;
        this.mangaService = mangaService;
        this.credentialsService = credentialsService;
    }
    
    // Visualizza i commenti dell'utente loggato
    @GetMapping("/mieiCommenti")
    public String mostraMieiCommenti(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User utente = credentialsService.getCredentials(username).getUtente();
        List<Commento> commentoList = this.commentoService.findByUtenteId(utente.getId());
        model.addAttribute("listaMieiCommenti", commentoList);
        return "commenti/mieiCommenti";
    }
    
    // Salva un nuovo commento per un manga specifico
    @PostMapping("/manga/{idManga}/commenti")
    public String saveCommento(@PathVariable("idManga") Long idManga, 
                               @Valid @ModelAttribute("commento") Commento commento,
                               BindingResult bindingResult, 
                               Model model) {
        
        Manga manga = mangaService.findById(idManga);
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("manga", manga);
            model.addAttribute("commenti", commentoService.findByMangaId(idManga));
            return "manga/mostraManga";
        }
        
        commento.setManga(manga);
        commento.setTempoPubblicazione(java.time.LocalDateTime.now());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User utente = credentialsService.getCredentials(username).getUtente();
        commento.setUtente(utente);

        this.commentoService.save(commento);
        return "redirect:/mangas/" + idManga;
    }
}