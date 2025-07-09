package br.com.systechmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SysTechManagerApplication {


    @GetMapping("/")
    public String getRun() {
    	return "Server is Running";
    }
    

    @PostMapping("/")
    public String postRun() {
    	return "Server is Running";
    }
	
	public static void main(String[] args) {
		SpringApplication.run(SysTechManagerApplication.class, args);
		System.out.println("SysTechManager Server is Running");
	}

}