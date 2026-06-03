package it.uniroma3.siw.manga.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.manga.model.Commento;
import it.uniroma3.siw.manga.repository.CommentoRepository;

/**
 * Service per la gestione dei commenti.
 *
 * Si occupa di recuperare, salvare ed eliminare i commenti.
 * La logica di eliminazione (deleteIfOwner) gestisce sia commenti principali
 * sia risposte, garantendo che solo il proprietario possa eliminare i propri contenuti.
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
     */
    @Transactional(readOnly = true)
    public Commento findById(Long id) {
        return this.commentoRepository.findById(id).orElse(null);
    }

    /**
     * Restituisce tutti i commenti scritti da un utente, identificato dal suo id.
     * Usato nella pagina "I miei commenti".
     */
    @Transactional(readOnly = true)
    public List<Commento> findByUtenteId(Long id) {
        return this.commentoRepository.findByUtenteId(id);
    }

    /**
     * Restituisce solo i commenti principali (senza padre) di un manga,
     * cioè esclude le risposte. Usato per popolare la sezione commenti
     * nella pagina di dettaglio del manga.
     */
    @Transactional(readOnly = true)
    public List<Commento> findTopLevelByMangaId(Long mangaId) {
        return this.commentoRepository.findByMangaIdAndCommentoPadreIsNull(mangaId);
    }

<<<<<<< HEAD
    /**
     * Salva un commento (nuovo o aggiornato) nel database.
     */
=======

    //Restituisce solo i commenti principali (senza padre) di un manga
    @Transactional(readOnly = true)
    public List<Commento> findTopLevelByMangaId(Long mangaId) {
        return this.commentoRepository.findByMangaIdAndCommentoPadreIsNull(mangaId);
    }


    //salva il commento
>>>>>>> 5d5dc9cfc21420119f1c688c386c5ddd2463f799
    @Transactional
    public void save(Commento commento) {
        this.commentoRepository.save(commento);
    }

<<<<<<< HEAD
    /**
     * Elimina un commento senza verificare la proprietà (riservato all'admin).
     * Usa la stessa logica di orphanRemoval di deleteIfOwner per garantire
     * la corretta pulizia di risposte e reazioni in cascata.
     */
    @Transactional
    public void deleteAsAdmin(Long commentoId) {
        Commento commento = this.commentoRepository.findById(commentoId).orElse(null);
        if (commento == null) return;
        Commento padre = commento.getCommentoPadre();
        if (padre != null) {
            padre.getRisposte().removeIf(r -> r.getId().equals(commentoId));
            this.commentoRepository.save(padre);
        } else {
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
=======
    // Elimina un commento solo se l'utente corrente ne è il proprietario
>>>>>>> 5d5dc9cfc21420119f1c688c386c5ddd2463f799
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
<<<<<<< HEAD
            // È un commento principale: delete diretto.
            // CascadeType.ALL propaga la cancellazione alle risposte e alle reazioni.
            this.commentoRepository.delete(commento);
        }
    }

    /**
     * Aggiorna il testo di un commento solo se l'utente corrente ne è il proprietario.
     * Restituisce false se: il commento non esiste, l'utente non è il proprietario,
     * o il nuovo testo è vuoto/blank (almeno un carattere non-spazio richiesto).
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
=======
            // È un commento primario: delete diretto.
            // CascadeType.ALL propaga la cancellazione alle sue risposte.
            this.commentoRepository.delete(commento);
        }
    }
>>>>>>> 5d5dc9cfc21420119f1c688c386c5ddd2463f799
}