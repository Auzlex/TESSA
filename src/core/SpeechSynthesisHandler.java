package core;

//import marytts.modules.synthesis.Voice;

public class SpeechSynthesisHandler {

	public static TextToSpeech ttsEngine;
	
	// called when we want to initialize the Speech Synthesis handler
	public static void Initialize()
	{
		System.out.println("Creating new TTS Engine.");
		
		// create a new voice
		ttsEngine = new TextToSpeech();
		
		System.out.println("Done!");
		
		// this will print out all available voices that we have added on the class path
		//Voice.getAvailableVoices().stream().forEach(System.out::println);
	}
	
	public static void Speak( String text, boolean await )
	{
		ttsEngine.speak(text, 1f, false, await);
	}
	
}
