package helpers;

import java.util.ArrayList;
import java.util.List;

import interfaces.Feature;
import models.FeatureizedTestSet;
import models.TokenizedTestSet;

/**
 * The Class FeatureHelper.
 */
public class FeatureHelper {

	/** The features. */
	List<Feature> features;

	/**
	 * Featureize test sets.
	 *
	 * @param tokenizedTestSets the tokenized test sets
	 * @return the list
	 */
	public List<FeatureizedTestSet> featureizeTestSets(List<TokenizedTestSet> tokenizedTestSets) {
		List<FeatureizedTestSet> featureizedTestSets = new ArrayList<FeatureizedTestSet>();
		
		for (int i = 0; i < tokenizedTestSets.size(); i++) {
			FeatureizedTestSet featureizedTestSet = new FeatureizedTestSet(tokenizedTestSets.get(i).getName(), tokenizedTestSets.get(i).getTokenizer());
			if (features.size() == 0) {
				featureizedTestSet.setFeatureizedNegativeTestFiles(tokenizedTestSets.get(i).getTokenizedNegativeTestFiles());
				featureizedTestSet
						.setFeatureizedNegativeTrainingFiles(tokenizedTestSets.get(i).getTokenizedNegativeTrainingFiles());
				featureizedTestSet.setFeatureizedPositiveTestFiles(tokenizedTestSets.get(i).getTokenizedPositiveTestFiles());
				featureizedTestSet
						.setFeatureizedPositiveTrainingFiles(tokenizedTestSets.get(i).getTokenizedPositiveTrainingFiles());
			}
			for (Feature feature : features) {
				featureizedTestSet.getFeatures().add(feature.getClass());
				featureizedTestSet.setFeatureizedNegativeTestFiles(
						feature.applyFeature(tokenizedTestSets.get(i).getTokenizedNegativeTestFiles()));
				featureizedTestSet.setFeatureizedNegativeTrainingFiles(
						feature.applyFeature(tokenizedTestSets.get(i).getTokenizedNegativeTrainingFiles()));
				featureizedTestSet.setFeatureizedPositiveTestFiles(
						feature.applyFeature(tokenizedTestSets.get(i).getTokenizedPositiveTestFiles()));
				featureizedTestSet.setFeatureizedPositiveTrainingFiles(
						feature.applyFeature(tokenizedTestSets.get(i).getTokenizedPositiveTrainingFiles()));
			}
			featureizedTestSets.add(featureizedTestSet);
		}
		
		return featureizedTestSets;
	}

	/**
	 * Gets the features.
	 *
	 * @return the features
	 */
	public List<Feature> getFeatures() {
		return features;
	}

	/**
	 * Sets the features.
	 *
	 * @param features the new features
	 */
	public void setFeatures(List<Feature> features) {
		this.features = features;
	}

}
