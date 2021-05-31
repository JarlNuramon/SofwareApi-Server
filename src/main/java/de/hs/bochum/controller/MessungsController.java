package de.hs.bochum.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.hs.bochum.buisness.services.MessungsService;
import de.hs.bochum.entities.CreateMessreiheRequest;
import de.hs.bochum.messung.Messreihe;
import de.hs.bochum.messung.MessreiheResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "api/v1")
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
public class MessungsController {
	@Autowired
	private MessungsService messService;
	private Messreihe aktuelleMessung;
	
	
	@GetMapping(value = "/messung")
	@ApiResponse(responseCode = "200", description = "Erhalte alle Messungen", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = MessreiheResponse.class)) })
	@Operation(summary = "Gibt alle Messeihen zurück")
	public ResponseEntity<MessreiheResponse> getMessreihen() {
			return ResponseEntity.ok().body(MessreiheResponse.builder().messungen( messService.getAlleMessungen()).build());

	}
	@PostMapping(value = "messung/create")
	@ApiResponse(responseCode = "200", description = "Messung wurde gespeichert", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = Messreihe.class)) })
	@ApiResponse(responseCode = "400", description = "Messungsid existert schon")
	@Operation(summary = "Messung wurde gestarted")
	public ResponseEntity erstellMessreihe(@RequestBody CreateMessreiheRequest request) throws Exception{
		try {
		messService.erstellMessung(request.getZeitIntervall(), request.getVerbraucher(),request.getMessgroesse(), request.getId());
		return ResponseEntity.ok().build();
		}catch(Exception e) {
			throw new Exception ("Messungsid existiert");
		}
	}
	
	@PostMapping(value = "messung/start/{id}")
	@ApiResponse(responseCode = "200", description = "Messung wurde gestartet", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = Messreihe.class)) })
	@ApiResponse(responseCode = "403", description = "Eine Messung lief schon")
	@Operation(summary = "Messung wurde gestarted")
	public ResponseEntity startMessreihe(@PathVariable("id") int id) {
		Optional<Messreihe> m = messService.getMessungById(id);
		if(!m.isPresent())
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Not found");
		if(aktuelleMessung==null) {
			aktuelleMessung = messService.startMessung(m.get());
		return ResponseEntity.ok().build();
		}
		return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build();
	}
	
	@PostMapping(value = "/messung/stop/{id}")
	@ApiResponse(responseCode = "200", description = "Messung wurde gestoppt und Messreihe zurückgeschickt", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = Messreihe.class)) })
	@Operation(summary = "Stoppt die letzte Messung und gibt die Messreihe dazu zurück")
	public ResponseEntity<Messreihe> stoppMessung(@PathVariable("id") int id) {
		messService.stopMessung(aktuelleMessung);
		ResponseEntity<Messreihe> messResponse= ResponseEntity.status(HttpStatus.OK).body(aktuelleMessung);
		aktuelleMessung=null;
		return messResponse;

	}

}
