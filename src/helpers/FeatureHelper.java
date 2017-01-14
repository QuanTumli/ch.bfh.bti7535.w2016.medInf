package helpers;

import java.util.ArrayList;
import java.util.List;

import interfaces.Feature;
import models.FeatureizedTestSet;
import models.TokenizedTestSet;

public class FeatureHelper {

	List<Feature> features;

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

	public List<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}

}
