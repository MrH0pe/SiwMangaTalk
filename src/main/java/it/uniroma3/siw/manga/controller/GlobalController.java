package it.uniroma3.siw.manga.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


// ControllerAdvice applicato a tutti i controller: espone l'utente loggato come attributo di modello globale
@ControllerAdvice
public class GlobalController {

    // Aggiunge "userDetails" al model di ogni richiesta:
    // contiene i dati dell'utente autenticato, oppure null se l'utente è anonimo
    @ModelAttribute("userDetails")
    public UserDetails getUser() {
        UserDetails user = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            user = (UserDetails) authentication.getPrincipal();
        }

        return user;
    }
}