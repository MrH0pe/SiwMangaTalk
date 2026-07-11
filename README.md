Progetto Sistemi Informativi su Web, Luglio 2026 Panzeri-Lai.
Sito su gestione elenco Manga e relativi voti.
Tecnologie utilizzate : 
• Spring Boot (backend)
• JPA / Hibernate (persistenza)
• PostgreSQL (o altro RDBMS)
• Thymeleaf (frontend)
• React (almeno per una parte del frontend)

Casi d'uso implementati nel progetto (almeno 6, come richiesto):
	 
	  1) (DONE) INSERIMENTO: L'utente registrato può scrivere zero o molti commenti su un manga.
	 
	  2) (DONE) AGGIORNAMENTO: L'utente può modificare i propri commenti già pubblicati.
	 
	  3) (DONE) CANCELLAZIONE:  L'admin può cancellare qualsiasi commento di qualsiasi utente.
	          
	  4) (DONE) LETTURA SINGOLA ENTITÀ: Una pagina web che mostra tutti i commenti
	          scritti da un singolo utente su vari manga (/mieiCommenti).
	 
	  5) (DONE) LETTURA CON ORDINAMENTO: Nella pagina iniziale (/mangas) l'utente può
	           visualizzare i manga ordinati per voto e ordine alfabetico (ascendente o discendente).
	 
	  6) (DONE) LETTURA ADMIN: Solo l'admin può vedere , quali utenti
	           hanno commentato, con username e numero di commenti effettuati e quali manga. (/admin)
	  			e cancellarli.
