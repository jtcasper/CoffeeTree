package coffeeTree;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ObservationParserTest {
	
	ObservationParser parser = new ObservationParser();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testParseString() {
		String unclassifiedObservationString = "hasCountryOfOrigin:Guatemala,hasRoast:Light";
		String classifiedObservationString = "Bitter,hasCountryOfOrigin:Indonesia,hasRoast:Dark";
		String classString = classifiedObservationString.split(",")[0];
		
		Observation unclassifiedObservation = parser.parseString(unclassifiedObservationString, false);
		assertNull(unclassifiedObservation.getClassification());
		//TODO change this when Attributes class is implemented
		assertEquals(unclassifiedObservation.getAttributes()[0], unclassifiedObservationString.split(",")[0]);
		
		Observation classifiedObservation = parser.parseString(classifiedObservationString, true);
		assertNotNull(classifiedObservation.getClassification());
		assertEquals(classString, classifiedObservation.getClassification());
		//TODO change this when Attributes class is implemented
		assertEquals(classifiedObservation.getAttributes()[0], classifiedObservationString.split(",")[1]);

		
	}

	@Test
	public void testParseFile() {
		File classifiedFile = new File("test/resources/parse_test_data.csv");
		ArrayList<Observation> classifiedObservations = null;
		ArrayList<Observation> unclassifiedObservations = null;
		ArrayList<Observation> testClassifiedObs = new ArrayList<Observation>();
		testClassifiedObs.add(new Observation(new String[] {"hasCorporateEntity:Guardian", "hasCountry:UK", "hasCompanyType:Media"}, "disclosure"));
		testClassifiedObs.add(new Observation(new String[] {"hasGovernmentEntity:", "hasCountry:USA"}, "increased accessibility"));
		ArrayList<Observation> testUnclassifiedObs = new ArrayList<Observation>();
		testUnclassifiedObs.add(new Observation(new String[] {"disclosure", "hasCorporateEntity:Guardian", "hasCountry:UK", "hasCompanyType:Media"}));
		testUnclassifiedObs.add(new Observation(new String[] {"increased accessibility","hasGovernmentEntity:", "hasCountry:USA"}));

		try {
			classifiedObservations = parser.parseFile(classifiedFile.getAbsolutePath(), true);
			unclassifiedObservations = parser.parseFile(classifiedFile.getAbsolutePath(), false);
		} catch (IOException e) {
			e.printStackTrace();
			fail("The file does exist");
		}
		
		assertNotNull(classifiedObservations);
		assertTrue(classifiedObservations.equals(testClassifiedObs));
		
		assertNotNull(unclassifiedObservations);
		assertTrue(unclassifiedObservations.equals(testUnclassifiedObs));
		
		
	}

}
