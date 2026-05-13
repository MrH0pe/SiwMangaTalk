package it.uniroma3.siw.manga.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

// Classe che modella la reazione (like/dislike) di un utente a un commento
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"utente_id", "commento_id"})
})
public class ReazioneCommento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // "LIKE" oppure "DISLIKE"
    private String tipo;

    @ManyToOne(fetch = FetchType.EAGER)
    private User utente;

    @ManyToOne(fetch = FetchType.EAGER)
    private Commento commento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public User getUtente() {
        return utente;
    }

    public void setUtente(User utente) {
        this.utente = utente;
    }

    public Commento getCommento() {
        return commento;
    }

    public void setCommento(Commento commento) {
        this.commento = commento;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tipo, utente, commento);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ReazioneCommento other = (ReazioneCommento) obj;
        return Objects.equals(id, other.id) && Objects.equals(tipo, other.tipo)
                && Objects.equals(utente, other.utente) && Objects.equals(commento, other.commento);
    }
}
