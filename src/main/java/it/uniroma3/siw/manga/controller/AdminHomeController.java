package it.uniroma3.siw.manga.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.manga.model.Commento;
import it.uniroma3.siw.manga.model.Credentials;
import it.uniroma3.siw.manga.service.CommentoService;
import it.uniroma3.siw.manga.service.CredentialsService;

@Controller
public class AdminHomeController {

    @Autowired
    private CredentialsService credentialsService;
    @Autowired
    private CommentoService commentoService;

    @GetMapping("/admin")
    public String adminHome(Model model) {
        List<Map<String, Object>> utenti = new ArrayList<>();

        // Accede alle credenziali tramite il service, non direttamente dal repository
        for (Credentials creds : credentialsService.getAllNonAdmin()) {
            List<Commento> commenti = commentoService.findByUtenteId(creds.getUtente().getId());

            // Ordina commenti per data decrescente
            commenti.sort(new Comparator<Commento>() {
                @Override
                public int compare(Commento a, Commento b) {
                    return b.getTempoPubblicazione().compareTo(a.getTempoPubblicazione());
                }
            });

            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("username", creds.getUsername());
            entry.put("commenti", commenti);
            entry.put("totale", commenti.size());
            utenti.add(entry);
        }

        // Ordina utenti per username alfabetico
        utenti.sort(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> a, Map<String, Object> b) {
                return ((String) a.get("username"))
                        .compareToIgnoreCase((String) b.get("username"));
            }
        });

        model.addAttribute("utenti", utenti);
        return "admin/adminHome";
    }
}
