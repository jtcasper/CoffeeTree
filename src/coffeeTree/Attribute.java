package coffeeTree;

public class Attribute {
	
	private String attributeName;
	private String attributeValue;
	
	public Attribute(String attributeName, String attributeValue) {
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
	}
	
	public Attribute(String attributeName) {
		this(attributeName, null);
	}
	
	public String getAttributeName() {
		return attributeName;
	}
	
	public String getAttributeValue() {
		return attributeValue;
	}
	
	/**
	 * Tests if two Attributes share the same name, but differ on value
	 * @param o
	 * @return
	 */
	public boolean softEquals(Object o) {
		
		boolean result = false;
		
		if (o instanceof Attribute) {
			Attribute other = (Attribute) o;
			String attributeName = this.getAttributeName();
			String otherAttributeName = other.getAttributeName();
			if(attributeName == null && otherAttributeName == null) {
				result = true;
			} else {
				if (attributeName.equals(otherAttributeName)) {
					result = true;
				}
			}
		}
			
		return result;
		
	}
	
	@Override
	public boolean equals(Object o) {
		
		boolean result = false;
		
		if (o instanceof Attribute) {
			Attribute other = (Attribute) o;
			String attributeName = this.getAttributeName();
			String otherAttributeName = other.getAttributeName();
			if(attributeName == null && otherAttributeName == null) {
				result = true;
			} else {
				if (attributeName.equals(otherAttributeName)) {
					String attributeValue = this.getAttributeValue();
					String otherAttributeValue = other.getAttributeValue();
					if (attributeValue == null && otherAttributeValue == null) {
						result = true;
					} else {
						if (attributeValue.equals(other.attributeValue)) {
							result = true;
						}
					}
				}
			}
		}
		
		return result;
		
	}
	
	
	
}
