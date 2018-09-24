package topicmodel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import util.FileUtil;


public class atmGibbsSampling {
	
	
	public static class modelparameters {
		float alpha = 0.1f; //usual value is 50 / K
		float beta = 0.01f;//usual value is 0.1
		float gamma = 0.5f;
		float[] eta = {1, 1};
		int aspectNum = 5;
		int topicNum = 5;
		int iteration = 120;
		int saveStep = 10;
		int beginSaveIters = 100;
		String respath = "model/alpha=" + alpha + "_beta=" + beta + "_gamma=" + gamma + "_K+" + topicNum + "/";
	}
	
	/**Get parameters from configuring file. If the 
	 * configuring file has value in it, use the value.
	 * Else the default value in program will be used
	 * @param ldaparameters
	 * @param parameterFile
	 * @return void
	 */
	private static void getParametersFromFile(modelparameters ldaparameters,
			String parameterFile) {
		// TODO Auto-generated method stub
		ArrayList<String> paramLines = new ArrayList<String>();
		FileUtil.readLines(parameterFile, paramLines);
		for(String line : paramLines){
			String[] lineParts = line.split("\t");
			switch(parameters.valueOf(lineParts[0])){
			case alpha:
				ldaparameters.alpha = Float.valueOf(lineParts[1]);
				break;
			case beta:
				ldaparameters.beta = Float.valueOf(lineParts[1]);
				break;
			case eta:
				ldaparameters.eta[0] = Float.valueOf(lineParts[1]);
				ldaparameters.eta[1] = Float.valueOf(lineParts[2]);
				break;
			case gamma:
				ldaparameters.gamma = Float.valueOf(lineParts[1]);
				break;
			case aspectNum:
				ldaparameters.aspectNum = Integer.valueOf(lineParts[1]);
				break;
			case topicNum:
				ldaparameters.topicNum = Integer.valueOf(lineParts[1]);
				break;
			case iteration:
				ldaparameters.iteration = Integer.valueOf(lineParts[1]);
				break;
			case saveStep:
				ldaparameters.saveStep = Integer.valueOf(lineParts[1]);
				break;
			case beginSaveIters:
				ldaparameters.beginSaveIters = Integer.valueOf(lineParts[1]);
				break;
			case respath:
				ldaparameters.respath = lineParts[1];
				break;
			}
		}
	}
	
	public enum parameters{
		alpha, beta, eta, gamma, topicNum, aspectNum, iteration, saveStep, beginSaveIters, respath;
	}
	
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		String originalDocsPath = PathConfig.ldaDocsPath;
		String resultPath = PathConfig.LdaResultsPath;
		//String parameterFile= ParameterConfig.LDAPARAMETERFILE;
		
		modelparameters ldaparameters = new modelparameters();
		//getParametersFromFile(ldaparameters, parameterFile);
		Documents docSet = new Documents();
		docSet.readDocs(originalDocsPath);
		docSet.outPutIndex(resultPath);
		System.out.println("wordMap size " + docSet.tword2id.size());
		FileUtil.mkdir(new File(resultPath));
		aspectTopicModel model = new aspectTopicModel(ldaparameters);
		System.out.println("1 Initialize the model ...");
		model.initializeModel(docSet);
		System.out.println("2 Learning and Saving the model ...");
		model.inferenceModel(docSet);
		System.out.println("3 Output the final model ...");
		model.saveIteratedModel(ldaparameters.iteration, docSet);
		System.out.println("Done!");
	}
}
