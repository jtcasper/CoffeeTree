package coffeeTree;

public class WeightedGiniMetric extends AbstractMetric {
	
	/**
	 * Implementation of superclass calculateScore that computes the weighted GINI impurity.
	 * @param groups 2D array of Observations of data
	 * @param classes The set of classes that data can be classified to.
	 * @return Weighted GINI impurity
	 */
	public double calculateScore(Observation[][] groups, String[] classes) {
		int totalSize = 0;
		double giniScore = 0;
		
		for(Observation[] group: groups) {
			totalSize += group.length;
		}
		
		for(Observation[] group: groups) {
			if (group.length == 0)
				continue;
			giniScore += calculateGini(group, classes) * (group.length / (double) totalSize);
		}
		
		
		return giniScore;
	}
	
	/**
	 * Calculates the GINI impurity of a single node
	 * @param group Observations contained by the node
	 * @param classes Classifications in the model
	 * @return Gini
	 */
	private double calculateGini(Observation[] group, String[] classes) {
		double giniScore = 1;
		
		int size = group.length;
		double classificationError = 0;
		for(String classification: classes) {
			double probability = 0;
			for(Observation o: group) {
				if(o.getClassification().equals(classification)) {
					probability += 1;
				}
			}
			probability /= size;
			classificationError += probability * probability;
		}
		
		giniScore -= classificationError;
				
		return giniScore;

	}

}
