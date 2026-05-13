package it.uniroma3.siw.manga.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.manga.model.Manga;
import it.uniroma3.siw.manga.model.User;
import it.uniroma3.siw.manga.model.Votazione;
import it.uniroma3.siw.manga.repository.VotazioneRepository;

/**
 * Service per la gestione delle votazioni (voto con stelle) ai manga.
 *
 * Il sistema supporta voti da 0.5 a 5.0 a mezzi punti.
 * Se l'utente vota di nuovo lo stesso manga, il record esistente viene aggiornato
 * (logica upsert: findOrNew + save).
 */
@Service
public class VotazioneService {

	private final VotazioneRepository votazioneRepository;

	/** Costruttore con iniezione del repository tramite Spring. */
	public VotazioneService(VotazioneRepository votazioneRepository) {
		this.votazioneRepository = votazioneRepository;
	}

	/**
	 * Salva o aggiorna il voto dell'utente per un manga (upsert).
	 * Se esiste già un voto per la coppia (utente, manga), viene aggiornato;
	 * altrimenti viene creato un nuovo record.
	 */
	@Transactional
	public void vota(Manga manga, User utente, double valore) {
		Votazione votazione = this.votazioneRepository
				.findByUtenteIdAndMangaId(utente.getId(), manga.getId())
				.orElse(new Votazione());
		votazione.setManga(manga);
		votazione.setUtente(utente);
		votazione.setValoreStelline(valore);
		this.votazioneRepository.save(votazione);
	}

	/**
	 * Restituisce il voto espresso dall'utente per un manga,
	 * oppure null se l'utente non ha ancora votato.
	 */
	@Transactional(readOnly = true)
	public Double getVotoUtente(Long utenteId, Long mangaId) {
		return this.votazioneRepository.findByUtenteIdAndMangaId(utenteId, mangaId)
				.map(Votazione::getValoreStelline)
				.orElse(null);
	}

	/**
	 * Calcola e restituisce la media aritmetica di tutti i voti di un manga.
	 * Restituisce null se il manga non ha ancora ricevuto voti.
	 */
	@Transactional(readOnly = true)
	public Double getMediaVoti(Long mangaId) {
		List<Votazione> voti = this.votazioneRepository.findByMangaId(mangaId);
		if (voti.isEmpty()) return null;
		return voti.stream().mapToDouble(Votazione::getValoreStelline).average().orElse(0);
	}

	/** Restituisce il numero totale di voti ricevuti da un manga. */
	@Transactional(readOnly = true)
	public long countVoti(Long mangaId) {
		return this.votazioneRepository.findByMangaId(mangaId).size();
	}

}
