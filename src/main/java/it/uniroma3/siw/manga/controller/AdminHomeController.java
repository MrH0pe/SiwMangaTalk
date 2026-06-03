package it.uniroma3.siw.manga.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.manga.model.Commento;
import it.uniroma3.siw.manga.model.Credentials;
import it.uniroma3.siw.manga.repository.CredentialsRepository;
import it.uniroma3.siw.manga.service.CommentoService;

/**
 * Controller per la dashboard principale dell'amministratore.
 *
 * Accessibile solo agli utenti con ruolo ADMIN (protezione in SecurityConfiguration).
 * Mostra tutti gli utenti registrati con i loro commenti e i pulsanti di eliminazione.
 */
@Controller
public class AdminHomeController {

    private final CredentialsRepository credentialsRepository;
    private final CommentoService commentoService;

    /** Costruttore con iniezione delle dipendenze tramite Spring. */
    public AdminHomeController(CredentialsRepository credentialsRepository,
                               CommentoService commentoService) {
        this.credentialsRepository = credentialsRepository;
        this.commentoService = commentoService;
    }

    /**
     * Mostra la dashboard admin con la lista degli utenti e i loro commenti.
     *
     * Per ogni utente non-admin viene costruita una mappa con:
     * - "username" → username dell'utente
     * - "commenti" → lista di tutti i suoi commenti (incluse risposte), dal più recente
     * - "totale"   → numero totale di commenti
     *
     * @param model model Spring MVC
     * @return il template admin/adminHome
     */
    @GetMapping("/admin")
    public String adminHome(Model model) {

        List<Map<String, Object>> utenti = new ArrayList<>();
        for (Credentials creds : credentialsRepository.findAll()) {
            if (Credentials.ADMIN_ROLE.equals(creds.getRole())) continue;
            if (creds.getUtente() == null) continue;

            List<Commento> commenti = commentoService.findByUtenteId(creds.getUtente().getId());
            commenti.sort((a, b) -> b.getTempoPubblicazione().compareTo(a.getTempoPubblicazione()));

            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("username", creds.getUsername());
            entry.put("commenti", commenti);
            entry.put("totale",   commenti.size());
            utenti.add(entry);
        }
        utenti.sort((a, b) -> ((String) a.get("username"))
                .compareToIgnoreCase((String) b.get("username")));

        model.addAttribute("utenti", utenti);
        return "admin/adminHome";
    }
}
