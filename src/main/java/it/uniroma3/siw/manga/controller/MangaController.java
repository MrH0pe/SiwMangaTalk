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

import it.uniroma3.siw.manga.model.Credentials;
import it.uniroma3.siw.manga.model.Manga;
import it.uniroma3.siw.manga.model.User;
import it.uniroma3.siw.manga.service.CommentoService;
import it.uniroma3.siw.manga.service.CredentialsService;
import it.uniroma3.siw.manga.service.MangaService;
import it.uniroma3.siw.manga.service.VotazioneService;

@Controller
public class MangaController {

	private final MangaService mangaService;
	private final CommentoService commentoService;
	private final CredentialsService credentialsService;
	private final VotazioneService votazioneService;

	public MangaController(MangaService mangaService, CommentoService commentoService,
			CredentialsService credentialsService, VotazioneService votazioneService) {
		this.mangaService = mangaService;
		this.commentoService = commentoService;
		this.credentialsService = credentialsService;
		this.votazioneService = votazioneService;
	}

	/**
	 * Mostra la pagina di dettaglio di un manga (GET /mangas/{id}).
	 *
	 * Se l'utente è ADMIN viene subito reindirizzato al pannello admin del manga.
	 *
	 * Carica nel model:
	 * - il manga e i suoi commenti principali (senza risposte)
	 * - la media e il numero totale dei voti
	 * - le mappe like/dislike per ciascun commento
	 * - se l'utente è autenticato: il suo voto attuale e le sue reazioni ai commenti
	 *
	 * Il template usato è: manga/mostraManga.html
	 */
	@GetMapping("/mangas/{id}")
	public String mostraManga(@PathVariable Long id, Model model) {
		Manga manga = this.mangaService.findById(id);
		if (manga == null) {
			return "redirect:/mangas";  //Se non esiste quell'ID, ci riporta alla lista di tutti i manga
		}
		model.addAttribute("manga", manga);
		model.addAttribute("commenti", this.commentoService.findTopLevelByMangaId(id));

		// Se l'utente è autenticato, recupera i suoi dati personali (voto).
		// L'admin viene reindirizzato subito al pannello di amministrazione.
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
			Credentials creds = credentialsService.getCredentials(auth.getName());
			if (creds != null && Credentials.ADMIN_ROLE.equals(creds.getRole())) {
				return "redirect:/mangas/admin/" + id;
			}
			if (creds != null && creds.getUtente() != null) {
				Long currentUserId = creds.getUtente().getId();
				// currentUserId: usato nella view per mostrare il pulsante "elimina" solo al proprietario
				model.addAttribute("currentUserId", currentUserId);
				// votoUtente: il voto (0.5–5.0) già espresso dall'utente, null se non ha votato
				model.addAttribute("votoUtente", votazioneService.getVotoUtente(currentUserId, id));

			}
		}
		// mediaVoti e countVoti: visibili a tutti (anche anonimi)
		model.addAttribute("mediaVoti", votazioneService.getMediaVoti(id));
		model.addAttribute("countVoti", votazioneService.countVoti(id));
		return "manga/mostraManga";
	}

	/**
	 * Registra o aggiorna il voto dell'utente autenticato per un manga (POST /mangas/{id}/voto).
	 * Accetta valori da 0.5 a 5.0 (mezze stelle); valori fuori range vengono ignorati.
	 * Dopo il salvataggio reindirizza alla pagina di dettaglio del manga.
	 *
	 * Delegato a: VotazioneService.vota()
	 */
	@PostMapping("/mangas/{id}/voto")
	public String votaManga(@PathVariable Long id, @RequestParam double valoreStelline) {  
		Manga manga = this.mangaService.findById(id);
		if (manga == null) return "redirect:/mangas";  //Se l'id non esiste ritorna alla lista dei manga
		// La validazione del range (0.5–5.0) è responsabilità del service
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User utente = credentialsService.getCredentials(username).getUtente();
		votazioneService.vota(manga, utente, valoreStelline);
		return "redirect:/mangas/" + id;
	}

	/**
	 * Mostra la lista di tutti i manga con ordinamento opzionale (GET /mangas).
	 *
	 * Il parametro {@code sort} accetta i valori:
	 * - "alpha-asc"   → ordine alfabetico A→Z (default)
	 * - "alpha-desc"  → ordine alfabetico Z→A
	 * - "rating-desc" → media voti dal più alto al più basso
	 * - "rating-asc"  → media voti dal più basso al più alto
	 *
	 * Il template usato è: manga/listaManga.html
	 */
	@GetMapping("/mangas")
	public String listaManga(Model model,
	        @RequestParam(required = false, defaultValue = "alpha-asc") String sort) {

	    List<Manga> mangaList = new ArrayList<>(this.mangaService.findAll());

	    Map<Long, Double> mediaMap = new HashMap<>();
	    for (Manga manga : mangaList) {
	        Double media = votazioneService.getMediaVoti(manga.getId());
	        mediaMap.put(manga.getId(), media != null ? media : 0.0);
	    }

	    Comparator<Manga> comparator;
	    switch (sort) {
	        case "alpha-desc":
	            comparator = new Comparator<Manga>() {
	                @Override
	                public int compare(Manga a, Manga b) {
	                    return b.getNome().compareToIgnoreCase(a.getNome());
	                }
	            };
	            break;
	        case "rating-desc":
	            comparator = new Comparator<Manga>() {
	                @Override
	                public int compare(Manga a, Manga b) {
	                    return Double.compare(
	                            mediaMap.getOrDefault(b.getId(), 0.0),
	                            mediaMap.getOrDefault(a.getId(), 0.0));
	                }
	            };
	            break;
	        case "rating-asc":
	            comparator = new Comparator<Manga>() {
	                @Override
	                public int compare(Manga a, Manga b) {
	                    return Double.compare(
	                            mediaMap.getOrDefault(a.getId(), 0.0),
	                            mediaMap.getOrDefault(b.getId(), 0.0));
	                }
	            };
	            break;
	        default: // alpha-asc
	            comparator = new Comparator<Manga>() {
	                @Override
	                public int compare(Manga a, Manga b) {
	                    return a.getNome().compareToIgnoreCase(b.getNome());
	                }
	            };
	            break;
	    }

	    mangaList.sort(comparator);

	    model.addAttribute("mangas", mangaList);
	    model.addAttribute("mediaMap", mediaMap);
	    model.addAttribute("sort", sort);

	    return "manga/listaManga";
	}
}
