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
	
	@Override
	public boolean equals(Object o) {

		boolean result = false;
		if (o instanceof Observation) {
			
			Observation other = (Observation) o;
			if((this.getClassification() == null && other.getClassification() == null) || this.getClassification().equals(other.getClassification())) {
				//TODO This can be cleaned up heavily once Attribute class implemented
				if (this.getAttributes().length == other.getAttributes().length) {
					for (int i = 0; i < this.getAttributes().length; i++) {
						if (this.getAttributes()[i].equals(other.getAttributes()[i])) {
							result = true;
						} else {
							result = false;
						}
					}
				}
			}
			
		}
		return result;
		
	}

}
