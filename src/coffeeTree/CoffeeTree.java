package coffeeTree;

import java.util.ArrayList;

public class CoffeeTree {
	
	private CoffeeTreeNode root;
	private ArrayList<String> attributeList;
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
		this.root.train(this.getAttributeList());
	}
	
	public CoffeeTreeNode getRoot() {
		return this.root;
	}
	
	private ArrayList<String> generateAttributeList(ArrayList<Observation> observations) {
		ArrayList<String> attributeList = new ArrayList<String>();
		for (Observation o: observations) {
			for (String attribute: o.getAttributes()) {
				if (!attributeList.contains(attribute)) {
					attributeList.add(attribute);
				}
			}
		}
		return attributeList;
	}
	
	public void setAttributeList(ArrayList<String> attributeList) {
		this.attributeList = attributeList;
	}
	
	public ArrayList<String> getAttributeList() {
		return this.attributeList;
	}
	
	@Override
	public boolean equals(Object o) {
		boolean result = true;
		if (o instanceof CoffeeTree) {
			CoffeeTree other = (CoffeeTree) o;
			if (this.getRoot().equals(other.getRoot())) {
				ArrayList<String> attributeList = this.getAttributeList();
				ArrayList<String> otherAttributeList = other.getAttributeList();
//				if (attributeList.length == otherAttributeList.length) {
//					for (int i = 0; i < attributeList.length; i++) {
//						if(!attributeList[i].equals(otherAttributeList[i])) {
//							result = false;
//							break;
//						}
//					}
//				}
				if(!attributeList.equals(otherAttributeList)) {
					result = false;
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
		 * @param attribute Attribute to check for
		 * @return Array of child nodes containing and lacking the observations with those attributes
		 */
		private CoffeeTreeNode[] split(String attribute) {
			CoffeeTreeNode haveAttribute = new CoffeeTreeNode();
			CoffeeTreeNode lackAttribute = new CoffeeTreeNode();
			for(Observation o: this.getObservations()) {
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
		
		/**
		 * Recursively train and split the node
		 * @param attributeList
		 */
		private void train(ArrayList<String> attributeList) {

			CoffeeTreeNode[] bestSplit = null;
			double bestScore = Float.MAX_VALUE;
			String bestAttribute = null;
			
			// Recursion base cases
			// No attributes remaining to classify any further
			if (attributeList.size() == 0) {
				return;
			}
			// All Observations of same class
			ArrayList<String> classifications = new ArrayList<String>();
			for (Observation o: this.getObservations()) {
				if(!classifications.contains(o.getClassification())) {
					classifications.add(o.getClassification());
				}
				if (classifications.size() > 1) {
					break;
				}
			}
			if (classifications.size() == 1) {
				return;
			} else


			for(String attribute: attributeList) {
				// Avoid NPE in short attribute ArrayLists
				if(attribute == null) {
					break;
				}
				CoffeeTreeNode[] currentSplit = this.split(attribute);
				double currentScore = metric.calculateScore(new Observation[][] {currentSplit[0].getObservations(), currentSplit[1].getObservations()}, new String[] {"0", "1"});
				if (currentScore < bestScore) {
					bestAttribute = attribute;
					bestScore = currentScore;
					bestSplit = currentSplit;
				}
			}
			this.setAttribute(bestAttribute);
			attributeList.remove(bestAttribute);
			this.setChildren(bestSplit);
			this.getChildren()[0].train(attributeList);
			this.getChildren()[1].train(attributeList);
			System.out.println(bestScore);

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
