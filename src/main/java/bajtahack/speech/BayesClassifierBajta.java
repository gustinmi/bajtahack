package bajtahack.speech;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import de.daslaboratorium.machinelearning.classifier.Classifier;
import de.daslaboratorium.machinelearning.classifier.bayes.BayesClassifier;

public class BayesClassifierBajta {
    
    public static final Classifier<String, String> bayesClassifier = new BayesClassifier<String, String>();
	
	public static void train(){
		//train
        List<String[]> trainSet = new ArrayList<String[]>(); 
        
        trainSet.add(new String[] {"110", "for one plus one"});
        trainSet.add(new String[] {"110", "41 + 1"});
        trainSet.add(new String[] {"110", "book one plus one"});
        trainSet.add(new String[] {"110", "short one like one up"});
        trainSet.add(new String[] {"110", "for one night one off"});
        
        trainSet.add(new String[] {"111", "Florida law on light one on"});
        trainSet.add(new String[] {"111", "Florida water light one on"});
        trainSet.add(new String[] {"111", "for one plus one"});
        trainSet.add(new String[] {"111", "for one light one on"});
        trainSet.add(new String[] {"111", "so the OnePlus One on"});
        
        trainSet.add(new String[] {"121", "4102"});
        trainSet.add(new String[] {"121", "go to one room to"});
        trainSet.add(new String[] {"121", "call 812"});
        trainSet.add(new String[] {"121", "Florida one room to park"});
        trainSet.add(new String[] {"121", "call 212"});
        	
        trainSet.add(new String[] {"120", "41 Room 2"});
        trainSet.add(new String[] {"120", "one room to up"});
        trainSet.add(new String[] {"120", "add one room to up"});
        trainSet.add(new String[] {"120", "Sword walkthrough"});
        trainSet.add(new String[] {"120", "Florida one room 2"});
        
        trainSet.add(new String[] {"130", "go to 103"});
        trainSet.add(new String[] {"130", "go to 1 2 3"});
        trainSet.add(new String[] {"130", "floor one room suite of"});
        trainSet.add(new String[] {"130", "413"});
        trainSet.add(new String[] {"130", "for one room free app"});
        
        trainSet.add(new String[] {"131", "41 Broome Street"});
        trainSet.add(new String[] {"131", "call 213"});
        trainSet.add(new String[] {"131", "413"});
        trainSet.add(new String[] {"131", "413"});
        trainSet.add(new String[] {"131", "go to one room 3 on"});
        
        trainSet.add(new String[] {"000", "all lights off"});
        trainSet.add(new String[] {"000", "lights off"});
        
        trainSet.add(new String[] {"222", "all lights on"});
        trainSet.add(new String[] { "000", "all lights off key"});
        trainSet.add(new String[] { "222", "all lights on key"});

        BayesClassifierBajta.bayesClassifier.setMemoryCapacity(500);
        
		for (String[] sentence : trainSet){
		    BayesClassifierBajta.bayesClassifier.learn(sentence[0], Arrays.asList(sentence[1].split("\\s")));
		}
		
		
	}
	
	public static String classify(String[] sentence){
		List<String> sentenceAsList = Arrays.asList(sentence);
		String result = "";
		
		result = BayesClassifierBajta.bayesClassifier.classify(sentenceAsList).getCategory();
		return result;
	}
	
}
