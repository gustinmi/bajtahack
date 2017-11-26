package bajtahack;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import bajtahack.speech.BayesClassifier;

import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

public class SpeechTester {

	@Test
	public void SpeechTest(){
		//train
		Map<String, String> trainSet = new HashMap<String, String>();
		trainSet.put("140", "Tron light for off key");
		trainSet.put("130", "floor one light three off key");
		trainSet.put("141", "floor 1 light for fun key");
		trainSet.put("240", "floor to lights for off key");
		trainSet.put("120", "floor one light to off key");
		trainSet.put("131", "floor 1 Flight 3 on key");
		trainSet.put("230", "for 2/3 of key");
		trainSet.put("241", "do for fun key");
		trainSet.put("110", "Florida law on light one-off key");
		trainSet.put("121", "for one light to on key");
		trainSet.put("220", "42 Lac Du off key");
		trainSet.put("231", "42 light 3 on key");
		trainSet.put("000", "all lights off key");
		trainSet.put("111", "all lights on key");
		trainSet.put("210", "for two lights one of key");
		trainSet.put("221", "floor to lie to on key");
		trainSet.put("211", "42 black one on key");
		
		BayesClassifier.Train(trainSet);
		
		try{
			GoogleCredential credential = GoogleCredential.getApplicationDefault();
			
			SpeechClient speech = SpeechClient.create();
	
			Map<String, String> fileNames = new HashMap<String, String>();
			fileNames.put("---", "test.flac");
			
			/*fileNames.put("110", "F1L1OFF.flac");
			fileNames.put("111", "F1L1ON.flac");
			fileNames.put("120", "F1L2OFF.flac");
			fileNames.put("121", "F1L2ON.flac");
			fileNames.put("130", "F1L3OFF.flac");
			fileNames.put("131", "F1L3ON.flac");
			fileNames.put("140", "F1L4OFF.flac");
			fileNames.put("141", "F1L4ON.flac");
			fileNames.put("210", "F2L1OFF.flac");
			fileNames.put("211", "F2L1ON.flac");
			fileNames.put("220", "F2L2OFF.flac");
			fileNames.put("221", "F2L2ON.flac");
			fileNames.put("230", "F2L3OFF.flac");
			fileNames.put("231", "F2L3ON.flac");
			fileNames.put("240", "F2L4OFF.flac");
			fileNames.put("241", "F2L4ON.flac");
			fileNames.put("000", "ALLLIGHTSOFF.flac");
			fileNames.put("111", "ALLLIGHTSON.flac");*/
			
			// Builds the sync recognize request
		    RecognitionConfig config = RecognitionConfig.newBuilder()
		        .setEncoding(AudioEncoding.FLAC)
		        .setSampleRateHertz(48000)
		        .setLanguageCode("en-US")
		        .build();
			
		    Path path = null;
		    byte[] data = null;
		    ByteString audioBytes = null;
		    
			for (Map.Entry<String, String> fileName : fileNames.entrySet()) {
				path = Paths.get("c:\\sounds\\" + fileName.getValue());
				data = Files.readAllBytes(path);
				audioBytes = ByteString.copyFrom(data);
				
				RecognitionAudio audio = RecognitionAudio.newBuilder()
				        .setContent(audioBytes)
				        .build();
				
			    RecognizeResponse response = speech.recognize(config, audio);
			    List<SpeechRecognitionResult> results = response.getResultsList();
			    
			    for (SpeechRecognitionResult result: results) {
			    	//lahko je več alternativ, vzamemo prvo
				    SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
				    System.out.printf("Google speech recognition: \"%s\" key: %s%n", alternative.getTranscript(), fileName.getKey());
				    
				    System.out.println(BayesClassifier.Classify(fileName));
				    break;
				}
			    
			}
			
		    speech.close();
		    
		    
		    
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}
