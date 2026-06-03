package it.uniroma3.siw.manga.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.manga.model.Commento;
import it.uniroma3.siw.manga.repository.CommentoRepository;

/**
 * Service per la gestione dei commenti.
 *
 * Si occupa di recuperare, salvare, modificare ed eliminare i commenti.
 *
 * La logica di eliminazione distingue due casi:
 * - deleteIfOwner: solo il proprietario può eliminare il proprio commento (utente normale)
 * - deleteAsAdmin: l'admin può eliminare qualsiasi commento senza verificare la proprietà
 *
 * È collegato a: CommentoRepository, Commento (model)
 */
@Service
public class CommentoService {

    private final CommentoRepository commentoRepository;

    /** Costruttore con iniezione del repository tramite Spring. */
    public CommentoService(CommentoRepository commentoRepository) {
        this.commentoRepository = commentoRepository;
    }

    /**
     * Restituisce un commento dato il suo id, oppure null se non esiste.
     * Usato nei controller per recuperare il commento prima di operazioni su di esso.
     */
    @Transactional(readOnly = true)
    public Commento findById(Long id) {
        return this.commentoRepository.findById(id).orElse(null);
    }

    /**
     * Restituisce tutti i commenti scritti da un utente, identificato dal suo id.
     * Usato nella pagina "I miei commenti" (/mieiCommenti).
     */
    @Transactional(readOnly = true)
    public List<Commento> findByUtenteId(Long id) {
        return this.commentoRepository.findByUtenteId(id);
    }

    /**
     * Restituisce solo i commenti principali (senza padre) di un manga,
     * cioè esclude le risposte. Usato per popolare la sezione commenti
     * nella pagina di dettaglio del manga (mostraManga.html).
     * Le risposte sono già incluse in cascata tramite il campo 'risposte' di Commento.
     */
    @Transactional(readOnly = true)
    public List<Commento> findTopLevelByMangaId(Long mangaId) {
        return this.commentoRepository.findByMangaIdAndCommentoPadreIsNull(mangaId);
    }

    /**
     * Salva un commento (nuovo o aggiornato) nel database.
     * Usato sia per nuovi commenti che per modifiche al testo.
     */
    @Transactional
    public void save(Commento commento) {
        this.commentoRepository.save(commento);
    }

    /**
     * Elimina un commento senza verificare la proprietà (riservato all'admin).
     *
     * - Se è una risposta: viene rimossa dalla collection del padre;
     *   Hibernate la cancella automaticamente grazie a orphanRemoval=true.
     * - Se è un commento principale: viene cancellato direttamente;
     *   CascadeType.ALL propaga la cancellazione alle risposte e alle reazioni.
     */
    @Transactional
    public void deleteAsAdmin(Long commentoId) {
        Commento commento = this.commentoRepository.findById(commentoId).orElse(null);
        if (commento == null) return;
        Commento padre = commento.getCommentoPadre();
        if (padre != null) {
            // È una risposta: rimuoviamo dalla collection del padre.
            // orphanRemoval=true fa sì che Hibernate cancelli il record al flush.
            padre.getRisposte().removeIf(r -> r.getId().equals(commentoId));
            this.commentoRepository.save(padre);
        } else {
            // È un commento principale: delete diretto.
            // CascadeType.ALL propaga la cancellazione alle risposte e alle reazioni.
            this.commentoRepository.delete(commento);
        }
    }

    /**
     * Elimina un commento solo se l'utente corrente ne è il proprietario.
     * Se il commento non esiste o appartiene a un altro utente, non viene fatto nulla.
     *
     * - Se è una risposta: viene rimossa dalla collection del padre; Hibernate
     *   la cancella automaticamente grazie a orphanRemoval=true.
     * - Se è un commento principale: viene cancellato direttamente; le risposte
     *   e le reazioni vengono eliminate per effetto di CascadeType.ALL.
     */
    @Transactional
    public void deleteIfOwner(Long commentoId, Long currentUserId) {
        Commento commento = this.commentoRepository.findById(commentoId).orElse(null);
        if (commento == null || commento.getUtente() == null
                || !commento.getUtente().getId().equals(currentUserId)) {
            return;
        }

        Commento padre = commento.getCommentoPadre();
        if (padre != null) {
            // È una risposta: la rimuoviamo dalla collection del padre.
            // Con orphanRemoval=true, Hibernate cancella automaticamente
            // l'entità rimossa dalla collection al flush della transazione.
            padre.getRisposte().removeIf(r -> r.getId().equals(commentoId));
            this.commentoRepository.save(padre);
        } else {
            // È un commento principale: delete diretto.
            // CascadeType.ALL propaga la cancellazione alle risposte e alle reazioni.
            this.commentoRepository.delete(commento);
        }
    }

    /**
     * Aggiorna il testo di un commento solo se l'utente corrente ne è il proprietario.
     * Restituisce false se: il commento non esiste, l'utente non è il proprietario,
     * o il nuovo testo è vuoto/blank (almeno un carattere non-spazio richiesto).
     * Usato dall'endpoint REST PATCH /api/commenti/{id}/modifica.
     */
    @Transactional
    public boolean updateIfOwner(Long commentoId, Long utenteId, String nuovoTesto) {
        if (nuovoTesto == null || nuovoTesto.isBlank()) return false;
        Commento commento = this.commentoRepository.findById(commentoId).orElse(null);
        if (commento == null || commento.getUtente() == null
                || !commento.getUtente().getId().equals(utenteId)) {
            return false;
        }
        commento.setTesto(nuovoTesto.trim());
        this.commentoRepository.save(commento);
        return true;
    }
}
