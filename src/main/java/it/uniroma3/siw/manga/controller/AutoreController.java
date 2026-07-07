package it.uniroma3.siw.manga.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.manga.model.Autore;
import it.uniroma3.siw.manga.service.AutoreService;

/**
 * Controller per la gestione delle pagine relative agli autori.
 *
 * Gestisce:
 * - la lista di tutti gli autori (GET /autori) → template autori/listaAutori.html
 * - il dettaglio di un autore (GET /autori/{id}) → template autori/mostraAutore.html
 *
 * Accessibile a tutti (anche utenti non loggati), come configurato in SecurityConfiguration.
 * È collegato a: AutoreService (per il recupero dal DB), Autore (model)
 */
@Controller
public class AutoreController {
	
	private final AutoreService autoreService;

	/** Costruttore con iniezione del service tramite Spring. */
	public AutoreController(AutoreService autoreService) {
		this.autoreService = autoreService;
	}

	// Mostra il dettaglio di un autore dato il suo id
	@GetMapping("/autori/{id}")
	public String mostraAutore(@PathVariable Long id, Model model) {
		Autore autore = this.autoreService.findById(id);
		if (autore == null) {
			return "redirect:/autori";  //Se non esiste quell'ID, torna alla lista degli autori
		}
		model.addAttribute("autore", autore);
		return "autori/mostraAutore";
	}

	// Mostra la lista di tutti gli autori presenti nel sistema
	@GetMapping("/autori")
	public String listaAutori(Model model) {
		model.addAttribute("autori", this.autoreService.findAll());
		return "autori/listaAutori";
	}
}