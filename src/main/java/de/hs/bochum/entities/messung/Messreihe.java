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
import javax.validation.constraints.Min;
import javax.validation.constraints.Negative;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

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
	@Positive
	private int messreihenId;

	@Positive
	@Min(15)
	private int zeitintervall;
	@NotNull
	@NotBlank
	private String verbraucher;
	@NotNull
	@NotBlank
	@Pattern(regexp = "Leistung|Arbeit")
	private String messgroesse;
	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Messung> elements;

	public Messreihe(int messreihenId, int zeitintervall) throws IllegalArgumentException {
		super();
		this.messreihenId = messreihenId;
		if (zeitintervall >= 15 && zeitintervall <= 3600) {
			this.zeitintervall = zeitintervall;
		} else if (zeitintervall < 15) {
			throw new IllegalArgumentException("Das Zeitintervall muss mindestens 15 Sekunden" + " lang sein.");
		} else {
			throw new IllegalArgumentException("Das Zeitintervall darf hoechstens" + " 3600 Sekunden lang sein.");
		}
	}

	public String gibAttributeAus() {
		return this.messreihenId + " " + this.zeitintervall + " " + this.verbraucher + " " + this.messgroesse;
	}

}
