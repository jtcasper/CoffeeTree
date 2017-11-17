package coffeeTree;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AttributeTest {
	
	private static Attribute valuedAttribute;
	private static Attribute valuedAttribute2;
	private static Attribute unvaluedAttribute;
	private static Attribute unvaluedAttribute2;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		valuedAttribute = new Attribute("hasCountryOfOrigin", "Guatemala");
		valuedAttribute2 = new Attribute("hasCountryOfOrigin", "Indonesia");
		unvaluedAttribute = new Attribute("hasCountryOfOrigin");
		unvaluedAttribute2 = new Attribute("hasRoast");
		
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testSoftEquals() {
		assertTrue(valuedAttribute.softEquals(valuedAttribute2));
		assertTrue(valuedAttribute.softEquals(unvaluedAttribute));
		assertFalse(valuedAttribute.softEquals(unvaluedAttribute2));
		assertFalse(unvaluedAttribute.softEquals(unvaluedAttribute2));
	}

}
