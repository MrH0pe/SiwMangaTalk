package it.uniroma3.siw.manga.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.manga.model.Autore;
import it.uniroma3.siw.manga.service.AutoreService;

@Controller
public class AutoreController {
	
	private AutoreService autoreService;

	public AutoreController(AutoreService autoreService) {
		this.autoreService = autoreService;
	}
	
	// AUTORE SINGOLO
	@GetMapping("/autori/{id}")
	public String mostraAutore(@PathVariable("id") Long id, Model model) {
		Autore autore = this.autoreService.findById(id);
		
		// CORREZIONE: Attributo al singolare per matchare ${autore.nome} nell'HTML
		model.addAttribute("autore", autore);
		
		return "autori/mostraAutore";
	}
	
	// TUTTI GLI AUTORI
	@GetMapping("/autori")
	public String listaAutori(Model model) {
		List<Autore> autoreList = this.autoreService.findAll();
		model.addAttribute("autori", autoreList);
		
		return "autori/listaAutori";
	}
}