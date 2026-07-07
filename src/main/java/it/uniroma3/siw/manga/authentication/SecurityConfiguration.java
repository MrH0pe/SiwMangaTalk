package it.uniroma3.siw.manga.authentication;


import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import it.uniroma3.siw.manga.model.Credentials;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final DataSource dataSource;


    public SecurityConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Bean
    public UserDetailsService userDetailsService() {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery(
            "SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
        manager.setAuthoritiesByUsernameQuery(
            "SELECT username, role FROM credentials WHERE username=?");
        return manager;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Definisce la catena di filtri di sicurezza: regole di autorizzazione, login e logout.
     *
     * Regole di accesso:
     * - Pagine pubbliche (tutti, anche anonimi): home, registrazione, lista manga,
     *   dettaglio manga, lista/dettaglio autori, risorse statiche (CSS, immagini, ecc.)
     * - POST /register e /login: pubblici (per permettere la registrazione e il login)
     * - GET /mieiCommenti: solo utenti autenticati
     * - /admin/**: solo utenti con ruolo ADMIN
     * - Qualsiasi altra richiesta: richiede autenticazione
     */
    @Bean
    protected SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception {
    	
        httpSecurity.authorizeHttpRequests(authorize -> {
            // Pannello admin manga: DEVE precedere la regola permAll per /mangas/**
            // altrimenti /mangas/admin/** sarebbe accessibile a tutti.
            authorize.requestMatchers("/mangas/admin/**").hasAnyAuthority(Credentials.ADMIN_ROLE);
            // Pagine e risorse accessibili a tutti (anche non loggati)
            authorize.requestMatchers(HttpMethod.GET, "/", "/index", "/register", "/mangas", "/mangas/**", "/autori", "/autori/**", "/css/**",
            		"/images/**", "/CopertinaManga/**", "/sfondoManga/**", "/favicon.ico", "/error").permitAll();
            // Registrazione e login accessibili a tutti
            authorize.requestMatchers(HttpMethod.POST, "/register", "/login").permitAll();
            // Pagina "I miei commenti": solo utenti loggati
            authorize.requestMatchers(HttpMethod.GET, "/mieiCommenti").authenticated();
            // Area amministrazione: solo ruolo ADMIN
            authorize.requestMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority(Credentials.ADMIN_ROLE);
            authorize.requestMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority(Credentials.ADMIN_ROLE);
            // Tutto il resto (es. post commenti, voti, reazioni): richiede login
            authorize.anyRequest().authenticated();
        });

        // Pagina di login personalizzata; Spring Security gestisce l'autenticazione in automatico.
        // Il successHandler personalizzato reindirizza l'admin a /admin e gli utenti normali a /.
        httpSecurity.formLogin(form -> {
            form.loginPage("/login").permitAll();
            form.successHandler((request, response, authentication) -> {
                boolean isAdmin = false;
                for (GrantedAuthority authority : authentication.getAuthorities()) {
                    if (Credentials.ADMIN_ROLE.equals(authority.getAuthority())) {
                        isAdmin = true;
                        break;
                    }
                }
                response.sendRedirect(isAdmin ? "/admin" : "/");
            });
            form.failureUrl("/login?error=true");    // in caso di credenziali errate
        });

        // Logout: invalida la sessione, cancella il cookie e reindirizza alla home
        httpSecurity.logout(logout -> {
            logout.logoutUrl("/logout");
            logout.logoutSuccessUrl("/");
            logout.invalidateHttpSession(true);
            logout.deleteCookies("JSESSIONID");
            logout.clearAuthentication(true);
            logout.permitAll();
        });

        return httpSecurity.build();
    }
}
