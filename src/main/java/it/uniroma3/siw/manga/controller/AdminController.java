package it.uniroma3.siw.manga.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import it.uniroma3.siw.manga.model.Commento;
import it.uniroma3.siw.manga.model.Manga;
import it.uniroma3.siw.manga.service.CommentoService;
import it.uniroma3.siw.manga.service.MangaService;
import it.uniroma3.siw.manga.service.VotazioneService;

@Controller
@RequestMapping("/mangas/admin")
public class AdminController {

    private final MangaService mangaService;
    private final CommentoService commentoService;
    private final VotazioneService votazioneService;

    /** Costruttore con iniezione dei service tramite Spring. */
    public AdminController(MangaService mangaService,
                           CommentoService commentoService,
                           VotazioneService votazioneService) {
        this.mangaService = mangaService;
        this.commentoService = commentoService;
        this.votazioneService = votazioneService;
    }

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
            return "redirect:/mangas";
        }

        List<Commento> commenti = commentoService.findTopLevelByMangaId(id);

        model.addAttribute("manga",      manga);
        model.addAttribute("commenti",   commenti);
        model.addAttribute("mediaVoti",  votazioneService.getMediaVoti(id));
        model.addAttribute("countVoti",  votazioneService.countVoti(id));

        return "manga/adminManga";
    }
}
