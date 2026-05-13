package it.uniroma3.siw.manga.authentication;


import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import it.uniroma3.siw.manga.model.Credentials;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    //Questo attributo e' un componente di Spring che modella un punto di accesso al DB
    private final DataSource dataSource;

    public SecurityConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    //CONFIGURAZIONE DELLA GESTIONE DEGLI UTENTI
    @Bean
    public UserDetailsService userDetailsService() {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery(
            "SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
        manager.setAuthoritiesByUsernameQuery(
            "SELECT username, role FROM credentials WHERE username=?");
        return manager;
    }

    //Codice alfanumerico per Criptare la password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //CHI PUO' ACCEDERE A COSA
    @Bean
    protected SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(authorize -> {
            authorize.requestMatchers(HttpMethod.GET, "/", "/index", "/register", "/mangas", "/mangas/**", "/autori", "/autori/**", "/css/**", "/images/**", "/CopertinaManga/**", "/sfondoManga/**", "/favicon.ico").permitAll();
            authorize.requestMatchers(HttpMethod.POST, "/register", "/login").permitAll();
            authorize.requestMatchers(HttpMethod.GET, "/mieiCommenti").authenticated();
            authorize.requestMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority(Credentials.ADMIN_ROLE);
            authorize.requestMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority(Credentials.ADMIN_ROLE);
            authorize.anyRequest().authenticated();
        });

        //CONFIGURAZIONE LOGIN
        httpSecurity.formLogin(form -> {
            form.loginPage("/login").permitAll();
            form.defaultSuccessUrl("/", true);
            form.failureUrl("/login?error=true");
        });

        //CONFIGURAZIONE LOGOUT
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
