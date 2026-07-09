package it.uniroma3.siw.manga.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.manga.model.Commento;
import it.uniroma3.siw.manga.model.Credentials;
import it.uniroma3.siw.manga.model.Manga;
import it.uniroma3.siw.manga.model.User;
import it.uniroma3.siw.manga.service.CommentoService;
import it.uniroma3.siw.manga.service.CredentialsService;
import it.uniroma3.siw.manga.service.MangaService;
import jakarta.validation.Valid;

@Controller
public class CommentoController {

    @Autowired
    private CommentoService commentoService;
    @Autowired
    private MangaService mangaService;
    @Autowired
    private CredentialsService credentialsService;

    /**
     * Mostra la pagina "I miei commenti" dell'utente loggato (GET /mieiCommenti).
     * Recupera tutti i commenti scritti dall'utente e li passa al template.
     *
     * Il template usato è: commenti/mieiCommenti.html
     */
    @GetMapping("/mieiCommenti")
    public String mostraMieiCommenti(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User utente = credentialsService.getCredentials(username).getUtente();
        List<Commento> commentoList = this.commentoService.findByUtenteId(utente.getId());
        model.addAttribute("listaMieiCommenti", commentoList);
        return "commenti/mieiCommenti";
    }

    /**
     * Salva un nuovo commento principale su un manga (POST /manga/{idManga}/commenti).
     * Valida il testo tramite @Valid; in caso di errori ricarica la pagina del manga.
     * Imposta automaticamente l'utente corrente e la data/ora di pubblicazione.
     *
     * Delegato a: CommentoService.save()
     */
    @PostMapping("/manga/{idManga}/commenti")
    public String saveCommento(@PathVariable Long idManga,
                               @Valid @ModelAttribute("commento") Commento commento,
                               BindingResult bindingResult,
                               Model model) {

        Manga manga = mangaService.findById(idManga);
        if (manga == null) return "redirect:/mangas";

        if (bindingResult.hasErrors()) {
            model.addAttribute("manga", manga);
            model.addAttribute("commenti", commentoService.findTopLevelByMangaId(idManga));
            return "manga/mostraManga";
        }

        //Se non ci sono errori, salvi il commento nel DB
        commento.setManga(manga);
        commento.setTempoPubblicazione(java.time.LocalDateTime.now());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User utente = credentialsService.getCredentials(username).getUtente();
        commento.setUtente(utente);

        this.commentoService.save(commento);
        return "redirect:/mangas/" + idManga;
    }

    /**
     * Salva una risposta a un commento esistente (POST /manga/{idManga}/commenti/{idCommentoPadre}/risposte).
     * Collega la risposta al commento padre tramite setCommentoPadre().
     * Se il testo è vuoto, ricarica la pagina del manga senza salvare.
     *
     * Delegato a: CommentoService.save()
     */
    @PostMapping("/manga/{idManga}/commenti/{idCommentoPadre}/risposte")
    public String saveRisposta(@PathVariable Long idManga,
                               @PathVariable Long idCommentoPadre,
                               @ModelAttribute Commento risposta,
                               BindingResult bindingResult,
                               Model model) {

        Manga manga = mangaService.findById(idManga);
        if (manga == null) return "redirect:/mangas";

        Commento commentoPadre = commentoService.findById(idCommentoPadre);
        // Senza padre valido la "risposta" diventerebbe un commento principale: meglio annullare
        if (commentoPadre == null) return "redirect:/mangas/" + idManga;

        if (bindingResult.hasErrors() || risposta.getTesto() == null || risposta.getTesto().trim().isEmpty()) {
            model.addAttribute("manga", manga);
            model.addAttribute("commenti", commentoService.findTopLevelByMangaId(idManga));
            return "manga/mostraManga";
        }

        risposta.setManga(manga);
        risposta.setCommentoPadre(commentoPadre);
        risposta.setTempoPubblicazione(java.time.LocalDateTime.now());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User utente = credentialsService.getCredentials(username).getUtente();
        risposta.setUtente(utente);

        this.commentoService.save(risposta);
        return "redirect:/mangas/" + idManga;
    }

    /**
     * Elimina un commento (POST /commenti/{idCommento}/elimina).
     *
     * Logica di cancellazione:
     * - Se l'utente ha ruolo ADMIN: può eliminare qualsiasi commento (deleteAsAdmin).
     * - Altrimenti: solo il proprietario può eliminare il proprio commento (deleteIfOwner).
     *
     * Parametri di redirect opzionali:
     *   fromAdmin     = true → torna al pannello admin del manga (/mangas/admin/{id})
     *   fromAdminHome = true → torna alla dashboard admin (/admin)
     *   (nessuno)           → torna alla pagina pubblica del manga (/mangas/{id})
     *
     * Delegato a: CommentoService.deleteAsAdmin() oppure CommentoService.deleteIfOwner()
     */
    @PostMapping("/commenti/{idCommento}/elimina")
    public String eliminaCommento(@PathVariable Long idCommento,
                                  @RequestParam(required = false, defaultValue = "false") boolean fromAdmin,
                                  @RequestParam(required = false, defaultValue = "false") boolean fromAdminHome) {

        Commento commento = commentoService.findById(idCommento);


        if (commento == null) return "redirect:/mangas";


        Long mangaId = commento.getManga().getId();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Credentials creds = credentialsService.getCredentials(username);
        // La scelta tra deleteAsAdmin e deleteIfOwner è logica di business: delegata al service
        commentoService.delete(idCommento, creds);
        if (fromAdminHome) {
            return "redirect:/admin";
        }
        if (fromAdmin) {
            return "redirect:/mangas/admin/" + mangaId;
        }



        return "redirect:/mangas/" + mangaId;
    }
}
