package helpers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import interfaces.Feature;
import models.FeatureizedTestSet;
import models.TokenizedTestSet;

public class FeatureHelper {

	List<Feature> features;

	public List<FeatureizedTestSet> featureizeTestSets(List<TokenizedTestSet> listOfTestSets) {
		List<FeatureizedTestSet> featureizedTestSets = new ArrayList<FeatureizedTestSet>();
		for (Feature feature : features) {
			for (TokenizedTestSet tokenizedTestSet : listOfTestSets) {
				FeatureizedTestSet featureizedTestSet = new FeatureizedTestSet(tokenizedTestSet);
				featureizedTestSet.getFeatures().add(feature.getClass());
				featureizedTestSet.setFeatureizedNegativeTestFiles(feature.applyFeature(tokenizedTestSet.getTokenizedNegativeTestFiles()));
				featureizedTestSet.setFeatureizedNegativeTrainingFiles(feature.applyFeature(tokenizedTestSet.getTokenizedNegativeTrainingFiles()));
				featureizedTestSet.setFeatureizedPositiveTestFiles(feature.applyFeature(tokenizedTestSet.getTokenizedPositiveTestFiles()));
				featureizedTestSet.setFeatureizedPositiveTrainingFiles(feature.applyFeature(tokenizedTestSet.getTokenizedPositiveTrainingFiles()));		
				featureizedTestSets.add(featureizedTestSet);
			}
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
