package helpers;

import java.util.ArrayList;
import java.util.List;

import models.TestSet;

public class KFoldHelper {

	private int kFoldValue;

	public KFoldHelper(int kFoldValue) {
		setKFoldValue(kFoldValue);
	}

	public List<TestSet> kFoldFiles(String negativeReviewsPath, String positiveReviewsPath) {
		List<String> documentsPos = FileHelper.getFileNamesFromFolder(positiveReviewsPath);
		List<String> documentsNeg = FileHelper.getFileNamesFromFolder(negativeReviewsPath);
		int sizeOfKFoldPos = documentsPos.size() / this.kFoldValue;
		int sizeOfKFoldNeg = documentsNeg.size() / this.kFoldValue;

		List<TestSet> testSets = new ArrayList<TestSet>();

		for (int i = 0; i < kFoldValue; i++) {

			int startNegFile = i * sizeOfKFoldNeg;
			int endNegFile = sizeOfKFoldNeg + i * sizeOfKFoldNeg;
			int startPosFile = i * sizeOfKFoldPos;
			int endPosFile = sizeOfKFoldPos + i * sizeOfKFoldPos;

			// for testing
			// if(i==1) break;
			System.out.println("K-Fold " + (i + 1) + "/" + kFoldValue + " " + sizeOfKFoldNeg);

			// - pick 1-100 docs (pos&neg) as TEST
			List<String> testDocumentsNeg = documentsNeg.subList(startNegFile, endNegFile);
			List<String> testDocumentsPos = documentsPos.subList(startPosFile, endPosFile);
			
			// - pick 101-1000 docs (pos&neg) as TRAIN
			List<String> trainDocumentsNeg = new ArrayList<String>(documentsNeg);
			for (int j = i * sizeOfKFoldNeg; j < (sizeOfKFoldNeg + i * sizeOfKFoldNeg); j++) {
				trainDocumentsNeg.remove(documentsNeg.get(j));
			}
			List<String> trainDocumentsPos = new ArrayList<String>(documentsPos);
			for (int j = i * sizeOfKFoldPos; j < (sizeOfKFoldPos + i * sizeOfKFoldPos); j++) {
				trainDocumentsPos.remove(documentsPos.get(j));
			}
			TestSet newTestSet = new TestSet("Set_Neg_" + startNegFile + "-" + (endNegFile - 1) + "_Pos_" + startPosFile
					+ "-" + (endPosFile - 1));
			newTestSet.setNegativeTestFiles(testDocumentsNeg);
			newTestSet.setPositiveTestFiles(testDocumentsPos);
			newTestSet.setNegativeTrainingFiles(trainDocumentsNeg);
			newTestSet.setPositiveTrainingFiles(trainDocumentsPos);

			testSets.add(newTestSet);
		}
		return testSets;
	}

	public int getKFoldValue() {
		return kFoldValue;
	}

	public void setKFoldValue(int kFoldValue) {
		this.kFoldValue = kFoldValue;
	}

}
