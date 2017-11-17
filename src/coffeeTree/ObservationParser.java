package coffeeTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Parses formatted files that represent the attributes and classifications an Observation is made of
 * @author Jacob Casper
 *
 */
public class ObservationParser {
	
	public Observation parseString(String oString, boolean isClassified) {
		String[] values = oString.split("\\s*,\\s*");
		ArrayList<String> valueList = new ArrayList<String>(Arrays.asList(values));
//		ArrayList<Attribute> attributes = mapAttributes(valueList);
		Observation obs;
		if (isClassified) {
			String classification = valueList.remove(0);
			ArrayList<Attribute> attributes = mapAttributes(valueList);
			obs = new Observation(attributes, classification);
		} else {
			ArrayList<Attribute> attributes = mapAttributes(valueList);
			obs = new Observation(attributes);
		}
		return obs;
	}
	
	/**
	 * Holy one liners batman
	 * @param valueList
	 * @return
	 */
	private ArrayList<Attribute> mapAttributes(ArrayList<String> valueList) {
		return new ArrayList<Attribute>(valueList.stream()
				.map(value -> value.split(":"))
				.map(attributePair -> new Attribute(attributePair[0], (attributePair.length == 2) ? attributePair[1] : null))
				.collect(Collectors.toList()));
	}

	public ArrayList<Observation> parseFile(String fileName, boolean isClassified) throws IOException {
		
		ArrayList<Observation> observations = new ArrayList<Observation>();
		
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String oString = reader.readLine();
		while(oString != null) {
			observations.add(parseString(oString, isClassified));
			oString = reader.readLine();
		}
		
		return observations;
		
	}
	
}
