package it.uniroma3.siw.manga.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import it.uniroma3.siw.manga.model.Commento;
import it.uniroma3.siw.manga.model.Manga;
import it.uniroma3.siw.manga.service.CommentoService;
import it.uniroma3.siw.manga.service.MangaService;
import it.uniroma3.siw.manga.service.VotazioneService;

@Controller
@RequestMapping("/mangas/admin")
public class AdminController {

    @Autowired
    private MangaService mangaService;
    @Autowired
    private CommentoService commentoService;
    @Autowired
    private VotazioneService votazioneService;

    /**
     * Mostra il pannello di amministrazione per un manga specifico.
     *
     * Carica nel model:
     * - il manga e tutti i suoi commenti principali (con le relative risposte)
     * - media e numero totale voti
     *
     * @param id    ID del manga da gestire
     * @param model model Spring MVC
     * @return il template adminManga
     */
    @GetMapping("/{id}")
    public String adminManga(@PathVariable Long id, Model model) {
        Manga manga = mangaService.findById(id);
        if (manga == null) {
            //Se non esiste quell'ID, risponde 404 → Spring Boot mostra error/404.html
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Manga non trovato");
        }

        List<Commento> commenti = commentoService.findTopLevelByMangaId(id);

        model.addAttribute("manga",      manga);
        model.addAttribute("commenti",   commenti);
        model.addAttribute("mediaVoti",  votazioneService.getMediaVoti(id));
        model.addAttribute("countVoti",  votazioneService.countVoti(id));

        return "manga/adminManga";
    }
}
