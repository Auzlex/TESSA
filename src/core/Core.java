package core;

import java.io.IOException;

public class Core {

	public enum Status
	{
		Ready,
		Listening,
		Busy,
	}
	
	// public static state variable to determine in other scripts of what our voice assistant is doing
	public static Status currentState = Status.Ready;
	
	// our main initialisation method that will load our modules and other classes
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		// Audio testing
		//AudioHandler.Play(AudioHandler.AudioType.Listening, true);		
		//AudioHandler.Play(AudioHandler.AudioType.Working, true);
		//AudioHandler.Play(AudioHandler.AudioType.Working, true);
		//AudioHandler.Play(AudioHandler.AudioType.Working, true);
		AudioHandler.Play(AudioHandler.AudioType.Error, true);
		
		// Initialize the Speech Synthesis Handler
		SpeechSynthesisHandler.Initialize();
		
		// Initialise the Speech Recognition Engine
		SpeechRecognitionHandler.Initialize();
		
	}

}
