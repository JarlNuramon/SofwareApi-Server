package de.hs.bochum.entities.messung;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class Messreihe {
	@Id
	private int messreihenId;
	private int zeitintervall;
	private String verbraucher;
	private String messgroesse;
	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Messung> elements;
	
	
	public String gibAttributeAus(){
		return this.messreihenId + " " 
	 	    + this.zeitintervall + " " + this.verbraucher + " " + this.messgroesse;
	}

}