package coffeeTree;

public abstract class AbstractMetric {
	
	/**
	 * To be implemented in sub-classes. Measures the impurity of a node split in the decision tree.
	 * @return Impurity of a node split
	 */
	public abstract double calculateScore(Observation[][] groups, String[] classes);
	
}
