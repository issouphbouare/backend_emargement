package com.example.cahierTexte.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cahierTexte.Repo.PayerRepository;
import com.example.cahierTexte.Repo.ProfInt;
import com.example.cahierTexte.Repo.ProfesseurRepository;
import com.example.cahierTexte.Repo.SeanceRepository;
@RestController
@RequestMapping("profthing")
public class ProfesseurRestController{
	@Autowired
	ProfesseurRepository professeurRepository;
	@Autowired
	PayerRepository payerRepository;
	@Autowired
	SeanceRepository seanceRepository;

	@PutMapping
	public void MiseAJourHonoraire() {
		
		
	}

}
