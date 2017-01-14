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
		
		for (TokenizedTestSet tokenizedTestSet : tokenizedTestSets) {
			FeatureizedTestSet featureizedTestSet = new FeatureizedTestSet(tokenizedTestSet);
			if (features.size() == 0) {
				featureizedTestSet.setFeatureizedNegativeTestFiles(tokenizedTestSet.getTokenizedNegativeTestFiles());
				featureizedTestSet
						.setFeatureizedNegativeTrainingFiles(tokenizedTestSet.getTokenizedNegativeTrainingFiles());
				featureizedTestSet.setFeatureizedPositiveTestFiles(tokenizedTestSet.getTokenizedPositiveTestFiles());
				featureizedTestSet
						.setFeatureizedPositiveTrainingFiles(tokenizedTestSet.getTokenizedPositiveTrainingFiles());
			}
			for (Feature feature : features) {
				featureizedTestSet.getFeatures().add(feature.getClass());
				featureizedTestSet.setFeatureizedNegativeTestFiles(
						feature.applyFeature(tokenizedTestSet.getTokenizedNegativeTestFiles()));
				featureizedTestSet.setFeatureizedNegativeTrainingFiles(
						feature.applyFeature(tokenizedTestSet.getTokenizedNegativeTrainingFiles()));
				featureizedTestSet.setFeatureizedPositiveTestFiles(
						feature.applyFeature(tokenizedTestSet.getTokenizedPositiveTestFiles()));
				featureizedTestSet.setFeatureizedPositiveTrainingFiles(
						feature.applyFeature(tokenizedTestSet.getTokenizedPositiveTrainingFiles()));
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
