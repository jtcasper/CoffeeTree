package coffeeTree;

import java.util.ArrayList;

public class CoffeeTree {
	
	private CoffeeTreeNode root;
	private String[] attributeList;
	//TODO allow user selection of metric
	private AbstractMetric metric = new WeightedGiniMetric();
	
	public CoffeeTree(ArrayList<Observation> observations) {
		
		this.root = new CoffeeTreeNode(observations);
		this.setAttributeList(generateAttributeList(observations));
		
	}
	
	//TODO maybe change this to be a method of CoffeeTreeNode?
	/**
	 * Trains the coffee tree model by splitting based on model's metric
	 */
	public void trainModel() {

		CoffeeTreeNode[] bestSplit = null;
		double bestScore = Float.MAX_VALUE;
		String bestAttribute = null;
		
		for(String attribute: this.getAttributeList()) {
			CoffeeTreeNode[] currentSplit = this.root.split(this.root, attribute);
			double currentScore = metric.calculateScore(new Observation[][] {currentSplit[0].getObservations(), currentSplit[1].getObservations()}, new String[] {"0", "1"});
			if (currentScore < bestScore) {
				bestAttribute = attribute;
				bestScore = currentScore;
				bestSplit = currentSplit;
			}
		}
		this.root.setAttribute(bestAttribute);
		this.root.setChildren(bestSplit);
		System.out.println(bestScore);
		
	}
	
	public CoffeeTreeNode getRoot() {
		return this.root;
	}
	
	private String[] generateAttributeList(ArrayList<Observation> observations) {
		ArrayList<String> attributeList = new ArrayList<String>();
		for (Observation o: observations) {
			for (String attribute: o.getAttributes()) {
				if (!attributeList.contains(attribute)) {
					attributeList.add(attribute);
				}
			}
		}
		String[] outAttributeList = new String[attributeList.size()];
		return attributeList.toArray(outAttributeList);
	}
	
	public void setAttributeList(String[] attributeList) {
		this.attributeList = attributeList;
	}
	
	public String[] getAttributeList() {
		return this.attributeList;
	}
	
	@Override
	public boolean equals(Object o) {
		boolean result = true;
		if (o instanceof CoffeeTree) {
			CoffeeTree other = (CoffeeTree) o;
			if (this.getRoot().equals(other.getRoot())) {
				String[] attributeList = this.getAttributeList();
				String[] otherAttributeList = other.getAttributeList();
				if (attributeList.length == otherAttributeList.length) {
					for (int i = 0; i < attributeList.length; i++) {
						if(!attributeList[i].equals(otherAttributeList[i])) {
							result = false;
							break;
						}
					}
				}
			} else {
				result = false;
			}
		}
		return result;
	}
	

	
	/**
	 * A node that makes up part of the CoffeeTree
	 * @author Jacob Casper
	 *
	 */
	private class CoffeeTreeNode {
		
		private ArrayList<Observation> observations;
		private CoffeeTreeNode[] children;
		private String attribute;
		
		public CoffeeTreeNode(ArrayList<Observation> observations, CoffeeTreeNode[] children, String attribute) {
			this.observations = observations;
			this.children = children;
			this.setAttribute(attribute);
		}
		
		public CoffeeTreeNode(ArrayList<Observation> observations) {
			this(observations, null, null);
		}
		
		public CoffeeTreeNode() {
			this(new ArrayList<Observation>());
		}
		
		/**
		 * Splits a node in to two nodes, those that have the attribute being selected and those that lack it
		 * @param root Tree node being split
		 * @param attribute Attribute to check for
		 * @return Array of child nodes containing and lacking the observations with those attributes
		 */
		private CoffeeTreeNode[] split(CoffeeTreeNode root, String attribute) {
			CoffeeTreeNode haveAttribute = new CoffeeTreeNode();
			CoffeeTreeNode lackAttribute = new CoffeeTreeNode();
			for(Observation o: root.getObservations()) {
				boolean contains = false;
				for(String a: o.getAttributes()) {
					if(a.equals(attribute)) {
						contains = true;
						break;
					}
				}
				if (contains) {
					haveAttribute.addObservation(o);
				} else {
					lackAttribute.addObservation(o);
				}
			}
			CoffeeTreeNode[] children = {haveAttribute, lackAttribute};
			return children;
			
		}
		

		public Observation[] getObservations() {
			Observation[] obsArray = new Observation[this.observations.size()];
			return this.observations.toArray(obsArray);
		}
		
		public void addObservation(Observation o) {
			this.observations.add(o);
		}
		
		public CoffeeTreeNode[] getChildren() {
			return this.children;
		}
		
		public void setChildren(CoffeeTreeNode[] children) {
			this.children = children;
		}
		
		public String getAttribute() {
			return attribute;
		}

		public void setAttribute(String attribute) {
			this.attribute = attribute;
		}

		@Override
		public boolean equals(Object o){
			boolean result = false;
			if (o instanceof CoffeeTreeNode) {
				boolean attributesEqual = false;
				boolean childrenEqual = false;
				boolean observationsEqual = true;
				CoffeeTreeNode other = (CoffeeTreeNode) o;
				String attribute = this.getAttribute();
				String otherAttribute = other.getAttribute();
				CoffeeTreeNode[] children = this.getChildren();
				CoffeeTreeNode[] otherChildren = other.getChildren();
				Observation[] observations = this.getObservations();
				Observation[] otherObservations = other.getObservations();
				if ( attribute != null && otherAttribute != null ) {
					if ( attribute.equals(otherAttribute) ) {
						attributesEqual = true;
					}
				} else if ( attribute == null && otherAttribute == null ) {
					attributesEqual = true;
				}
				if ( children == null && otherChildren == null ) {
					childrenEqual = true;
				} else if(children != null && otherChildren != null) {
					if (children.length == otherChildren.length && children[0].equals(otherChildren[0]) 
							&& children[1].equals(otherChildren[1])) {
						childrenEqual = true;
					}
				}
				if (observations.length == otherObservations.length) {
					for (int i = 0; i < observations.length; i++) {
						if(!observations[i].equals(otherObservations[i])) {
							observationsEqual = false;
							break;
						}
					}
				} else {
					observationsEqual = false;
				}
				result = attributesEqual && childrenEqual && observationsEqual;
			}
			return result;
			
		}
		
	}

}
