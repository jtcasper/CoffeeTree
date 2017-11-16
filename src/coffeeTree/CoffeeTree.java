package coffeeTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CoffeeTree {
	
	private CoffeeTreeNode root;
	private ArrayList<Attribute> attributeList;
	private static ArrayList<String> classificationList;
	private int maxDepth;
	private int minObservations;
	private double deltaGain;
	
	private final static int DEFAULT_MAX_DEPTH = Integer.MAX_VALUE;
	private final static int DEFAULT_MIN_OBSERVATIONS = 0;
	private final static double DEFAULT_DELTA_GAIN = 0.0;
	//TODO allow user selection of metric
	private AbstractMetric metric;
	
	public CoffeeTree(ArrayList<Observation> observations, AbstractMetric metric, int maxDepth, int minObservations, double deltaGain) {
		
		this.metric = metric;
		this.root = new CoffeeTreeNode(observations);
		this.setAttributeList(generateAttributeList(observations));
		this.setClassificationList(generateClassificationList(observations));
		this.setMaxDepth(maxDepth);
		this.setMinObservations(minObservations);
		this.setDeltaGain(deltaGain);
		
	}

	public CoffeeTree(ArrayList<Observation> observations, AbstractMetric metric) {
		
		this(observations, metric, DEFAULT_MAX_DEPTH, DEFAULT_MIN_OBSERVATIONS, DEFAULT_DELTA_GAIN);
		
	}
	
	//TODO maybe change this to be a method of CoffeeTreeNode?
	/**
	 * Trains the coffee tree model by splitting based on model's metric
	 */
	public void trainModel() {
		this.root.train(this.getAttributeList(), 1, this.getMaxDepth(), this.getMinObservations(), this.getDeltaGain());
	}
	
	/**
	 * Recursively descend through the tree using attributes until a terminal node is reached
	 * @param observation The Observation to be classified using a trained tree
	 */
	public void predictObservation(Observation observation) {
		this.root.predict(observation);
	}
	
	public CoffeeTreeNode getRoot() {
		return this.root;
	}
	
	private ArrayList<Attribute> generateAttributeList(ArrayList<Observation> observations) {
		ArrayList<Attribute> attributeList = new ArrayList<Attribute>();
		for (Observation o: observations) {
			for (Attribute attribute: o.getAttributes()) {
				if (!attributeList.contains(attribute)) {
					attributeList.add(attribute);
				}
			}
		}
		return attributeList;
	}
	
	public void setAttributeList(ArrayList<Attribute> attributeList) {
		this.attributeList = attributeList;
	}
	
	public ArrayList<Attribute> getAttributeList() {
		return this.attributeList;
	}
	
	private ArrayList<String> generateClassificationList(ArrayList<Observation> observations) {
		ArrayList<String> classificationList = new ArrayList<String>();
		for (Observation o: observations) {
			if (!classificationList.contains(o.getClassification())) {
				classificationList.add(o.getClassification());
			}
		}
		return classificationList;
	}
	
	
	private void setClassificationList(ArrayList<String> classificationList) {
		CoffeeTree.classificationList = classificationList;
	}
	
	public ArrayList<String> getClassificationList() {
		return classificationList;
	}

	public int getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	public int getMinObservations() {
		return minObservations;
	}

	public void setMinObservations(int minObservations) {
		this.minObservations = minObservations;
	}

	public double getDeltaGain() {
		return deltaGain;
	}

	public void setDeltaGain(double deltaGain) {
		this.deltaGain = deltaGain;
	}

	@Override
	public boolean equals(Object o) {
		boolean result = true;
		if (o instanceof CoffeeTree) {
			CoffeeTree other = (CoffeeTree) o;
			if (this.getRoot().equals(other.getRoot())) {
				ArrayList<Attribute> attributeList = this.getAttributeList();
				ArrayList<Attribute> otherAttributeList = other.getAttributeList();
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
		private Attribute attribute;
		private double impurity;
		
		public CoffeeTreeNode(ArrayList<Observation> observations, CoffeeTreeNode[] children, Attribute attribute, double impurity) {
			this.observations = observations;
			this.children = children;
			this.setAttribute(attribute);
			this.setImpurity(impurity);
		}
		
		public CoffeeTreeNode(ArrayList<Observation> observations) {
			this(observations, null, null, metric.MAX_IMPURITY);
		}
		
		public CoffeeTreeNode() {
			this(new ArrayList<Observation>());
		}
		
		/**
		 * Splits a node in to two nodes, those that have the attribute being selected and those that lack it
		 * @param attribute Attribute to check for
		 * @return Array of child nodes containing and lacking the observations with those attributes
		 */
		private CoffeeTreeNode[] split(Attribute attribute) {
			CoffeeTreeNode haveAttribute = new CoffeeTreeNode();
			CoffeeTreeNode lackAttribute = new CoffeeTreeNode();
			for(Observation o: this.getObservations()) {
				boolean contains = false;
				for(Attribute a: o.getAttributes()) {
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
		 * @param attributeList The list of all possible attributes available that this node can use to train itself
		 * @param depth The current depth of a branch
		 * @param maxDepth The maximum depth a branch can reach before being a terminal node
		 * @param minObservations The minimum number of Observations a node must have to continue splitting
		 * @param deltaGain The gain in impurity required to accept a split, used to reduce overfitting
		 * @return 
		 */
		private CoffeeTreeNode train(ArrayList<Attribute> attributeList, int depth, int maxDepth, int minObservations, double deltaGain) {

			CoffeeTreeNode[] bestSplit = null;
			double bestScore = Float.MAX_VALUE;
			Attribute bestAttribute = null;
			CoffeeTreeNode currentNode = this;
			
			// Recursion base cases
			// These base cases imply we have reached a terminal CoffeeTreeNode
			// No attributes remaining to classify any further
			if (attributeList.size() == 0) {
				return new TerminalCoffeeTreeNode(currentNode, attributeList);
			} else if (depth > maxDepth) {
				return new TerminalCoffeeTreeNode(currentNode, attributeList);
			} else if (currentNode.getObservations().length < minObservations) {
				return new TerminalCoffeeTreeNode(currentNode, attributeList);
			} else {
				// All Observations of same class
				ArrayList<String> classifications = new ArrayList<String>();
				for (Observation o: currentNode.getObservations()) {
					if(!classifications.contains(o.getClassification())) {
						classifications.add(o.getClassification());
					}
					if (classifications.size() > 1) {
						break;
					}
				}
				if (classifications.size() == 1) {
					return new TerminalCoffeeTreeNode(currentNode, attributeList);
				} 
			}


			for(Attribute attribute: attributeList) {
				// Avoid NPE in short attribute ArrayLists
				if(attribute == null) {
					break;
				}
				CoffeeTreeNode[] currentSplit = currentNode.split(attribute);
				double currentScore = metric.calculateScore(new Observation[][] {currentSplit[0].getObservations(), currentSplit[1].getObservations()}, new String[] {"0", "1"});
				if (currentScore < bestScore) {
					bestAttribute = attribute;
					bestScore = currentScore;
					bestSplit = currentSplit;
				}
			}
			// If there is not a sufficient improvement in impurity, stop splitting this node
			if ( Math.abs(currentNode.getImpurity() - bestScore) < deltaGain ) {
				return new TerminalCoffeeTreeNode(currentNode, attributeList);
			}
			currentNode.setAttribute(bestAttribute);
			attributeList.remove(bestAttribute);
			currentNode.setImpurity(bestScore);
			//Recursively train children
			bestSplit[0] = bestSplit[0].train(attributeList, depth + 1, maxDepth, minObservations, deltaGain);
			bestSplit[1] = bestSplit[1].train(attributeList, depth + 1, maxDepth, minObservations, deltaGain);
			currentNode.setChildren(bestSplit);
			System.out.println(bestScore);
			return currentNode;
			
		}
		
		/**
		 * Recursively descend through the tree using attributes until a terminal node is reached
		 * @param observation The Observation to be classified using a trained tree
		 */
		public void predict(Observation observation) {
			CoffeeTreeNode currentNode = this;
			// Base case: a leaf is reached
			if (this instanceof TerminalCoffeeTreeNode) {
				observation.setClassification(((TerminalCoffeeTreeNode) currentNode).getClassification());
			}
			else {
				Attribute attribute = currentNode.getAttribute();
				boolean contains = false;
				for(Attribute a: observation.getAttributes()) {
					if (attribute.equals(a)) {
						contains = true;
						break;
					}
				}
				if (contains) {
					// Left child is contains = true from split
					currentNode.getChildren()[0].predict(observation);
				} else {
					// Right child is contains = false from split
					currentNode.getChildren()[1].predict(observation);
				}
			}
			
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
		
		public Attribute getAttribute() {
			return attribute;
		}

		public void setAttribute(Attribute attribute) {
			this.attribute = attribute;
		}

		public double getImpurity() {
			return impurity;
		}

		public void setImpurity(double impurity) {
			this.impurity = impurity;
		}

		@Override
		public boolean equals(Object o){
			boolean result = false;
			if (o instanceof CoffeeTreeNode) {
				boolean attributesEqual = false;
				boolean childrenEqual = false;
				boolean observationsEqual = true;
				CoffeeTreeNode other = (CoffeeTreeNode) o;
				Attribute attribute = this.getAttribute();
				Attribute otherAttribute = other.getAttribute();
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
	
	private class TerminalCoffeeTreeNode extends CoffeeTreeNode {
		
		/**
		 * Classification that Observations who reach this node will be predicted to be
		 */
		private String classification;
		
		public TerminalCoffeeTreeNode(CoffeeTreeNode node, ArrayList<Attribute> attributeList) {
			super(new ArrayList<Observation>(Arrays.asList(node.getObservations())));
						
			String classification = null;
			int highestOccurence = 0;
			ArrayList<String> classificationList = new ArrayList<String>();
			for (Observation o: this.getObservations()) {
				classificationList.add(o.getClassification());
			}
			for (String c: classificationList) {
				int occurences = Collections.frequency(classificationList, c);
				if (occurences > highestOccurence) {
					highestOccurence = occurences;
					classification = c;
				}
			}
			this.setClassification(classification);
			
		}

		public String getClassification() {
			return classification;
		}

		public void setClassification(String classification) {
			this.classification = classification;
		}
		
	}

}
