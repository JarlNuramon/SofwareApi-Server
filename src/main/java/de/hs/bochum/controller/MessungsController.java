package de.hs.bochum.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.hs.bochum.buisness.services.MessungsService;
import de.hs.bochum.dto.CreateMessreiheRequest;
import de.hs.bochum.entities.messung.Messreihe;
import de.hs.bochum.entities.messung.MessreiheResponse;
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
	@Operation(summary = "Messung wird erstellt")
	public ResponseEntity<String> erstellMessreihe(@RequestBody CreateMessreiheRequest request) throws Exception{
		try {
		messService.erstellMessung(request.getZeitIntervall(), request.getVerbraucher(),request.getMessgroesse(), request.getId());
		return ResponseEntity.ok().build();
		}catch(Exception e) {
			return ResponseEntity.status(400).body("MessungsId existiert schon");
		}
	}
	
	@DeleteMapping(value = "messung/{id}")
	@ApiResponse(responseCode = "200", description = "Löscht eine Messung mit der ID.")
	@ApiResponse(responseCode = "400", description = "Messung mit der ID wird noch durchgeführt")
	@Operation(summary = "Messung wird gelöscht")
	public ResponseEntity<String> deleteMessreihe(@PathVariable("id") int id) throws Exception{
			if(aktuelleMessung !=null && aktuelleMessung.getMessreihenId() ==id)
				return ResponseEntity.status(400).body("Messung mit dieser ID wird zeitlich durchgeführt. Erst speichern dann löschen.");
			messService.deleteMessreihe(id);
		return ResponseEntity.ok().build();
	
	}
	
	
	
	
	@PostMapping(value = "messung/start/{id}")
	@ApiResponse(responseCode = "200", description = "Messung wurde gestartet", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = Messreihe.class)) })
	@ApiResponse(responseCode = "403", description = "Eine Messung lief schon")
	@Operation(summary = "Messung wird gestartet")
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
