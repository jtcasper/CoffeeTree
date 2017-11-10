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
	private ArrayList<Observation> rootObservations;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		class0Obs = new Observation(new String[]{"useless"}, "0");
		class1Obs = new Observation(new String[]{"useless"}, "1");
	}

	@Before
	public void setUp() throws Exception {
		rootObservations = new ArrayList<Observation>();
		for(int i = 0; i < 7; i++) {
			rootObservations.add(class0Obs);
		}
		for(int i = 0; i < 5; i++) {
			rootObservations.add(class1Obs);
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCoffeeTree() {
		CoffeeTree tree = new CoffeeTree(rootObservations);
		assertNotNull(tree);
	}

	@Test
	public void testTrainModel() {
		CoffeeTree tree = new CoffeeTree(rootObservations);
		CoffeeTree tree2 = new CoffeeTree(rootObservations);
		
		tree.trainModel();
		assertFalse(tree.equals(tree2));
		tree2.trainModel();
		assertTrue(tree.equals(tree2));
		
		System.out.println(tree.getRoot());
	}

	@Test
	public void testGetRoot() {
		CoffeeTree tree = new CoffeeTree(rootObservations);
		CoffeeTree tree2 = new CoffeeTree(rootObservations);
		assertNotNull(tree.getRoot());
		assertNotNull(tree2.getRoot());
		assertTrue(tree.equals(tree2));
		
	}

}
