package it.uniroma3.siw.manga.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.manga.model.Commento;
import it.uniroma3.siw.manga.service.CommentoService;

@Controller
public class CommentoController {
	private CommentoService commentoService;
	
	//Costruttore
	public CommentoController(CommentoService commentoService) {
		this.commentoService = commentoService;
	}
	
	//
	 @GetMapping("/commenti")
	 public String mostra(@PathVariable("id") Long id, Model model) {

			Commento commento = this.commentoService.findById(id);
			model.addAttribute("commento", commento);

			return "commenti/mostra";
		}
}
