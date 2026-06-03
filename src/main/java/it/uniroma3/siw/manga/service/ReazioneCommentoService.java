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

<<<<<<< HEAD
/**
 * Service per la gestione delle reazioni (like/dislike) ai commenti.
 *
 * Ogni utente può esprimere al massimo una reazione per commento.
 * La logica di toggle permette di:
 * - aggiungere una reazione se non ne esiste una
 * - cambiare tipo (da LIKE a DISLIKE o viceversa) se esiste già
 * - rimuovere la reazione se si clicca di nuovo la stessa (toggle off)
 */
=======
>>>>>>> 5d5dc9cfc21420119f1c688c386c5ddd2463f799
@Service
public class ReazioneCommentoService {

    private final ReazioneCommentoRepository reazioneRepository;
    private final CommentoRepository commentoRepository;

<<<<<<< HEAD
    /** Costruttore con iniezione dei repository tramite Spring. */
=======
>>>>>>> 5d5dc9cfc21420119f1c688c386c5ddd2463f799
    public ReazioneCommentoService(ReazioneCommentoRepository reazioneRepository,
                                   CommentoRepository commentoRepository) {
        this.reazioneRepository = reazioneRepository;
        this.commentoRepository = commentoRepository;
    }

<<<<<<< HEAD
    /**
     * Gestisce la reazione (like/dislike) di un utente a un commento con logica di toggle:
     * - Se non esiste reazione → la crea
     * - Se esiste con lo stesso tipo → la rimuove (toggle off)
     * - Se esiste con tipo diverso → la aggiorna al nuovo tipo
     */
=======
    // Aggiunge, cambia o rimuove (toggle) la reazione dell'utente a un commento
>>>>>>> 5d5dc9cfc21420119f1c688c386c5ddd2463f799
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

<<<<<<< HEAD
    /**
     * Restituisce una mappa {commentoId → numero di like} per tutti i commenti
     * del manga specificato. Usata per mostrare i contatori di like nella view.
     */
=======
    // Mappa commentoId → numero di like, per tutti i commenti del manga
>>>>>>> 5d5dc9cfc21420119f1c688c386c5ddd2463f799
    @Transactional(readOnly = true)
    public Map<Long, Long> getLikeCountByManga(Long mangaId) {
        List<ReazioneCommento> all = this.reazioneRepository.findByCommentoMangaId(mangaId);
        return all.stream()
                .filter(r -> "LIKE".equals(r.getTipo()))
                .collect(Collectors.groupingBy(r -> r.getCommento().getId(), Collectors.counting()));
    }

<<<<<<< HEAD
    /**
     * Restituisce una mappa {commentoId → numero di dislike} per tutti i commenti
     * del manga specificato. Usata per mostrare i contatori di dislike nella view.
     */
=======
    // Mappa commentoId → numero di dislike, per tutti i commenti del manga
>>>>>>> 5d5dc9cfc21420119f1c688c386c5ddd2463f799
    @Transactional(readOnly = true)
    public Map<Long, Long> getDislikeCountByManga(Long mangaId) {
        List<ReazioneCommento> all = this.reazioneRepository.findByCommentoMangaId(mangaId);
        return all.stream()
                .filter(r -> "DISLIKE".equals(r.getTipo()))
                .collect(Collectors.groupingBy(r -> r.getCommento().getId(), Collectors.counting()));
    }

<<<<<<< HEAD
    /**
     * Restituisce una mappa {commentoId → "LIKE"/"DISLIKE"} contenente solo
     * le reazioni espresse dall'utente corrente sui commenti del manga.
     * Usata nella view per evidenziare il pulsante già cliccato dall'utente.
     */
=======
    // Mappa commentoId → tipo reazione ("LIKE" / "DISLIKE"), solo per l'utente corrente
>>>>>>> 5d5dc9cfc21420119f1c688c386c5ddd2463f799
    @Transactional(readOnly = true)
    public Map<Long, String> getReazioniUtente(Long utenteId, Long mangaId) {
        return this.reazioneRepository.findByUtenteIdAndCommentoMangaId(utenteId, mangaId)
                .stream()
                .collect(Collectors.toMap(r -> r.getCommento().getId(), ReazioneCommento::getTipo));
    }

<<<<<<< HEAD
    /**
     * Restituisce il numero di like sul commento specificato.
     * Usato dall'endpoint REST dopo un'operazione di reazione per restituire
     * il contatore aggiornato senza ricaricare la pagina.
     */
    @Transactional(readOnly = true)
    public long countLikes(Long commentoId) {
        return this.reazioneRepository.countByCommentoIdAndTipo(commentoId, "LIKE");
    }

    /**
     * Restituisce il numero di dislike sul commento specificato.
     * Usato dall'endpoint REST dopo un'operazione di reazione per restituire
     * il contatore aggiornato senza ricaricare la pagina.
     */
    @Transactional(readOnly = true)
    public long countDislikes(Long commentoId) {
        return this.reazioneRepository.countByCommentoIdAndTipo(commentoId, "DISLIKE");
    }

    /**
     * Restituisce il tipo di reazione ("LIKE" o "DISLIKE") dell'utente sul commento,
     * oppure un Optional vuoto se l'utente non ha ancora reagito (o ha rimosso la reazione).
     * Usato dall'endpoint REST per comunicare al client lo stato aggiornato.
     */
    @Transactional(readOnly = true)
    public Optional<String> getTipoReazioneUtente(Long utenteId, Long commentoId) {
        return this.reazioneRepository.findByUtenteIdAndCommentoId(utenteId, commentoId)
                .map(ReazioneCommento::getTipo);
    }

=======
>>>>>>> 5d5dc9cfc21420119f1c688c386c5ddd2463f799
}
