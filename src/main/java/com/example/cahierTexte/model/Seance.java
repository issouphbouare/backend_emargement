package com.example.cahierTexte.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data  
@AllArgsConstructor 
@NoArgsConstructor 
@ToString
public class Seance {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Temporal(TemporalType.DATE)
	@Column(columnDefinition="Date DEFAULT CURRENT_TIMESTAMP",insertable = false, updatable = false)
	private Date date;
	//private String titre;
	//private String type;
	private String horaire;
	private double duree;
	//private String contenu;
	
	@ManyToOne
	private Classe classe;
	@ManyToOne
	private Matiere matiere;
	@ManyToOne
	private Professeur professeur;
	

	
	
	

}
