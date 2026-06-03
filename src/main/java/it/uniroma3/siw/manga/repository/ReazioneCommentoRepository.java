package it.uniroma3.siw.manga.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.manga.model.ReazioneCommento;

public interface ReazioneCommentoRepository extends CrudRepository<ReazioneCommento, Long> {

    // Reazione specifica di un utente su un commento
    Optional<ReazioneCommento> findByUtenteIdAndCommentoId(Long utenteId, Long commentoId);

    // Tutte le reazioni sui commenti di un manga (usato per calcolare like/dislike counts)
    List<ReazioneCommento> findByCommentoMangaId(Long mangaId);

    // Reazioni di un utente su tutti i commenti di un manga (per sapere cosa ha già votato)
    List<ReazioneCommento> findByUtenteIdAndCommentoMangaId(Long utenteId, Long mangaId);

    // Conta le reazioni di un tipo specifico su un singolo commento (usato dall'endpoint REST)
    long countByCommentoIdAndTipo(Long commentoId, String tipo);

}
