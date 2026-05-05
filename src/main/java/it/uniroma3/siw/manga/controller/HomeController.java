package it.uniroma3.siw.manga.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {

	@GetMapping("/")
	public String getHome(Model model) {
		return "index";
	}

}
