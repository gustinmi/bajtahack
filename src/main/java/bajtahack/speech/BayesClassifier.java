package bajtahack.speech;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import bajtahack.common.Global;
import de.daslaboratorium.machinelearning.classifier.Classifier;

public class BayesClassifier {
	//private static Classifier<String, String> bayesClassifier;	
	
	public BayesClassifier(){
		//this.bayesClassifier = Global.bayesClassifier;
	}
	
	public static void Train(Map<String, String> sentences){
		for (Map.Entry<String, String> sentence : sentences.entrySet()){
			Global.bayesClassifier.learn(sentence.getKey(), Arrays.asList(sentence.getValue().split("\\s")));
		}
	}
	
	public static String Classify(Map.Entry<String, String> sentence){
		List<String> sentenceAsList = Arrays.asList(sentence.getValue().split("\\s"));
		String result = "";
		result = Global.bayesClassifier.classify(sentenceAsList).getCategory();
		return result;
	}
	
}
