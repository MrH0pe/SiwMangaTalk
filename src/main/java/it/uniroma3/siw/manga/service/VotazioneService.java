package it.uniroma3.siw.manga.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.manga.model.Manga;
import it.uniroma3.siw.manga.model.User;
import it.uniroma3.siw.manga.model.Votazione;
import it.uniroma3.siw.manga.repository.VotazioneRepository;

@Service
public class VotazioneService {

	private final VotazioneRepository votazioneRepository;

	public VotazioneService(VotazioneRepository votazioneRepository) {
		this.votazioneRepository = votazioneRepository;
	}

	// Salva o aggiorna il voto dell'utente per un manga
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

	// Restituisce il voto dell'utente per un manga, null se non ha ancora votato
	@Transactional(readOnly = true)
	public Double getVotoUtente(Long utenteId, Long mangaId) {
		return this.votazioneRepository.findByUtenteIdAndMangaId(utenteId, mangaId)
				.map(Votazione::getValoreStelline)
				.orElse(null);
	}

	// Restituisce la media dei voti di un manga, null se non ci sono voti
	@Transactional(readOnly = true)
	public Double getMediaVoti(Long mangaId) {
		List<Votazione> voti = this.votazioneRepository.findByMangaId(mangaId);
		if (voti.isEmpty()) return null;
		return voti.stream().mapToDouble(Votazione::getValoreStelline).average().orElse(0);
	}

	// Restituisce il numero totale di voti per un manga
	@Transactional(readOnly = true)
	public long countVoti(Long mangaId) {
		return this.votazioneRepository.findByMangaId(mangaId).size();
	}

}
