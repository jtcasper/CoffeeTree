package coffeeTree;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CoffeeTreeTest {
	
	private static Observation class0Obs;
	private static Observation class1Obs;
	private static Observation class2Obs;
	private ArrayList<Observation> binaryClassObservations;
	private ArrayList<Observation> ternaryClassObservations;
	private static AbstractMetric binaryClassGiniMetric;
	private static AbstractMetric ternaryClassGiniMetric;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		class0Obs = new Observation(new String[]{"useless"}, "0");
		class1Obs = new Observation(new String[]{"less useless"}, "1");
		class2Obs = new Observation(new String[]{"least useless"}, "2");
		binaryClassGiniMetric = new WeightedGiniMetric(2);
		ternaryClassGiniMetric = new WeightedGiniMetric(3);
	}

	@Before
	public void setUp() throws Exception {
		binaryClassObservations = new ArrayList<Observation>();
		for(int i = 0; i < 7; i++) {
			binaryClassObservations.add(class0Obs);
		}
		for(int i = 0; i < 5; i++) {
			binaryClassObservations.add(class1Obs);
		}
		ternaryClassObservations = new ArrayList<Observation>();
		for(int i = 0; i < 7; i++) {
			ternaryClassObservations.add(class0Obs);
		}
		for(int i = 0; i < 5; i++) {
			ternaryClassObservations.add(class1Obs);
		}
		for(int i = 0; i < 3; i++) {
			ternaryClassObservations.add(class2Obs);
		}

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCoffeeTree() {
		CoffeeTree tree = new CoffeeTree(binaryClassObservations, binaryClassGiniMetric);
		assertNotNull(tree);
	}

	@Test
	public void testTrainModel() {
		CoffeeTree tree = new CoffeeTree(binaryClassObservations, binaryClassGiniMetric);
		CoffeeTree tree2 = new CoffeeTree(binaryClassObservations, binaryClassGiniMetric);
		CoffeeTree tree3d = new CoffeeTree(ternaryClassObservations, ternaryClassGiniMetric);
		
		assertEquals(tree, tree2);
		assertNotEquals(tree, tree3d);
		
		tree.trainModel();
		assertFalse(tree.equals(tree2));
		assertFalse(tree.equals(tree3d));
		tree2.trainModel();
		assertTrue(tree.equals(tree2));
		tree3d.trainModel();
		
		System.out.println(tree.getRoot());
	}
	
	@Test
	public void testPredictObservation() {
		CoffeeTree tree3d = new CoffeeTree(ternaryClassObservations, ternaryClassGiniMetric);
		tree3d.trainModel();
		Observation unclassifiedObservation = new Observation(new String[]{"useless"});
		Observation unclassifiedObservation2 = new Observation(new String[]{"less useless"});
		Observation unclassifiedObservation3 = new Observation(new String[]{"least useless"});
		tree3d.predictObservation(unclassifiedObservation);
		//Test initial split
		assertNotNull(unclassifiedObservation.getClassification());
		assertEquals("0", unclassifiedObservation.getClassification());
		//Test secondary split
		tree3d.predictObservation(unclassifiedObservation2);
		assertNotNull(unclassifiedObservation2.getClassification());
		assertEquals("1", unclassifiedObservation2.getClassification());
		//Test right side of secondary split
		tree3d.predictObservation(unclassifiedObservation3);
		assertNotNull(unclassifiedObservation3.getClassification());
		assertEquals("2", unclassifiedObservation3.getClassification());
		
	}

	@Test
	public void testGetRoot() {
		CoffeeTree tree = new CoffeeTree(binaryClassObservations, binaryClassGiniMetric);
		CoffeeTree tree2 = new CoffeeTree(binaryClassObservations, binaryClassGiniMetric);
		assertNotNull(tree.getRoot());
		assertNotNull(tree2.getRoot());
		assertEquals(tree, tree2);
		
	}

}
