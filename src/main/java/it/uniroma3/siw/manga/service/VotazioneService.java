package it.uniroma3.siw.manga.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.manga.model.Manga;
import it.uniroma3.siw.manga.model.User;
import it.uniroma3.siw.manga.model.Votazione;
import it.uniroma3.siw.manga.repository.VotazioneRepository;

@Service
public class VotazioneService {

	@Autowired
	private VotazioneRepository votazioneRepository;

	/**
	 * Salva o aggiorna il voto dell'utente per un manga (upsert).
	 * Se il valore è fuori dal range 0.5–5.0 il metodo non fa nulla (validazione di business).
	 * Se esiste già un voto per la coppia (utente, manga), viene aggiornato;
	 * altrimenti viene creato un nuovo record Votazione.
	 */
	@Transactional
	public void vota(Manga manga, User utente, double valore) {
		// Validazione di business: solo valori tra 0.5 e 5.0 sono accettati
		if (valore < 0.5 || valore > 5.0) return;

		Votazione votazione = this.votazioneRepository.findByUtenteIdAndMangaId(utente.getId(), manga.getId()).orElse(null);
		if (votazione == null) {
			votazione = new Votazione();
		}
		votazione.setManga(manga);
		votazione.setUtente(utente);
		votazione.setValoreStelline(valore);
		this.votazioneRepository.save(votazione);
	}

	/**
	 * Restituisce il voto espresso dall'utente per un manga,
	 * oppure null se l'utente non ha ancora votato.
	 * Usato nella pagina del manga per pre-selezionare le stelle già cliccate.
	 */
	@Transactional(readOnly = true)
	public Double getVotoUtente(Long utenteId, Long mangaId) {
		Votazione votazione = this.votazioneRepository.findByUtenteIdAndMangaId(utenteId, mangaId).orElse(null);
		if (votazione != null) {
			return votazione.getValoreStelline();
		}
		return null;
	}

	/**
	 * Calcola e restituisce la media aritmetica di tutti i voti di un manga.
	 * Restituisce null se il manga non ha ancora ricevuto voti (AVG su zero righe è null).
	 * Usato nella pagina del manga e nella lista manga per mostrare il rating.
	 *
	 * La media è calcolata dal database con una query aggregata (AVG): prima veniva
	 * calcolata in Java caricando tutte le entità Votazione, che con manga e utente
	 * EAGER generava una query aggiuntiva per ogni votante (problema N+1).
	 */
	@Transactional(readOnly = true)
	public Double getMediaVoti(Long mangaId) {
	    return this.votazioneRepository.mediaByMangaId(mangaId);
	}

	/**
	 * Restituisce il numero totale di voti ricevuti da un manga.
	 * Usato nella pagina del manga per mostrare quante persone hanno votato.
	 * Il conteggio è fatto dal database (COUNT), senza caricare le entità.
	 */
	@Transactional(readOnly = true)
	public long countVoti(Long mangaId) {
		return this.votazioneRepository.countByMangaId(mangaId);
	}

}
