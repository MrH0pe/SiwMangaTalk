package it.uniroma3.siw.manga.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.manga.service.AutoreService;

@Controller
public class AutoreController {
	
	private final AutoreService autoreService;

	public AutoreController(AutoreService autoreService) {
		this.autoreService = autoreService;
	}
	
	@GetMapping("/autori/{id}")
	public String mostraAutore(@PathVariable Long id, Model model) {
		model.addAttribute("autore", this.autoreService.findById(id));
		return "autori/mostraAutore";
	}

	@GetMapping("/autori")
	public String listaAutori(Model model) {
		model.addAttribute("autori", this.autoreService.findAll());
		return "autori/listaAutori";
	}
}