package de.hs.bochum.messung;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Messung {
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Id
	private int id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Messreihe parent;
	private double wert;
	private int laufendeNummer;
	
	public String gibAttributeAus(){
		return this.laufendeNummer + " " + this.wert;
	}
}
