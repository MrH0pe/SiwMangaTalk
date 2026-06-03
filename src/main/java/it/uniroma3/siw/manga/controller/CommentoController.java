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
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.manga.model.Commento;
import it.uniroma3.siw.manga.model.Credentials;
import it.uniroma3.siw.manga.model.Manga;
import it.uniroma3.siw.manga.model.User;
import it.uniroma3.siw.manga.service.CommentoService;
import it.uniroma3.siw.manga.service.CredentialsService;
import it.uniroma3.siw.manga.service.MangaService;
import it.uniroma3.siw.manga.service.ReazioneCommentoService;
import jakarta.validation.Valid;

// Controller per la gestione dei commenti sui manga
@Controller
public class CommentoController {

    private final CommentoService commentoService;
    private final MangaService mangaService;
    private final CredentialsService credentialsService;
    private final ReazioneCommentoService reazioneService;

    public CommentoController(CommentoService commentoService, MangaService mangaService,
            CredentialsService credentialsService, ReazioneCommentoService reazioneService) {
        this.commentoService = commentoService;
        this.mangaService = mangaService;
        this.credentialsService = credentialsService;
        this.reazioneService = reazioneService;
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
            model.addAttribute("commenti", commentoService.findTopLevelByMangaId(idManga));
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

    // Salva una risposta a un commento esistente
    @PostMapping("/manga/{idManga}/commenti/{idCommentoPadre}/risposte")
    public String saveRisposta(@PathVariable("idManga") Long idManga,
                               @PathVariable Long idCommentoPadre,
                               @ModelAttribute Commento risposta,
                               BindingResult bindingResult,
                               Model model) {

        Manga manga = mangaService.findById(idManga);
        Commento commentoPadre = commentoService.findById(idCommentoPadre);

        if (bindingResult.hasErrors() || risposta.getTesto() == null || risposta.getTesto().isBlank()) {
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

    // Aggiunge, cambia o rimuove (toggle) la reazione like/dislike a un commento
    @PostMapping("/commenti/{idCommento}/reazione")
    public String reagisci(@PathVariable Long idCommento, @RequestParam String tipo) {
        Commento commento = commentoService.findById(idCommento);
        if (commento == null) return "redirect:/mangas";
        if (!"LIKE".equals(tipo) && !"DISLIKE".equals(tipo)) {
            return "redirect:/mangas/" + commento.getManga().getId();
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User utente = credentialsService.getCredentials(username).getUtente();
        reazioneService.reagisci(idCommento, utente, tipo);
        return "redirect:/mangas/" + commento.getManga().getId();
    }

    /**
     * Elimina un commento.
     * - Se l'utente ha ruolo ADMIN: può eliminare qualsiasi commento (deleteAsAdmin).
     * - Altrimenti: solo il proprietario può eliminare il proprio commento (deleteIfOwner).
     *
     * Il parametro fromAdmin (opzionale) indica se la richiesta proviene dal
     * pannello admin: in quel caso il redirect torna alla pagina admin del manga.
     */
    /**
     * Elimina un commento.
     * - ADMIN: elimina qualsiasi commento (deleteAsAdmin).
     * - Utente normale: solo il proprio (deleteIfOwner).
     *
     * Parametri di redirect opzionali:
     *   fromAdmin     = true → torna al pannello admin del manga
     *   fromAdminHome = true → torna alla dashboard /admin
     *   (nessuno)           → torna alla pagina pubblica del manga
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
        if (Credentials.ADMIN_ROLE.equals(creds.getRole())) {
            commentoService.deleteAsAdmin(idCommento);
        } else {
            commentoService.deleteIfOwner(idCommento, creds.getUtente().getId());
        }
        if (fromAdminHome) {
            return "redirect:/admin";
        }
        if (fromAdmin) {
            return "redirect:/mangas/admin/" + mangaId;
        }
        return "redirect:/mangas/" + mangaId;
    }
}