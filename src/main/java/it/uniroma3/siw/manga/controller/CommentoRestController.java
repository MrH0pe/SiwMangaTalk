package it.uniroma3.siw.manga.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.manga.model.Commento;
import it.uniroma3.siw.manga.model.Manga;
import it.uniroma3.siw.manga.model.User;
import it.uniroma3.siw.manga.service.CommentoService;
import it.uniroma3.siw.manga.service.CredentialsService;
import it.uniroma3.siw.manga.service.MangaService;

/**
REACT
 */
@RestController
@RequestMapping("/api")
public class CommentoRestController {

    private final CommentoService commentoService;
    private final MangaService mangaService;
    private final CredentialsService credentialsService;

    /** Costruttore con iniezione dei service tramite Spring. */
    public CommentoRestController(CommentoService commentoService,
                                  MangaService mangaService,
                                  CredentialsService credentialsService) {
        this.commentoService = commentoService;
        this.mangaService = mangaService;
        this.credentialsService = credentialsService;
    }

    /**
     * Salva un nuovo commento principale per un manga e restituisce i dati
     * del commento appena creato come JSON.
     *
     * Richiesta:  POST /api/manga/{mangaId}/commenti
     * Body JSON:  { "testo": "testo del commento" }
     * Risposta:   { "id": 1, "testo": "...", "autore": "Mario", "data": "15/01/2024 10:30" }
     */
    @PostMapping("/manga/{mangaId}/commenti")
    public ResponseEntity<Map<String, Object>> aggiungiCommento(
            @PathVariable Long mangaId,
            @RequestBody Map<String, String> body) {

        // Valida il testo del commento (trim().isEmpty() invece di isBlank() per compatibilità Java 7+)
        String testo = body.get("testo");
        if (testo == null || testo.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Recupera il manga; restituisce 404 se non esiste
        Manga manga = mangaService.findById(mangaId);
        if (manga == null) {
            return ResponseEntity.notFound().build();
        }

        // Recupera l'utente corrente dal contesto di sicurezza di Spring
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User utente = credentialsService.getCredentials(username).getUtente();

        // Costruisce e salva il commento
        Commento commento = new Commento();
        commento.setTesto(testo);
        commento.setManga(manga);
        commento.setUtente(utente);
        commento.setTempoPubblicazione(LocalDateTime.now());
        commentoService.save(commento);

        // Costruisce la risposta JSON con i dati da mostrare nel componente React
        Map<String, Object> risposta = new HashMap<>();
        risposta.put("id", commento.getId());
        risposta.put("testo", commento.getTesto());
        risposta.put("autore", utente.getName());
        risposta.put("data", commento.getTempoPubblicazione()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        return ResponseEntity.ok(risposta);
    }

    /**
     * Modifica il testo di un commento se l'utente corrente ne è il proprietario.
     *
     * Richiesta:  PATCH /api/commenti/{commentoId}/modifica
     * Body JSON:  { "testo": "nuovo testo" }
     * Risposta:   { "testo": "nuovo testo" }   — 200 OK
     *             400 Bad Request               — testo vuoto/blank
     *             403 Forbidden                 — utente non proprietario del commento
     */
    @PatchMapping("/commenti/{commentoId}/modifica")
    public ResponseEntity<Map<String, Object>> modificaCommento(
            @PathVariable Long commentoId,
            @RequestBody Map<String, String> body) {

        String nuovoTesto = body.get("testo");
        if (nuovoTesto == null || nuovoTesto.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User utente = credentialsService.getCredentials(username).getUtente();

        boolean aggiornato = commentoService.updateIfOwner(commentoId, utente.getId(), nuovoTesto);
        if (!aggiornato) {
            return ResponseEntity.status(403).build();
        }

        Map<String, Object> risposta = new HashMap<>();
        risposta.put("testo", nuovoTesto.trim());
        return ResponseEntity.ok(risposta);
    }

    /**
     * Salva una risposta a un commento esistente e restituisce i dati come JSON.
     *
     * Richiesta:  POST /api/commenti/{commentoId}/risposte
     * Body JSON:  { "testo": "testo della risposta" }
     * Risposta:   { "id": 2, "testo": "...", "autore": "Mario", "data": "15/01/2024 10:30" }
     *
     * Il manga della risposta viene ereditato dal commento padre.
     */
    @PostMapping("/commenti/{commentoId}/risposte")
    public ResponseEntity<Map<String, Object>> aggiungiRisposta(
            @PathVariable Long commentoId,
            @RequestBody Map<String, String> body) {

        // Valida il testo della risposta
        String testo = body.get("testo");
        if (testo == null || testo.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Recupera il commento padre; restituisce 404 se non esiste
        Commento padre = commentoService.findById(commentoId);
        if (padre == null) {
            return ResponseEntity.notFound().build();
        }

        // Recupera l'utente corrente dal contesto di sicurezza di Spring
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User utente = credentialsService.getCredentials(username).getUtente();

        // Costruisce e salva la risposta (eredita il manga dal commento padre)
        Commento risposta = new Commento();
        risposta.setTesto(testo);
        risposta.setManga(padre.getManga());
        risposta.setUtente(utente);
        risposta.setCommentoPadre(padre);
        risposta.setTempoPubblicazione(LocalDateTime.now());
        commentoService.save(risposta);

        // Costruisce la risposta JSON con i dati da mostrare nel componente React
        Map<String, Object> json = new HashMap<>();
        json.put("id",     risposta.getId());
        json.put("testo",  risposta.getTesto());
        json.put("autore", utente.getName());
        json.put("data",   risposta.getTempoPubblicazione()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        return ResponseEntity.ok(json);
    }
}
