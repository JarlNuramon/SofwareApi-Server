package de.hs.bochum;

import static org.assertj.core.api.Assertions.assertThat;
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

import java.awt.Desktop;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class MessreiheStartenWithErrorSteps {

//	@Mock
//	MessungsRepository messRepo;
//	@Mock
//	private EmuCheckConnection ecc;
//	@InjectMocks
//	MessungsService messungsService;
//	Throwable t;
//	@BeforeStory 
//	  public void setUp() {
//	    MockitoAnnotations.initMocks(this);
//	}
//	@Given("there is a messung with the id $id already available")
//	public void initMessung(int id) {
//		Optional<Messreihe> m = Optional.of(Messreihe.builder().elements(new ArrayList<>(1))
//				.zeitintervall(15).verbraucher("x").messgroesse("Strom").messreihenId(id).build());
//		when(messRepo.findById(id)).thenReturn(m);
//	}
//
//	@When("I create messungen with the id $id ")
//	public void createMessungenForMessreihe(int id) {
//		try {
//		messungsService.alternativeErstellMessung(2, "x", null, id);
//		}catch(Throwable t) {
//			this.t = t;
//		}
//	}
//
//	@Then("the a error message appears, saying: $message")
//	public void throwsErrorMessage(String message) {
//		  assertTrue(()->t.getMessage().equals(message),"Correct Message was thrown");
//		  
//	}
//
//	@AfterStories
//	public void openReport() throws IOException {
//		File htmlFile = new File("target\\jbehave\\view\\reports.html");
//		Desktop.getDesktop().browse(htmlFile.toURI());
//	}

}
