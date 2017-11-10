package coffeeTree;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class WeightedGiniMetricTest {
	
	private static Observation class0Obs;
	private static Observation class1Obs;
	private WeightedGiniMetric binaryGiniMetric = new WeightedGiniMetric(2);


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		class0Obs = new Observation(new String[]{"useless"}, "0");
		class1Obs = new Observation(new String[]{"useless"}, "1");
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCalculateScore() {
		String[] classes = {"0", "1"};
		Observation[][] worstCase = { {class0Obs, class1Obs}, {class0Obs, class1Obs} };
		Observation[][] bestCase = { {class0Obs, class0Obs}, {class1Obs, class1Obs} };
		Observation[][] weightedBestCase = { {class0Obs, class0Obs, class0Obs, class0Obs, class0Obs, class1Obs}, {class0Obs, class0Obs, class1Obs, class1Obs, class1Obs, class1Obs} };
		Observation[][] multiWaySplit = { {class0Obs, class1Obs, class1Obs, class1Obs}, {class0Obs, class0Obs, class0Obs, class0Obs, class0Obs, class0Obs, class0Obs, class0Obs}, {class0Obs, class1Obs, class1Obs, class1Obs, class1Obs, class1Obs, class1Obs, class1Obs} };
		
		assertEquals(0.0, binaryGiniMetric.calculateScore(bestCase, classes), 0.0001);
		assertEquals(0.5, binaryGiniMetric.calculateScore(worstCase, classes), 0.0001);
		assertEquals(0.361, binaryGiniMetric.calculateScore(weightedBestCase, classes), 0.1);
		assertEquals(0.1625, binaryGiniMetric.calculateScore(multiWaySplit, classes), 0.0001);
		
	}
	
}
