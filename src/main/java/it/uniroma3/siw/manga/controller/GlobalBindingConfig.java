package it.uniroma3.siw.manga.controller;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * Configurazione globale del data binding applicata a tutti i controller.
 *
 * @ControllerAdvice fa sì che questa classe venga applicata automaticamente
 * a ogni richiesta HTTP gestita da un @Controller.
 */
@ControllerAdvice
public class GlobalBindingConfig {

    /**
     * Registra un editor personalizzato per i campi String dei form.
     *
     * StringTrimmerEditor(true) fa due cose:
     * 1. Rimuove gli spazi iniziali e finali da ogni stringa (trim)
     * 2. Converte le stringhe vuote (es. "") in null (il parametro "true" abilita questa opzione)
     *
     * Questo evita di salvare nel database stringhe con soli spazi o stringhe vuote
     * al posto di null, riducendo la necessità di validazione manuale nei controller.
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}