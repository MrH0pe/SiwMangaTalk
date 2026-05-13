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

/**
 * Controller per la gestione delle pagine relative ai manga.
 *
 * Gestisce:
 * - la pagina di dettaglio di un singolo manga (con commenti, votazione e reazioni)
 * - la pagina con la lista di tutti i manga (con ordinamento opzionale)
 * - la sottomissione del voto dell'utente per un manga
 */
@Controller
public class MangaController {

	private final MangaService mangaService;
	private final CommentoService commentoService;
	private final CredentialsService credentialsService;
	private final VotazioneService votazioneService;
	private final ReazioneCommentoService reazioneService;

	/** Costruttore con iniezione di tutti i service tramite Spring. */
	public MangaController(MangaService mangaService, CommentoService commentoService,
			CredentialsService credentialsService, VotazioneService votazioneService,
			ReazioneCommentoService reazioneService) {
		this.mangaService = mangaService;
		this.commentoService = commentoService;
		this.credentialsService = credentialsService;
		this.votazioneService = votazioneService;
		this.reazioneService = reazioneService;
	}

	/**
	 * Mostra la pagina di dettaglio di un manga.
	 *
	 * Carica nel model:
	 * - il manga e i suoi commenti principali
	 * - un oggetto Commento vuoto per il form di inserimento
	 * - la media e il numero totale dei voti
	 * - le mappe like/dislike per ciascun commento
	 * - se l'utente è autenticato: il suo voto attuale e le sue reazioni ai commenti
	 */
	@GetMapping("/mangas/{id}")
	public String mostraManga(@PathVariable("id") Long id, Model model) {
		Manga manga = this.mangaService.findById(id);
		if (manga == null) {
			return "redirect:/mangas";
		}
		model.addAttribute("manga", manga);
		model.addAttribute("commento", new Commento());
		model.addAttribute("commenti", this.commentoService.findTopLevelByMangaId(id));

		// Se l'utente è autenticato, recupera i suoi dati personali (voto e reazioni)
		Long currentUserId = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
			Credentials creds = credentialsService.getCredentials(auth.getName());
			if (creds != null && creds.getUtente() != null) {
				currentUserId = creds.getUtente().getId();
				// currentUserId: usato nella view per mostrare il pulsante "elimina" solo al proprietario
				model.addAttribute("currentUserId", currentUserId);
				// votoUtente: il voto (0.5–5.0) già espresso dall'utente, null se non ha votato
				model.addAttribute("votoUtente", votazioneService.getVotoUtente(currentUserId, id));
				// reazioniUtente: mappa {commentoId → "LIKE"/"DISLIKE"} per i commenti già votati dall'utente
				model.addAttribute("reazioniUtente", reazioneService.getReazioniUtente(currentUserId, id));
			}
		}
		// mediaVoti e countVoti: visibili a tutti (anche anonimi)
		model.addAttribute("mediaVoti", votazioneService.getMediaVoti(id));
		model.addAttribute("countVoti", votazioneService.countVoti(id));
		// likeMap / dislikeMap: mappe {commentoId → conteggio} per tutti i commenti del manga
		model.addAttribute("likeMap", reazioneService.getLikeCountByManga(id));
		model.addAttribute("dislikeMap", reazioneService.getDislikeCountByManga(id));
		return "manga/mostraManga";
	}

	/**
	 * Registra o aggiorna il voto dell'utente autenticato per un manga.
	 * Accetta valori da 0.5 a 5.0 (mezze stelle); valori fuori range vengono ignorati.
	 * Dopo il salvataggio reindirizza alla pagina di dettaglio del manga.
	 */
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

	/**
	 * Mostra la lista di tutti i manga con ordinamento opzionale.
	 *
	 * Il parametro {@code sort} accetta i valori:
	 * - "alpha-asc"   → ordine alfabetico A→Z (default)
	 * - "alpha-desc"  → ordine alfabetico Z→A
	 * - "rating-desc" → media voti dal più alto al più basso
	 * - "rating-asc"  → media voti dal più basso al più alto
	 */
	@GetMapping("/mangas")
	public String listaManga(Model model,
			@RequestParam(required = false, defaultValue = "alpha-asc") String sort) {

		List<Manga> mangaList = new ArrayList<>(this.mangaService.findAll());

		// Precalcola la media voti per ogni manga per usarla nel comparatore e nella view
		Map<Long, Double> mediaMap = new HashMap<>();
		for (Manga manga : mangaList) {
			Double media = votazioneService.getMediaVoti(manga.getId());
			mediaMap.put(manga.getId(), media != null ? media : 0.0);
		}

		// Seleziona il comparatore in base al parametro di ordinamento
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