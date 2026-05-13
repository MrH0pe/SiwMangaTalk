package it.uniroma3.siw.manga.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.manga.model.Commento;
import it.uniroma3.siw.manga.model.Credentials;
import it.uniroma3.siw.manga.model.Manga;
import it.uniroma3.siw.manga.model.User;
import it.uniroma3.siw.manga.service.CommentoService;
import it.uniroma3.siw.manga.service.CredentialsService;
import it.uniroma3.siw.manga.service.MangaService;
import it.uniroma3.siw.manga.service.ReazioneCommentoService;
import it.uniroma3.siw.manga.service.VotazioneService;

@Controller
public class MangaController {

	private final MangaService mangaService;
	private final CommentoService commentoService;
	private final CredentialsService credentialsService;
	private final VotazioneService votazioneService;
	private final ReazioneCommentoService reazioneService;

	public MangaController(MangaService mangaService, CommentoService commentoService,
			CredentialsService credentialsService, VotazioneService votazioneService,
			ReazioneCommentoService reazioneService) {
		this.mangaService = mangaService;
		this.commentoService = commentoService;
		this.credentialsService = credentialsService;
		this.votazioneService = votazioneService;
		this.reazioneService = reazioneService;
	}


	//Serve per mostrare il singolo manga
	@GetMapping("/mangas/{id}")
	public String mostraManga(@PathVariable("id") Long id, Model model) {
		Manga manga = this.mangaService.findById(id);
		if (manga == null) {
			return "redirect:/mangas";
		}
		model.addAttribute("manga", manga);
		model.addAttribute("commento", new Commento());
		model.addAttribute("commenti", this.commentoService.findTopLevelByMangaId(id));

		// Aggiunge l'id dell'utente corrente per il controllo di proprietà dei commenti,
		// i dati della votazione e le reazioni ai commenti
		Long currentUserId = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
			Credentials creds = credentialsService.getCredentials(auth.getName());
			if (creds != null && creds.getUtente() != null) {
				currentUserId = creds.getUtente().getId();
				model.addAttribute("currentUserId", currentUserId);
				model.addAttribute("votoUtente", votazioneService.getVotoUtente(currentUserId, id));
				model.addAttribute("reazioniUtente", reazioneService.getReazioniUtente(currentUserId, id));
			}
		}
		model.addAttribute("mediaVoti", votazioneService.getMediaVoti(id));
		model.addAttribute("countVoti", votazioneService.countVoti(id));
		model.addAttribute("likeMap", reazioneService.getLikeCountByManga(id));
		model.addAttribute("dislikeMap", reazioneService.getDislikeCountByManga(id));
		return "manga/mostraManga";
	}

	// Salva o aggiorna il voto dell'utente per un manga
	@PostMapping("/mangas/{id}/voto")
	public String votaManga(@PathVariable Long id, @RequestParam double valoreStelline) {
		Manga manga = this.mangaService.findById(id);
		if (manga == null) return "redirect:/mangas";
		if (valoreStelline >= 0.5 && valoreStelline <= 5.0) {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			User utente = credentialsService.getCredentials(username).getUtente();
			votazioneService.vota(manga, utente, valoreStelline);
		}
		return "redirect:/mangas/" + id;
	}

	// TUTTI I MANGA con ordinamento opzionale
	@GetMapping("/mangas")
	public String listaManga(Model model,
			@RequestParam(required = false, defaultValue = "alpha-asc") String sort) {

		List<Manga> mangaList = new ArrayList<>(this.mangaService.findAll());

		// Calcola la media voti per ogni manga
		Map<Long, Double> mediaMap = new HashMap<>();
		for (Manga manga : mangaList) {
			Double media = votazioneService.getMediaVoti(manga.getId());
			mediaMap.put(manga.getId(), media != null ? media : 0.0);
		}

		// Ordina la lista in base al parametro ricevuto
		Comparator<Manga> comparator;
		switch (sort) {
			case "alpha-desc":
				comparator = (a, b) -> b.getNome().compareToIgnoreCase(a.getNome());
				break;
			case "rating-desc":
				comparator = (a, b) -> Double.compare(
						mediaMap.getOrDefault(b.getId(), 0.0),
						mediaMap.getOrDefault(a.getId(), 0.0));
				break;
			case "rating-asc":
				comparator = (a, b) -> Double.compare(
						mediaMap.getOrDefault(a.getId(), 0.0),
						mediaMap.getOrDefault(b.getId(), 0.0));
				break;
			default: // alpha-asc
				comparator = (a, b) -> a.getNome().compareToIgnoreCase(b.getNome());
				break;
		}
		mangaList.sort(comparator);

		model.addAttribute("mangas", mangaList);
		model.addAttribute("mediaMap", mediaMap);
		model.addAttribute("sort", sort);

		return "manga/listaManga";
	}
}