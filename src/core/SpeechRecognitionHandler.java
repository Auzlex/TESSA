package core;

import java.awt.*;
import java.io.IOException;
import java.net.*;

//import CMUSphinx4 core
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

//import edu.cmu.sphinx.linguist.language.grammar.Grammar;
//import edu.cmu.sphinx.jsgf.JSGFGrammar;
//import edu.cmu.sphinx.jsgf.JSGFGrammarException;
//import edu.cmu.sphinx.jsgf.JSGFGrammarParseException;


public class SpeechRecognitionHandler {

	//public static JSGFGrammar jsgfGrammar;
	//public static BaseRecognizer jsapiRecognizer;
	
	public static void Initialize() throws IOException
	{
		Configuration configuration = new Configuration();
		
		URL path = ClassLoader.getSystemResource("");
		
		System.out.println(path);
		
		/*URL acousticModel = ClassLoader.getSystemResource("resource:edu/cmu/sphinx/models/en-us/en-us");
		System.out.println(acousticModel);
		
		URL DictionaryPath = ClassLoader.getSystemResource("resource:edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
		System.out.println(DictionaryPath);
		
		
		URL LanguageModel = ClassLoader.getSystemResource("resource:edu/cmu/sphinx/models/en-us/en-us.lm.bin");
		System.out.println(LanguageModel);*/

		// loads in the acoustic model, dictionary and language path within this resources folder of this project
		configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
		configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
		configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
		configuration.setSampleRate(16000); // set the sample rate
		
		// failed attempt to make dynamic grammar
		//jsgfGrammar.loadJSGF("music");
		//RuleGrammar ruleGrammar = new BaseRuleGrammar(jsapiRecognizer, jsgfGrammar.getRuleGrammar());
		//addRule(ruleGrammar, "song1", "listen to over the rainbow");
		//addRule(ruleGrammar, "song2", "listen to as time goes by");
		//addRule(ruleGrammar, "song3", "listen to singing in the rain");

		// setup CMU to use grammar and point it to the grammar file
		configuration.setUseGrammar( true );
		configuration.setGrammarName( "tessa" );
		configuration.setGrammarPath( "resource:/grammars/" );

		// create a live speech recognizer
		LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(configuration);

		// Start recognition process pruning previously cached data.
		recognizer.startRecognition(true);

		// do audio and not await for it
		AudioHandler.Play(AudioHandler.AudioType.Working, false);

		// speak and await
		SpeechSynthesisHandler.Speak( "Voice assistant listening, hot word is Tessa. to terminate voice assistant. say Tessa, stop listening.", true );
		AudioHandler.Play(AudioHandler.AudioType.Listening, true);

		// reference to speech result class
		SpeechResult result;

		// while the speech result is not null then
		while ((result = recognizer.getResult()) != null) 
		{
			// print its hypothesis
			System.out.format("Hypothesis: %s\n", result.getHypothesis());
			//System.out.println("NBest: " + result.getNbest(5));
			//System.out.println("Lattice: " + result.getLattice());
			//System.out.println("Words Array: " + result.getWords());

			// pass my hypothesis
			HandleHypothesis(result.getHypothesis());
		}
		
		// Pause recognition process. It can be resumed then with startRecognition(false).
		recognizer.stopRecognition();
	}

	// this will pass in a string seperated by char | if the heystack given
	// is found then return true else nothing found return false
	// this will be used to help group words to same actions
	public static boolean FindMatchFromArray( String splitData, String heystack )
	{
		String[] data = splitData.split("\\|", -2);
		
		for(int i = 0;i < data.length;i++)
		{
			if(heystack.indexOf(data[i]) >= 0)
			{
				return true;
			}
		}
		
		return false;
	}

