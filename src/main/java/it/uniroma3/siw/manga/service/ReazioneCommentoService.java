package it.uniroma3.siw.manga.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.manga.model.Commento;
import it.uniroma3.siw.manga.model.ReazioneCommento;
import it.uniroma3.siw.manga.model.User;
import it.uniroma3.siw.manga.repository.CommentoRepository;
import it.uniroma3.siw.manga.repository.ReazioneCommentoRepository;

@Service
public class ReazioneCommentoService {

    private final ReazioneCommentoRepository reazioneRepository;
    private final CommentoRepository commentoRepository;

    public ReazioneCommentoService(ReazioneCommentoRepository reazioneRepository,
                                   CommentoRepository commentoRepository) {
        this.reazioneRepository = reazioneRepository;
        this.commentoRepository = commentoRepository;
    }

    // Aggiunge, cambia o rimuove (toggle) la reazione dell'utente a un commento
    @Transactional
    public void reagisci(Long commentoId, User utente, String tipo) {
        Commento commento = this.commentoRepository.findById(commentoId).orElse(null);
        if (commento == null) return;

        Optional<ReazioneCommento> existing =
                this.reazioneRepository.findByUtenteIdAndCommentoId(utente.getId(), commentoId);

        if (existing.isPresent()) {
            ReazioneCommento reazione = existing.get();
            if (reazione.getTipo().equals(tipo)) {
                // Stessa reazione → toggle off (rimuovi)
                this.reazioneRepository.delete(reazione);
            } else {
                // Reazione diversa → cambia tipo
                reazione.setTipo(tipo);
                this.reazioneRepository.save(reazione);
            }
        } else {
            // Nessuna reazione precedente → aggiungi
            ReazioneCommento nuova = new ReazioneCommento();
            nuova.setUtente(utente);
            nuova.setCommento(commento);
            nuova.setTipo(tipo);
            this.reazioneRepository.save(nuova);
        }
    }

    // Mappa commentoId → numero di like, per tutti i commenti del manga
    @Transactional(readOnly = true)
    public Map<Long, Long> getLikeCountByManga(Long mangaId) {
        List<ReazioneCommento> all = this.reazioneRepository.findByCommentoMangaId(mangaId);
        return all.stream()
                .filter(r -> "LIKE".equals(r.getTipo()))
                .collect(Collectors.groupingBy(r -> r.getCommento().getId(), Collectors.counting()));
    }

    // Mappa commentoId → numero di dislike, per tutti i commenti del manga
    @Transactional(readOnly = true)
    public Map<Long, Long> getDislikeCountByManga(Long mangaId) {
        List<ReazioneCommento> all = this.reazioneRepository.findByCommentoMangaId(mangaId);
        return all.stream()
                .filter(r -> "DISLIKE".equals(r.getTipo()))
                .collect(Collectors.groupingBy(r -> r.getCommento().getId(), Collectors.counting()));
    }

    // Mappa commentoId → tipo reazione ("LIKE" / "DISLIKE"), solo per l'utente corrente
    @Transactional(readOnly = true)
    public Map<Long, String> getReazioniUtente(Long utenteId, Long mangaId) {
        return this.reazioneRepository.findByUtenteIdAndCommentoMangaId(utenteId, mangaId)
                .stream()
                .collect(Collectors.toMap(r -> r.getCommento().getId(), ReazioneCommento::getTipo));
    }

}
