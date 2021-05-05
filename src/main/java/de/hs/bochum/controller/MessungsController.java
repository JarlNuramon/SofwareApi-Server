package de.hs.bochum.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.hs.bochum.buisnessEmu.MessungsService;
import de.hs.bochum.messung.Messreihe;
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
	MessungsService messService;
	@GetMapping(value = "/messung/{id}")
	@ApiResponse(responseCode = "200", description = "Messreihe wird zurück geschickt", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = Messreihe.class)) })
	@ApiResponse(responseCode = "204", description = "Messung für die ID wurde nicht gefunden.", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = Messreihe.class)) })
	@Operation(summary = "Returns all Sessions but not their events")
	public ResponseEntity<Messreihe> getMessreihe(@RequestParam("id") int id) {
		Optional<Messreihe> messung = messService.getMessungById(id);
		if (messung.isPresent())
			return ResponseEntity.ok().body(messung.get());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

	}
	private Messreihe aktuelleMessung;
	@PostMapping(value = "/messung")
	@ApiResponse(responseCode = "200", description = "Messung wurde gestartet", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = Messreihe.class)) })
	@ApiResponse(responseCode = "403", description = "Eine Messung lief schon")
	@Operation(summary = "Messung wurde gestarted")
	public ResponseEntity startMessReihe(@RequestBody StartMessungRequest request) {
		if(aktuelleMessung==null) {
			aktuelleMessung = messService.startMessung(request.getZeitIntervall(), request.getVerbraucher(),request.getMessgroesse(), request.getId());
		return ResponseEntity.ok().build();
		}
		return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build();
	}
	@PostMapping(value = "/messung/{id}")
	@ApiResponse(responseCode = "200", description = "Messung wurde gestoppt und Messreihe zurückgeschickt", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = Messreihe.class)) })
	@Operation(summary = "Stoppt die letzte Messung und gibt die Messreihe dazu zurück")
	public ResponseEntity<Messreihe> stoppMessung(@RequestParam("id") int id) {
		messService.stopMessung(aktuelleMessung);
		ResponseEntity<Messreihe> messResponse= ResponseEntity.status(HttpStatus.NO_CONTENT).body(aktuelleMessung);
		aktuelleMessung=null;
		return messResponse;

	}

}
