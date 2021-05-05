package de.hs.bochum.messung;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
public class Messung {
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Id
	private int laufendeNummer;
	@ManyToOne(fetch = FetchType.LAZY)
	private Messreihe parent;
	private double wert;

	public String gibAttributeAus(){
		return this.laufendeNummer + " " + this.wert;
	}
}
