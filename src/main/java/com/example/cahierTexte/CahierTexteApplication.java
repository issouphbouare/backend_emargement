package com.example.cahierTexte;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

import com.example.cahierTexte.model.Professeur;

@SpringBootApplication
public class CahierTexteApplication implements CommandLineRunner {
	@Autowired
    private RepositoryRestConfiguration restConfiguration;
	public static void main(String[] args) {
		SpringApplication.run(CahierTexteApplication.class, args);
		
		
	}
	@Override
	public void run(String... args) throws Exception {
		restConfiguration.exposeIdsFor(Professeur.class);
		// TODO Auto-generated method stub
		
	}
	

}