	// upon given hypothesis text
	public static void HandleHypothesis( String resultText )
	{
		
		// if we are busy stop listening or if the result is unk
		if(Core.currentState == Core.Status.Busy || resultText.equals("<unk>"))
			return;

		// lower the result, yeah I could of used compare ignore case but meh
		String resultTextLower = resultText.toLowerCase();

		// check for hotword and if we are ready
		if(resultTextLower.indexOf("tessa") >= 0 && Core.currentState == Core.Status.Ready || (resultTextLower.indexOf("ok") >= 0 && resultTextLower.indexOf("tessa") >= 0 || resultTextLower.indexOf("hey") >= 0 && resultTextLower.indexOf("tessa") >= 0 || resultTextLower.indexOf("okay") >= 0 && resultTextLower.indexOf("tessa") >= 0) && Core.currentState == Core.Status.Ready)
		{
			// set to busy
			Core.currentState = Core.Status.Busy;
			
			// play the audio file
			AudioHandler.Play(AudioHandler.AudioType.Listening, true);
			
			// say we are listening
			System.out.println("Listening!");
			
			// set our status to listening
			Core.currentState = Core.Status.Listening;
		}

		// check for input data
		if(FindMatchFromArray("stop listening", resultTextLower ) && Core.currentState == Core.Status.Listening )
		{
			// set our status to busy handling request
			Core.currentState = Core.Status.Busy;
			
			// play the audio file
			AudioHandler.Play(AudioHandler.AudioType.Working, false);
			
			// speak
			SpeechSynthesisHandler.Speak("Terminating voice interface.", true);

			// say we are listening
			System.out.println("Terminating voice interface!");
			
			// exit application
			System.exit(0);
		}		
		else if(FindMatchFromArray("cancel inquiry|cancel input|never mind|stop input|dismiss input|dismiss inquiry|never|mind", resultTextLower ) && Core.currentState == Core.Status.Listening)
		{
			// set our status to busy handling request
			Core.currentState = Core.Status.Busy;
			
			// play the audio file
			AudioHandler.Play(AudioHandler.AudioType.Working, false);
			
			// speak
			SpeechSynthesisHandler.Speak("Inquiry dismissed...", true);
			
			// say we are listening
			System.out.println("Inquiry dismissed!");
			
			// set our status to ready
			Core.currentState = Core.Status.Ready;
		}
		else if(FindMatchFromArray("turn on the computer|turn on computer", resultTextLower ) && Core.currentState == Core.Status.Listening)
		{
			// set our status to busy handling request
			Core.currentState = Core.Status.Busy;
			
			// play the audio file
			AudioHandler.Play(AudioHandler.AudioType.Working, false);
			
			// speak
			SpeechSynthesisHandler.Speak("Working...", true);

			// this was a bogus attempt to make a computer on my network boot via magic packet
			// pretty sure it does not work but might fix later..
			try {
				String host = "192.168.1.80";
				int port = 0;
				
				byte[] message = "34:97:f6:b6:cb:37".getBytes();
				
				// Get the internet address of the specified host
				InetAddress address = InetAddress.getByName(host);
				
				// Initialize a datagram packet with data and address
				DatagramPacket packet = new DatagramPacket(message, message.length,address, port);
				  
				// Create a datagram socket, send the packet through it, close it.
				DatagramSocket dsocket = new DatagramSocket();
				dsocket.send(packet);
				dsocket.close();
				
				// play the audio file
				//AudioHandler.Play(AudioHandler.AudioType.Working, false);
				SpeechSynthesisHandler.Speak("Magic packet sent.", true);
			}
			catch(Exception e)
			{
				// play the audio file
				AudioHandler.Play(AudioHandler.AudioType.Error, false);
				SpeechSynthesisHandler.Speak("Unable to comply packet not sent: " + e.getMessage().toString(), true);
			}

			// say we are listening
			System.out.println("Inquiry dismissed!");
			
			AudioHandler.Play(AudioHandler.AudioType.Working, true);
			
			// set our status to ready
			Core.currentState = Core.Status.Ready;
		}
		else if(FindMatchFromArray("open a web browser|open the web browser", resultTextLower ) && Core.currentState == Core.Status.Listening)
		{
			// set our status to busy handling request
			Core.currentState = Core.Status.Busy;

			// play the audio file
			AudioHandler.Play(AudioHandler.AudioType.Working, false);

			// speak
			SpeechSynthesisHandler.Speak("Working...", true);

			// open a web browser
			Desktop desktop = Desktop.getDesktop();
			try {
				//specify the protocol along with the URL
				URI oURL = new URI(
						"https://www.google.com");
				desktop.browse(oURL);

				SpeechSynthesisHandler.Speak("I have opened your default web browser.", true);
			} catch (URISyntaxException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				// play the audio file
				AudioHandler.Play(AudioHandler.AudioType.Error, false);

				// speak
				SpeechSynthesisHandler.Speak("Unable to comply, this device does not have a desktop!", true);
			}


			// set our status to ready
			Core.currentState = Core.Status.Ready;
		}
		else
		{
			// check if we are still listening 
			if(Core.currentState == Core.Status.Listening && !FindMatchFromArray("tessa", resultTextLower ))
			{
				// set our status to busy handling request
				Core.currentState = Core.Status.Busy;
				
				// play the audio file
				AudioHandler.Play(AudioHandler.AudioType.Error, false);
				
				// speak
				SpeechSynthesisHandler.Speak("Command unclear. Please repeat.", true);

				// say we are listening
				System.out.println("Please Repeat!");
				
				// set our status to ready
				Core.currentState = Core.Status.Listening;
			}	
		}
		
	}
	
}
