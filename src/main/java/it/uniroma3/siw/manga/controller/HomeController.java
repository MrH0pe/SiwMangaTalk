package it.uniroma3.siw.manga.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Controller per la pagina principale dell'applicazione
@Controller
public class HomeController {

<<<<<<< HEAD
	// Restituisce la home page dell'applicazione
=======

>>>>>>> 5d5dc9cfc21420119f1c688c386c5ddd2463f799
	@GetMapping("/")
	public String getHome() {
		
		return "index";
	}
}
