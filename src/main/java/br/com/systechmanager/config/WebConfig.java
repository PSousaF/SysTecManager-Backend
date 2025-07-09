package br.com.systechmanager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/imagens/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET")
                .maxAge(3600);

       /* registry.addMapping("/api/**")
			    .allowedOrigins("http://192.168.1.160:4200")
			    .allowedMethods("GET", "POST")
			    .maxAge(3600);*/
	}
}
