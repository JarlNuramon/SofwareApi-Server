package de.hs.bochum.entities;

import lombok.Data;

@Data
public class CreateMessreiheRequest {

	private int zeitIntervall;
	private String verbraucher;
	private String messgroesse;
	private int id;
}
