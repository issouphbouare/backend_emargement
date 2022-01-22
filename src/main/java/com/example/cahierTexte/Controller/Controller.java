package com.example.cahierTexte.Controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.Spring;
import javax.websocket.server.PathParam;

import org.apache.catalina.filters.AddDefaultCharsetFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.cahierTexte.Repo.MatiereRepository;
import com.example.cahierTexte.Repo.PayerRepository;
import com.example.cahierTexte.Repo.ProfesseurRepository;
import com.example.cahierTexte.Repo.SeanceRepository;
import com.example.cahierTexte.model.Classe;
import com.example.cahierTexte.model.Matiere;
import com.example.cahierTexte.model.Payer;
import com.example.cahierTexte.model.Professeur;
import com.example.cahierTexte.model.Seance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RestController
@CrossOrigin("*")
public class Controller {
	
	@Autowired
	SeanceRepository seanceRepository;
	@Autowired
	PayerRepository payerRepository;
	@Autowired
	ProfesseurRepository professeurRepository;
	
	@RequestMapping(value ="/login/{tel}",method = RequestMethod.GET)
	public Professeur getProfesseurByTelPassword(@PathVariable("tel") int
	tel) {
	return professeurRepository.findByTel(tel);
	}
	
	@GetMapping("/honorairePr")
	public List<HonoraireForm> HonorairePr(){
		List<Professeur> profs=professeurRepository.findAll();
		List<Seance> seances =seanceRepository.findAll();
		List<Payer> payers=payerRepository.findAll();
		List<HonoraireForm> listes=new ArrayList<>();
		
		for(Professeur prof : profs ) {
			HonoraireForm honoraireForm=new HonoraireForm();
			double somH=0 ,somP=0;
			for(Seance s : seances) {
				if(s.getProfesseur().equals(prof)) somH+=s.getDuree();
			  }
				for(Payer p : payers) {
					if(p.getProfesseur().equals(prof)) somP+=p.getHonoraire();
				 }
				honoraireForm.setId(prof.getId());
			    honoraireForm.setPrenom(prof.getPrenom());
			    honoraireForm.setNom(prof.getNom());
			    honoraireForm.setHonoraire(somH-somP);
			    if(somP>somH) honoraireForm.setHonoraire(null);
			    honoraireForm.setDejaPayer(somP);
			    listes.add(honoraireForm);	
		}return listes;
		
	}
	
	@GetMapping("/honorairePr/{id}")
	public HonoraireForm HonorairePr(@PathVariable Long id){
		Professeur prof=professeurRepository.findById(id).get();
		List<Seance> seances =seanceRepository.findAll();
		List<Payer> payers=payerRepository.findAll();
		
			HonoraireForm honoraireForm=new HonoraireForm();
			double somH=0 ,somP=0;
			for(Seance s : seances) {
				if(s.getProfesseur().equals(prof)) somH+=s.getDuree();
			  }
				for(Payer p : payers) {
					if(p.getProfesseur().equals(prof)) somP+=p.getHonoraire();
				 }
			    honoraireForm.setId(prof.getId());
			    honoraireForm.setPrenom(prof.getPrenom());
			    honoraireForm.setNom(prof.getNom());
			    honoraireForm.setHonoraire(somH-somP);
			    if(somP>somH) honoraireForm.setHonoraire(null);
			    honoraireForm.setDejaPayer(somP);
			   
		    return honoraireForm;
	}
	
	      @Data
	       public class HonoraireForm {
		
		  private Long id;
		  private String prenom;
		  private String nom;
		  private Double honoraire;
		  private double DejaPayer;	
	     }
	      
	      @GetMapping("/honoraireTotal")
	  	public HonoraireTotal HonoraireTotal(){
	  		List<Seance> seances =seanceRepository.findAll();
	  		List<Payer> payers=payerRepository.findAll();
	  		
	  		HonoraireTotal honoraireTotal=new HonoraireTotal();
	  			double somH=0 ,somP=0;
	  			for(Seance s : seances) {
	  				 somH+=s.getDuree();
	  			  }
	  				for(Payer p : payers) {
	  					 somP+=p.getHonoraire();
	  				 }
	  				honoraireTotal.setHonoraire(somH-somP);
	  				honoraireTotal.setDejaPayer(somP);
	  			   
	  		    return honoraireTotal;
	  	}
	      
	      @Data
	       public class HonoraireTotal {
		  private double honoraire;
		  private double DejaPayer;	
	     }
	      
	

}
