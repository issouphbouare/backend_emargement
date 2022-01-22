package com.example.cahierTexte.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.cahierTexte.model.Professeur;
@RepositoryRestResource
@CrossOrigin("*")
public interface ProfesseurRepository extends JpaRepository<Professeur, Long> {
	Professeur findByTel(int tel);
	

}
