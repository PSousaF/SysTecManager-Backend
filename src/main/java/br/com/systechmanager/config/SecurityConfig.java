package br.com.systechmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.systechmanager.security.JwtAuthFilter;
import br.com.systechmanager.security.JwtService;
import br.com.systechmanager.security.UserAuth;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private JwtService jwtService;
    
    @Autowired
    @Lazy
    private UserAuth usuarioService;
    
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OncePerRequestFilter jwtFilter(){
        return new JwtAuthFilter(jwtService, usuarioService);
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
        .userDetailsService(usuarioService)
        .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure( HttpSecurity http ) throws Exception {
        http.cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))
        .csrf().disable()
        .authorizeRequests()
	            .antMatchers(HttpMethod.GET, "/")
	            .permitAll()
	            .antMatchers(HttpMethod.POST, "/")
	            .permitAll()
                .antMatchers("/api/user/new")
	            .permitAll()
                .antMatchers(HttpMethod.GET,"/api/user/**")
	            .permitAll()
                .antMatchers(HttpMethod.PUT,"/api/user/**")
	            .permitAll()
                .antMatchers("/api/client/**")
	            .permitAll()
                .antMatchers("/api/dash/**")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/app/**")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/api/user/auth")
                .permitAll()
                .anyRequest().authenticated()
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .addFilterBefore( jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        
        //http.addFilter(corsFilter());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }


    private CorsFilter corsFilter() {
        CorsConfigurationSource source = request -> {
            CorsConfiguration config = new CorsConfiguration();
            //config.setAllowedOrigins(List.of("http://localhost:4200")); // Defina o domínio permitido aqui
            //config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE")); // Métodos permitidos
            //config.setAllowedHeaders(List.of("Content-Type", "Authorization")); // Cabeçalhos permitidos
            //config.setAllowCredentials(true); // Permitir credenciais (por exemplo, cookies)
            return config;
        };

        UrlBasedCorsConfigurationSource sourceHandler = new UrlBasedCorsConfigurationSource();
        sourceHandler.registerCorsConfiguration("/**", (CorsConfiguration) source);

        return new CorsFilter(sourceHandler);
    }
}

