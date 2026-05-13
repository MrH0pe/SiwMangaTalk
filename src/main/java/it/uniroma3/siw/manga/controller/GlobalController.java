package it.uniroma3.siw.manga.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


// Classe globale valida per tutti i controller
@ControllerAdvice
public class GlobalController {
    @ModelAttribute("userDetails")

    public UserDetails getUser() {
        UserDetails user = null;

        // Recupera l'utente attualmente autenticato
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Se l'utente non è anonimo, prende i suoi dettagli
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }


        // Restituisce l'utente loggato, oppure null
        return user;

    }
}