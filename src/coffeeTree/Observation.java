package coffeeTree;

import java.util.ArrayList;

/**
 * An Observation is a Row of data from a dataset. It will have attributes
 * and potentially contain a Classification (for training)
 * @author Jacob Casper
 *
 */
public class Observation {
	
	private ArrayList<Attribute> attributes;
	private String classification;
	
	public Observation(ArrayList<Attribute> attributes, String classification) {
		this.attributes = attributes;
		this.classification = classification;
	}
	
	public Observation(ArrayList<Attribute> attributes) {
		this(attributes, null);
	}
	
	public ArrayList<Attribute> getAttributes() {
		return this.attributes;
	}
	
	public String getClassification() {
		return this.classification;
	}
	
	public void setClassification(String classification) {
		this.classification = classification;
	}
	
	@Override
	public boolean equals(Object o) {

		boolean result = false;
		if (o instanceof Observation) {
			
			Observation other = (Observation) o;
			if((this.getClassification() == null && other.getClassification() == null) || (this.getClassification() != null && this.getClassification().equals(other.getClassification()))) {
				//TODO This can be cleaned up heavily once Attribute class implemented
				if (this.getAttributes().equals(other.getAttributes())) {
					result = true;
				}
			}
			
		}
		return result;
		
	}

}
