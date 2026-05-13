package it.uniroma3.siw.manga.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Controller per la pagina principale dell'applicazione
@Controller
public class HomeController {

	// Restituisce la home page dell'applicazione
	@GetMapping("/")
	public String getHome() {
		return "index";
	}
}
