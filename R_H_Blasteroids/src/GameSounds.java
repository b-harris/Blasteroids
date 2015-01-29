/** Title: Blasteroids
 * 	File: GameSounds.java
 * 	Coder(s): Rebecca Harris, Julia Sloan, Werner Uetz
 * 	Date: August 16, 2013
 * 	Description: Handles the sounds used in the game (nuke, bomb destruction, laser)
 */

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.sound.sampled.*;


public class GameSounds
{
	// variable declaration - set what file is associated with the sound
	private static File laserF = new File("../sounds/laser3.wav");
	private static Clip laserS;
	
	private static File explosionF = new File("../sounds/explosion1.wav");
	private static Clip explosionS;
	
	private static File nukeF = new File("../sounds/nuke.wav");
	private static Clip nukeS;
	
	// play the laser sound
	public static void playLaser() throws LineUnavailableException, IOException, UnsupportedAudioFileException
	{

		try
		{
			laserS = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
			laserS.open(AudioSystem.getAudioInputStream(laserF));
			laserS.start();
			
		}
		catch (MalformedURLException e)
		{
			System.out.println(e.getMessage());
		}
	}

	// play the bomb destruction sound
	public static void playExplosion() throws LineUnavailableException, IOException, UnsupportedAudioFileException
	{

		try
		{	
			explosionS = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
			explosionS.open(AudioSystem.getAudioInputStream(explosionF));
			explosionS.start();
			
		}
		catch (MalformedURLException e)
		{
			System.out.println(e.getMessage());
		}
	}

	// play the nuke sound
	public static void playNuke() throws LineUnavailableException, IOException, UnsupportedAudioFileException
	{
		try
		{
			nukeS = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
			nukeS.open(AudioSystem.getAudioInputStream(nukeF));
			nukeS.start();
		}
		catch (MalformedURLException e)
		{
			System.out.println(e.getMessage());
		}
	}
}
	//end class