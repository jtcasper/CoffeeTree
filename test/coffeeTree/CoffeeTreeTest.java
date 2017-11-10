package coffeeTree;

import static org.junit.Assert.*;

import java.util.ArrayList;

import javax.swing.plaf.synth.SynthSeparatorUI;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CoffeeTreeTest {
	
	private static Observation class0Obs;
	private static Observation class1Obs;
	private static Observation class2Obs;
	private ArrayList<Observation> binaryClassObservations;
	private ArrayList<Observation> tertiaryClassObservations;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		class0Obs = new Observation(new String[]{"useless"}, "0");
		class1Obs = new Observation(new String[]{"less useless"}, "1");
		class2Obs = new Observation(new String[]{"least useless"}, "2");
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
		tertiaryClassObservations = new ArrayList<Observation>();
		for(int i = 0; i < 7; i++) {
			tertiaryClassObservations.add(class0Obs);
		}
		for(int i = 0; i < 5; i++) {
			tertiaryClassObservations.add(class1Obs);
		}
		for(int i = 0; i < 3; i++) {
			tertiaryClassObservations.add(class2Obs);
		}

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCoffeeTree() {
		CoffeeTree tree = new CoffeeTree(binaryClassObservations);
		assertNotNull(tree);
	}

	@Test
	public void testTrainModel() {
		CoffeeTree tree = new CoffeeTree(binaryClassObservations);
		CoffeeTree tree2 = new CoffeeTree(binaryClassObservations);
		CoffeeTree tree3d = new CoffeeTree(tertiaryClassObservations);
		
		assertTrue(tree.equals(tree2));
		assertFalse(tree.equals(tree3d));
		
		tree.trainModel();
		assertFalse(tree.equals(tree2));
		assertFalse(tree.equals(tree3d));
		tree2.trainModel();
		assertTrue(tree.equals(tree2));
		tree3d.trainModel();
		
		System.out.println(tree.getRoot());
	}

	@Test
	public void testGetRoot() {
		CoffeeTree tree = new CoffeeTree(binaryClassObservations);
		CoffeeTree tree2 = new CoffeeTree(binaryClassObservations);
		assertNotNull(tree.getRoot());
		assertNotNull(tree2.getRoot());
		assertTrue(tree.equals(tree2));
		
	}

}
