package coffeeTree;

/**
 * An Observation is a Row of data from a dataset. It will have attributes
 * and potentially contain a Classification (for training)
 * @author Jacob Casper
 *
 */
public class Observation {
	
	private String[] attributes;
	private String classification;
	
	public Observation(String[] attributes, String classification) {
		this.attributes = attributes;
		this.classification = classification;
	}
	
	public Observation(String[] attributes) {
		this(attributes, null);
	}
	
	public String[] getAttributes() {
		return this.attributes;
	}
	
	public String getClassification() {
		return this.classification;
	}
	
	public void setClassification(String classification) {
		this.classification = classification;
	}

}
