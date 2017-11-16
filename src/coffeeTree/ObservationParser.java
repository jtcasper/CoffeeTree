package coffeeTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Parses formatted files that represent the attributes and classifications an Observation is made of
 * @author Jacob Casper
 *
 */
public class ObservationParser {
	
	public Observation parseString(String oString, boolean isClassified) {
		String[] values = oString.split("\\s*,\\s*");
		Observation obs;
		if (isClassified) {
			obs = new Observation(Arrays.copyOfRange(values, 1, values.length), values[0]);
		} else {
			obs = new Observation(values);
		}
		return obs;
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
