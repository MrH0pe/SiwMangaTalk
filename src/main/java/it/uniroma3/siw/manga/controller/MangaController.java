package it.uniroma3.siw.manga.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.manga.model.Commento;
import it.uniroma3.siw.manga.model.Manga;
import it.uniroma3.siw.manga.service.CommentoService;
import it.uniroma3.siw.manga.service.MangaService;

@Controller
public class MangaController {

	private final MangaService mangaService;
	private final CommentoService commentoService;

	public MangaController(MangaService mangaService, CommentoService commentoService) {
		this.mangaService = mangaService;
		this.commentoService = commentoService;
	}

	@GetMapping("/mangas/{id}")
	public String mostraManga(@PathVariable("id") Long id, Model model) {
		Manga manga = this.mangaService.findById(id);
		if (manga == null) {
			return "redirect:/mangas";
		}
		model.addAttribute("manga", manga);
		model.addAttribute("commento", new Commento());
		model.addAttribute("commenti", this.commentoService.findByMangaId(id));
		return "manga/mostraManga";
	}
	
	// TUTTI I MANGA
	@GetMapping("/mangas")
	public String listaManga(Model model) {
		List<Manga> mangaList = this.mangaService.findAll();
		model.addAttribute("mangas", mangaList);
		
		return "manga/listaManga";
	}
}