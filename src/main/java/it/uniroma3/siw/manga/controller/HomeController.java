package it.uniroma3.siw.manga.controller;


import it.uniroma3.siw.manga.service.MangaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// Controller per la pagina principale dell'applicazione.
// È il punto di ingresso del sito: chiunque (anche non loggato) può visitare la home.
@Controller
public class HomeController {


	// Risponde a GET "/" e restituisce il template "index.html" nella cartella templates/
	@GetMapping("/")
	public String getHome(Model model) {
		return "index";
	}



}
