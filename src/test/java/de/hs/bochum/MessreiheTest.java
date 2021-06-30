package de.hs.bochum;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import de.hs.bochum.entities.messung.Messreihe;

@RunWith(SpringRunner.class)
public class MessreiheTest {
	 private static Validator validator;

	 @BeforeClass
	 public static void setupValidatorInstance() {
	     validator = Validation.buildDefaultValidatorFactory().getValidator();
	 }
	@Test
	public void testLeistung() {
	 Messreihe 	m = Messreihe.builder().messreihenId(1).zeitintervall(20).messgroesse("Leistung").verbraucher("LED").build();
	 assertTrue(m.getMessgroesse().equals("Leistung")&&m.getMessreihenId()==1&&m.getVerbraucher().equals("LED")&&m.getZeitintervall()==20);
	}
	@Test
	public void testArbeit() {

		 Messreihe 	m = Messreihe.builder().messreihenId(1).zeitintervall(20).messgroesse("Arbeit").verbraucher("LED").build();
		 assertTrue(m.getMessgroesse().equals("Arbeit")&&m.getMessreihenId()==1&&m.getVerbraucher().equals("LED")&&m.getZeitintervall()==20);
	}
	@Test
	/**
	 * zeitintervall untere Grenze 15
	 * messreiheId untere Grenze
	 * verbraucher blank
	 * messgröße auch blank
	 */
	public void testBlankError() {

		 Messreihe 	m = Messreihe.builder().messreihenId(Integer.MIN_VALUE).zeitintervall(15).messgroesse("").verbraucher("").build();
		 assertTrue(m.getMessgroesse().equals("")&&m.getMessreihenId()==Integer.MIN_VALUE&&m.getVerbraucher().equals("") &&m.getZeitintervall()==15);
		 Set<ConstraintViolation<Messreihe>> violations = validator.validate(m);
	     assertFalse(violations.isEmpty(),"Kein Fehler geworfen, obwohl messgroeße blank");
	}
	@Test
	/**
	 * zeitintervall Oben Grenze
	 * messreiheId mitte
	 * verbraucher null
	 * messgröße auch null
	 */
	public void testNullError() {

		 Messreihe 	m = Messreihe.builder().messreihenId(1).zeitintervall(Integer.MAX_VALUE).messgroesse(null).verbraucher(null).build();
		 assertTrue(m.getMessgroesse()==null&&m.getMessreihenId()==1&&m.getVerbraucher()==null &&m.getZeitintervall()==Integer.MAX_VALUE);
		 Set<ConstraintViolation<Messreihe>> violations = validator.validate(m);
	     assertFalse(violations.isEmpty(),"Kein Fehler geworfen, obwohl messgroeße null");
	}
	@Test
	/**
	 * zeitintervall 14 fehler
	 * messreiheId Upper Grenze
	 * verbraucher x
	 * messgröße  "Leistung"
	 */
	public void testZeitintervallTooLow() {

		 Messreihe 	m = Messreihe.builder().messreihenId(Integer.MAX_VALUE).zeitintervall(14).messgroesse("Leistung").verbraucher("x").build();
		 assertTrue(m.getMessgroesse().equals("Leistung")&&m.getMessreihenId()==Integer.MAX_VALUE&&m.getVerbraucher().equals("x") &&m.getZeitintervall()==14);
		 Set<ConstraintViolation<Messreihe>> violations = validator.validate(m);
	     assertFalse(violations.isEmpty(), "Kein Fehler  für zeitintervall geworfen");
	}
	@Test
	/**
	 * zeitintervall 17
	 * messreiheId mitte
	 * verbraucher y
	 * messgröße  "Arbeit"
	 */
	public void testLeistungMitteZeitintervall() {

		 Messreihe 	m = Messreihe.builder().messreihenId(1).zeitintervall(17).messgroesse("Arbeit").verbraucher("y").build();
		 assertTrue(m.getMessgroesse().equals("Arbeit")&&m.getMessreihenId()==1&&m.getVerbraucher().equals("y") &&m.getZeitintervall()==17);
		 Set<ConstraintViolation<Messreihe>> violations = validator.validate(m);
	     assertTrue(violations.isEmpty(), "Fehler beim Validieren. Es sollte kein geben");
	}
	@Test
	/**
	 * zeitintervall 17
	 * messreiheId mitte
	 * verbraucher ys
	 * messgröße  "undefined"
	 */
	public void testMessgroesseUndefined() {
		 Messreihe 	m = Messreihe.builder().messreihenId(1).zeitintervall(17).messgroesse("undefined").verbraucher("ys").build();
		 assertTrue(m.getMessgroesse().equals("undefined")&&m.getMessreihenId()==1&&m.getVerbraucher().equals("ys") &&m.getZeitintervall()==17);
		 Set<ConstraintViolation<Messreihe>> violations = validator.validate(m);
	     assertFalse(violations.isEmpty(),"Kein Fehler für die Validierung geworfen obwohl messgoesse undefined");
	}
}
