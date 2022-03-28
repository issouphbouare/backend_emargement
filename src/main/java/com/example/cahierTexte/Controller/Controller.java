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

	@RequestMapping(value = "/login/{tel}", method = RequestMethod.GET)
	public Professeur getProfesseurByTelPassword(@PathVariable("tel") int tel) {
		return professeurRepository.findByTel(tel);
	}

	@GetMapping("/honorairePr")
	public List<HonoraireForm> HonorairePr() {
		List<Professeur> profs = professeurRepository.findAll();
		List<Seance> seances = seanceRepository.findAll();
		List<Payer> payers = payerRepository.findAll();
		List<HonoraireForm> listes = new ArrayList<>();

		for (Professeur prof : profs) {
			HonoraireForm honoraireForm = new HonoraireForm();
			double somH = 0, somP = 0;
			for (Seance s : seances) {
				if (s.getProfesseur().equals(prof))
					somH += s.getDuree();
			}
			for (Payer p : payers) {
				if (p.getProfesseur().equals(prof))
					somP += p.getHonoraire();
			}
			honoraireForm.setId(prof.getId());
			honoraireForm.setPrenom(prof.getPrenom());
			honoraireForm.setNom(prof.getNom());
			honoraireForm.setHonoraire(somH - somP);
			if (somP > somH)
				honoraireForm.setHonoraire(null);
			honoraireForm.setDejaPayer(somP);
			listes.add(honoraireForm);
		}
		return listes;

	}

	@GetMapping("/honorairePr/{id}")
	public HonoraireForm HonorairePr(@PathVariable Long id) {
		Professeur prof = professeurRepository.findById(id).get();
		List<Seance> seances = seanceRepository.findAll();
		List<Payer> payers = payerRepository.findAll();

		HonoraireForm honoraireForm = new HonoraireForm();
		double somH = 0, somP = 0;
		for (Seance s : seances) {
			if (s.getProfesseur().equals(prof))
				somH += s.getDuree();
		}
		for (Payer p : payers) {
			if (p.getProfesseur().equals(prof))
				somP += p.getHonoraire();
		}
		honoraireForm.setId(prof.getId());
		honoraireForm.setPrenom(prof.getPrenom());
		honoraireForm.setNom(prof.getNom());
		honoraireForm.setHonoraire(somH - somP);
		if (somP > somH)
			honoraireForm.setHonoraire(null);
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
	public HonoraireTotal HonoraireTotal() {
		List<Seance> seances = seanceRepository.findAll();
		List<Payer> payers = payerRepository.findAll();

		HonoraireTotal honoraireTotal = new HonoraireTotal();
		double somH = 0, somP = 0;
		for (Seance s : seances) {
			somH += s.getDuree();
		}
		for (Payer p : payers) {
			somP += p.getHonoraire();
		}
		honoraireTotal.setHonoraire(somH - somP);
		honoraireTotal.setDejaPayer(somP);

		return honoraireTotal;
	}

	@GetMapping("endNS")
	public DernierN dernierNumeroS() {
		List<Seance> seances = seanceRepository.findAll();
		DernierN dernierN = new DernierN();
		long max = 0;
		for (Seance s : seances) {
			if (s.getId() > max)
				max = s.getId();
		}
		dernierN.setNum(max + 1);
		return dernierN;
	}

	@GetMapping("endNP")
	public DernierN dernierNumeroA() {
		List<Payer> payers = payerRepository.findAll();
		DernierN dernierN = new DernierN();
		long max = 0;
		for (Payer s : payers) {
			if (s.getId() > max)
				max = s.getId();
		}
		dernierN.setNum(max + 1);
		return dernierN;
	}

	@Data
	public class DernierN {
		private long num;
	}

	@Data
	public class HonoraireTotal {
		private int annee;
		private double honoraire;
		private double DejaPayer;
	}

	@Data
	public class HonoraireFormMois {
		private int annee;
		private int mois;
		private Long id;
		private String prenom;
		private String nom;
		private Double honoraire;
		 private double DejaPayer;
	}
	
    //Determine l' honoraires d'un prof donné dans un mois donné ds une annee
	@GetMapping("/honoraireMoisPr/{id}/{mois}/{annee}")
	public List<HonoraireFormMois> HonoraireMoisPr(@PathVariable Long id,@PathVariable int mois,@PathVariable int annee) {
		Professeur prof = professeurRepository.findById(id).get();
		List<Seance> seances = seanceRepository.findAll();
		List<Payer> payers = payerRepository.findAll();
		List<HonoraireFormMois> listes = new ArrayList<>();

		int i = mois,y=annee;
		
			HonoraireFormMois honoraireFormMois = new HonoraireFormMois();
			double hon = 0, deja=0;

			for (Seance s : seances) {

				if (s.getProfesseur().equals(prof) && s.getDate().getMonth()+1 == i
						&& s.getDate().getYear()+1900==y)  hon += s.getDuree();
			}
			
			for(Payer p:payers) {
				if(p.getProfesseur().equals(prof) &&
						p.getMoisPayer()==i && p.getAnneePayer()==y) deja+=p.getHonoraire();
			}
			honoraireFormMois.setAnnee(y);
			honoraireFormMois.setMois(i);
			honoraireFormMois.setId(prof.getId());
			honoraireFormMois.setNom(prof.getNom());
			honoraireFormMois.setPrenom(prof.getPrenom());
			honoraireFormMois.setHonoraire(hon-deja);
			honoraireFormMois.setDejaPayer(deja);
			listes.add(honoraireFormMois);
		
		return listes;
	}
	
     //Determine les honoraires des profs pr un mois donné ds une année donnée
	@GetMapping("/honoraireMois/{mois}/{annee}")
	public List<HonoraireFormMois> HonoraireMois(@PathVariable int mois,@PathVariable int annee) {
		List<Professeur> profs = professeurRepository.findAll();
		List<Seance> seances = seanceRepository.findAll();
		List<Payer> payers = payerRepository.findAll();
		List<HonoraireFormMois> listes = new ArrayList<>();

		int i = mois, y=annee;
		for (Professeur prof : profs) {
			HonoraireFormMois honoraireFormMois = new HonoraireFormMois();
			double hon = 0, deja=0;

			for (Seance s : seances) {

				if (s.getProfesseur().equals(prof) && s.getDate().getMonth()+1 == i &&
						s.getDate().getYear()+1900==y) hon += s.getDuree();
				for(Payer p:payers) {
					if(p.getProfesseur().equals(prof) && p.getMoisPayer()==i &&
							p.getMoisPayer()==i && p.getAnneePayer()==y) deja+=p.getHonoraire();
				}
				
			}
			honoraireFormMois.setAnnee(y);
			honoraireFormMois.setMois(i);
			honoraireFormMois.setId(prof.getId());
			honoraireFormMois.setNom(prof.getNom());
			honoraireFormMois.setPrenom(prof.getPrenom());
			honoraireFormMois.setHonoraire(hon-deja);
			honoraireFormMois.setDejaPayer(deja);
			listes.add(honoraireFormMois);
		}

		return listes;
	}

	// Determine les honoraires mensuels pr un prof donné pour une annee donnee
	@GetMapping("/honorairePrMois/{id}/{annee}")
	public List<HonoraireFormMois> HonorairePrMois(@PathVariable Long id,@PathVariable int annee) {
		Professeur prof = professeurRepository.findById(id).get();
		List<Seance> seances = seanceRepository.findAll();
		List<Payer> payers = payerRepository.findAll();
		List<HonoraireFormMois> listes = new ArrayList<>();
		int y=annee;

		for (int i = 1; i <=12; i++) {
			

			HonoraireFormMois honoraireFormMois = new HonoraireFormMois();
			double hon = 0,deja=0;

			for (Seance s : seances) {

				if (s.getProfesseur().equals(prof) && s.getDate().getMonth()+1 == i
						&& s.getDate().getYear()+1900==y) hon += s.getDuree();
			}
			for(Payer p : payers) {
				if(p.getProfesseur().equals(prof) && p.getMoisPayer()==i &&
						p.getAnneePayer()==y) deja+=p.getHonoraire();
			}
			honoraireFormMois.setAnnee(y);
			honoraireFormMois.setMois(i);
			honoraireFormMois.setId(prof.getId());
			honoraireFormMois.setNom(prof.getNom());
			honoraireFormMois.setPrenom(prof.getPrenom());
			honoraireFormMois.setHonoraire(hon-deja);
			honoraireFormMois.setDejaPayer(deja);
			listes.add(honoraireFormMois);
			

		}

		return listes;
	}
	
	//honoraires des profs dans une annee donnee
	@GetMapping("/honorairePrAnnee/{annee}")
	public List<HonoraireFormMois> HonorairePrAnnee(@PathVariable int annee) {
		List<Professeur> profs = professeurRepository.findAll();
		List<Seance> seances = seanceRepository.findAll();
		List<Payer> payers = payerRepository.findAll();
		List<HonoraireFormMois> listes = new ArrayList<>();
          int y=annee;
		for (Professeur prof : profs) {
			HonoraireFormMois honoraireFormMois = new HonoraireFormMois();
			double somH = 0, somP = 0;
			for (Seance s : seances) {
				if (s.getProfesseur().equals(prof) && s.getDate().getYear()+1900==y)
					somH += s.getDuree();
			}
			for (Payer p : payers) {
				if (p.getProfesseur().equals(prof) && p.getAnneePayer()==y)
					somP += p.getHonoraire();
			}
			honoraireFormMois.setAnnee(y);
			honoraireFormMois.setId(prof.getId());
			honoraireFormMois.setPrenom(prof.getPrenom());
			honoraireFormMois.setNom(prof.getNom());
			honoraireFormMois.setHonoraire(somH - somP);
			if (somP > somH)
				honoraireFormMois.setHonoraire(null);
			honoraireFormMois.setDejaPayer(somP);
			listes.add(honoraireFormMois);
		}
		return listes;

	}
	
	//honoraires d'un prof dans une annee donnee
		@GetMapping("/honorairePrAnnee/{id}/{annee}")
		public List<HonoraireFormMois> HonorairePrAnnee(@PathVariable Long id,@PathVariable int annee) {
			Professeur prof = professeurRepository.findById(id).get();
			List<Seance> seances = seanceRepository.findAll();
			List<Payer> payers = payerRepository.findAll();
			List<HonoraireFormMois> listes = new ArrayList<>();
	          int y=annee;
			
				HonoraireFormMois honoraireFormMois = new HonoraireFormMois();
				double somH = 0, somP = 0;
				for (Seance s : seances) {
					if (s.getProfesseur().equals(prof) && s.getDate().getYear()+1900==y)
						somH += s.getDuree();
				}
				for (Payer p : payers) {
					if (p.getProfesseur().equals(prof) && p.getAnneePayer()==y)
						somP += p.getHonoraire();
				}
				honoraireFormMois.setAnnee(y);
				honoraireFormMois.setId(prof.getId());
				honoraireFormMois.setPrenom(prof.getPrenom());
				honoraireFormMois.setNom(prof.getNom());
				honoraireFormMois.setHonoraire(somH - somP);
				if (somP > somH)
					honoraireFormMois.setHonoraire(null);
				honoraireFormMois.setDejaPayer(somP);
				listes.add(honoraireFormMois);
			
			return listes;

		}
		
		@GetMapping("/honoraireTotalAnnee/{annee}")
		public HonoraireTotal HonoraireTotalAnnee(@PathVariable int annee) {
			List<Seance> seances = seanceRepository.findAll();
			List<Payer> payers = payerRepository.findAll();

			HonoraireTotal honoraireTotal = new HonoraireTotal();
			double somH = 0, somP = 0;
			int y=annee;
			for (Seance s : seances) {
				if(s.getDate().getYear()+1900==y) somH += s.getDuree();
			}
			for (Payer p : payers) {
				if(p.getAnneePayer()==y) somP += p.getHonoraire();
			}
			honoraireTotal.setAnnee(y);
			honoraireTotal.setHonoraire(somH - somP);
			honoraireTotal.setDejaPayer(somP);

			return honoraireTotal;
		}

}
