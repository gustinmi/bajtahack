package bajtahack;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.cloud.speech.v1.*;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.protobuf.ByteString;

/**
 * @author <a href="mailto:joze.baligac@gmail.com">Jože Baligač</a>
 *
 */
public class SpeechTester {

	@Test
	public void SpeechTest(){
		
		try{
			GoogleCredential credential = GoogleCredential.getApplicationDefault();
			
			SpeechClient speech = SpeechClient.create();
	
			String dirName = "222";
			
			File f = new File("c:\\sounds\\" + dirName);
			ArrayList<String> names = new ArrayList<String>(Arrays.asList(f.list()));
			List<String[]> fileNames = new ArrayList<String[]>(); 
			
			for (String s : names) {
				fileNames.add(new String[]{dirName, s});
			}
			
			// Builds the sync recognize request
		    RecognitionConfig config = RecognitionConfig.newBuilder()
		        .setEncoding(AudioEncoding.FLAC)
		        .setSampleRateHertz(48000)
		        .setLanguageCode("en-US")
		        .build();
			
		    Path path = null;
		    byte[] data = null;
		    ByteString audioBytes = null;
		    
			for (String[] fileName : fileNames) {
				path = Paths.get(f.getAbsolutePath()+ "\\" + fileName[1]);
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
				    System.out.printf("trainSet.add(new String[] {\"%s\", \"%s\"});%n", fileName[0], alternative.getTranscript());
				    //System.out.println(BayesClassifierBajta.Classify(fileName));
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
