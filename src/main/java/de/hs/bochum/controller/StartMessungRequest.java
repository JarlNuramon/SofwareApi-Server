package de.hs.bochum.controller;

import lombok.Data;

@Data
public class StartMessungRequest {

	private int zeitIntervall;
	private String verbraucher;
	private String messgroesse;
	private int id;
}
