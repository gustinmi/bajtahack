package bajtahack.speech;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import bajtahack.main.LoggingFactory;
import com.google.cloud.speech.v1.*;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.protobuf.ByteString;

public class SpeechRecognition {
    
    public static final Logger log = LoggingFactory.loggerForThisClass();
	
	public SpeechRecognition(){
		
	}
	
	public static String Recognize2(String path){
		String fResult = "";
		
		try{
			SpeechClient speech = SpeechClient.create();
	
			// Builds the sync recognize request
		    RecognitionConfig config = RecognitionConfig.newBuilder()
		        .setEncoding(AudioEncoding.FLAC)
		        .setSampleRateHertz(48000)
		        .setLanguageCode("en-US")
		        .build();
			
		    byte[] data = null;
		    ByteString audioBytes = null;
		    
		    Path myPath;
		    myPath = Paths.get(path);
		    
			data = Files.readAllBytes(myPath);
			audioBytes = ByteString.copyFrom(data);
			
			RecognitionAudio audio = RecognitionAudio.newBuilder()
			        .setContent(audioBytes)
			        .build();
			
		    RecognizeResponse response = speech.recognize(config, audio);
		    List<SpeechRecognitionResult> results = response.getResultsList();
		    
		    for (SpeechRecognitionResult result: results) {
		    	//lahko je več alternativ, vzamemo prvo
			    SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
			    
			    fResult = alternative.getTranscript();
			    
			    System.out.printf("Google speech recognition: \"%s\"%n", alternative.getTranscript());
			    
			    //System.out.println(BayesClassifierBajta.Classify(fileName));
			    break;
			}
			    
		    speech.close();
		    
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
		}

		return fResult;
	}
	
	@Deprecated
	public static String Recognize(byte[] data){
		
		try{
			//GoogleCredential credential = GoogleCredential.getApplicationDefault();
			
			SpeechClient speech = SpeechClient.create();
	
			// Builds the sync recognize request
		    RecognitionConfig config = RecognitionConfig.newBuilder()
		        .setEncoding(AudioEncoding.FLAC)
		        .setSampleRateHertz(48000)
		        .setLanguageCode("en-US")
		        .build();
			
		    ByteString audioBytes = null;

			audioBytes = ByteString.copyFrom(data);
			
			RecognitionAudio audio = RecognitionAudio.newBuilder()
			        .setContent(audioBytes)
			        .build();
			
		    RecognizeResponse response = speech.recognize(config, audio);
		    List<SpeechRecognitionResult> results = response.getResultsList();
		    
		    for (SpeechRecognitionResult result: results) {
		    	//lahko je več alternativ, vzamemo prvo
			    SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
			    System.out.printf("Google speech recognition: \"%s\"", alternative.getTranscript());
			    
			    break;
			}
			    	
		    speech.close();    
		    
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
		}

		return "";
	}
}
