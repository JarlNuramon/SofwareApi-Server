package de.hs.bochum;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.jbehave.core.annotations.*;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hs.bochum.buisness.emu.EmuCheckConnection;
import de.hs.bochum.buisness.services.MessungsService;
import de.hs.bochum.entities.messung.Messreihe;
import de.hs.bochum.repository.MessungsRepository;
import lombok.extern.slf4j.Slf4j;

import java.awt.Desktop;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class MessreiheStartenSteps {

	@Mock
	MessungsRepository messRepo;
	@Mock
	private EmuCheckConnection ecc;
	@InjectMocks
	MessungsService messungsService;
	Throwable t;
	@BeforeStory 
	  public void setUp() {
	    MockitoAnnotations.initMocks(this);
	}
	@Given("The current size of messung with $id, $intervall, $consumer, $kind is $size")
	public void initMessung(int id, int intervall, String consumer, String kind, int size) {
		Optional<Messreihe> m = Optional.of(Messreihe.builder().elements(new ArrayList<>(size))
				.zeitintervall(intervall).verbraucher(consumer).messgroesse(kind).messreihenId(id).build());
		when(messRepo.findById(id)).thenReturn(m);
	}

	@When("I create messungen for $id and messreihen are $messungen")
	public void createMessungenForMessreihe(int id,List<Double> messungen) {
		int s = messungen.size();
		if(!messungen.isEmpty())
			when(ecc.gibErgebnisAus()).thenReturn(messungen.remove(0),messungen.toArray(new Double[messungen.size()]));
		for(int i=0;i<s;i++)
			messungsService.addMessungToMessreihe(messungsService.getMessungById(id).get(), ecc.gibErgebnisAus());
	}

	@Then("I should see $size messungen for element with $id")
	public void checkSize(int size, int id) {
		assertTrue(messRepo.findById(id).get().getElements().size()==size ,"SizeCheck");
	}
	@Given("there is a messung with the id $id already available")
	public void initMessung(int id) {
		Optional<Messreihe> m = Optional.of(Messreihe.builder().elements(new ArrayList<>(1))
				.zeitintervall(15).verbraucher("x").messgroesse("Strom").messreihenId(id).build());
		when(messRepo.findById(id)).thenReturn(m);
	}

	@When("I create messungen with the id $id")
	public void createMessungenForMessreihe(int id) {
		try {
		messungsService.alternativeErstellMessung(2, "x", null, id);
		
		}catch(Throwable t) {
			log.info("Thrown error {}?",t.getMessage());
			this.t = t;
		}
	}

	@Then("the a error message appears, saying: $message")
	public void throwsErrorMessage(String message) {
		log.info("message ={} and t={}",message,this.t.getMessage());
		  assertTrue(()->this.t instanceof IllegalArgumentException && this.t.getMessage().equals(message) ,"Correct Message was thrown");
		  
	}


	@AfterStories
	public void openReport() throws IOException {
		File htmlFile = new File("target\\jbehave\\view\\reports.html");
		Desktop.getDesktop().browse(htmlFile.toURI());
	}

}
