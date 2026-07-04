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

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}