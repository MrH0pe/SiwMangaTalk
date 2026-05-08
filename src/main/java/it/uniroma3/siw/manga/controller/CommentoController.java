package it.uniroma3.siw.manga.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.manga.model.Commento;
import it.uniroma3.siw.manga.model.Manga;
import it.uniroma3.siw.manga.service.CommentoService;
import it.uniroma3.siw.manga.service.MangaService;
import jakarta.validation.Valid;

@Controller
public class CommentoController {
    
    private CommentoService commentoService;
    private MangaService mangaService; // Aggiunto per recuperare il manga
    
    // Costruttore per la Dependency Injection (Best practice in Spring)
    public CommentoController(CommentoService commentoService, MangaService mangaService) {
        this.commentoService = commentoService;
        this.mangaService = mangaService;
    }
    
    // Corretto: aggiunto {id} nel path
    @GetMapping("/mieiCommenti/{id}")
    public String mostraMieiCommenti(@PathVariable("id") Long id, Model model) {
        List<Commento> commentoList = this.commentoService.findByUtenteId(id);
        model.addAttribute("listaMieiCommenti", commentoList);
        return "mieiCommenti";
    }
    
    // Corretto: passiamo l'ID del manga nell'URL e gestiamo la validazione
 // Dentro CommentoController.java

    @PostMapping("/manga/{idManga}/commenti")
    public String saveCommento(@PathVariable("idManga") Long idManga, 
                               @Valid @ModelAttribute("commento") Commento commento,
                               BindingResult bindingResult, 
                               Model model) {
        
        Manga manga = mangaService.findById(idManga);
        
        // Se c'è un errore di validazione (es. commento vuoto), ricarichiamo la pagina
        if (bindingResult.hasErrors()) {
            model.addAttribute("manga", manga);
            return "manga/mostraManga"; 
        }
        
        // Associamo il commento al manga corrispondente
        commento.setManga(manga);
        
        // Impostiamo la data odierna (utilizzando java.sql.Date come nel tuo Model)
        commento.setTempoPubblicazione(new java.sql.Date(System.currentTimeMillis()));
        
        // TODO: Quando implementerai l'autenticazione, qui legherai il commento all'utente
        // Utente utenteLoggato = ...;
        // commento.setUtente(utenteLoggato);
        
        // Salviamo nel DB!
        this.commentoService.save(commento);
        
        // Post-Redirect-Get: rimandiamo l'utente alla pagina del manga appena commentato
        return "redirect:/manga/" + idManga;
    }
}