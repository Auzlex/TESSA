package core;

import java.net.URL;
import java.util.Random;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioHandler {

	// enum of type of audio files we want to play
	public enum AudioType
	{
		Listening,
		Working,
		Error
	}
	
	private static String prefix = "[AudioHandler]: ";

	// invoked when we want to play a audio file by type and if to wait for it to fully play or not
	// there could be more simplification done here but eh keep it simple
	public static void Play(AudioType type, boolean await)
	{

		URL path; // path to file
		
		switch(type)
		{
			case Error:
				
				//System.out.println(ClassLoader.getSystemResource("error.wav").getPath());
				path = ClassLoader.getSystemResource("audio/responses/error.wav");

				try
				{
					System.out.println(prefix + "playing sound type: " + type.toString());

					Clip audioClip = AudioSystem.getClip();
					audioClip.open(AudioSystem.getAudioInputStream( path ));
					audioClip.start();

					if(await)
						Thread.sleep(audioClip.getMicrosecondLength()/1000);

					System.out.println(prefix + "finished playing sound type: " + type.toString());
				}
				catch(Exception e)
				{
					System.out.println(prefix + "Failed to play sound " + type.toString());
				}

				break;
			case Listening:

				path = ClassLoader.getSystemResource("audio/responses/listening.wav");
				
				try
				{
					System.out.println(prefix + "playing sound type: " + type.toString());
					
					Clip audioClip = AudioSystem.getClip();
					audioClip.open(AudioSystem.getAudioInputStream( path ));
					audioClip.start();
					
					if(await)
						Thread.sleep(audioClip.getMicrosecondLength()/1000);
					
					System.out.println(prefix + "finished playing sound type: " + type.toString());
				}
				catch(Exception e)
				{
					System.out.println(prefix + "Failed to play sound " + type.toString());
				}
				
				break;
			case Working:
				
				Random random = new Random();
				
				int randomInt = (random.nextInt(3));
				
				//soundFile = new File("resource/audio/responses/work0" + randomInt + ".wav");
				
				path = ClassLoader.getSystemResource("audio/responses/work0" + randomInt + ".wav");
				
				try
				{
					System.out.println(prefix + "playing sound type: " + type.toString());
					
					Clip audioClip = AudioSystem.getClip();
					audioClip.open(AudioSystem.getAudioInputStream( path ));
					audioClip.start();
					
					if(await)
						Thread.sleep(audioClip.getMicrosecondLength()/1000);
					
					System.out.println(prefix + "finished playing sound type: " + type.toString());
				}
				catch(Exception e)
				{
					System.out.println(prefix + "Failed to play sound " + type.toString());
				}
				
				break;
			default:
				break;	
		}
	}
	
}
