/** Title: Blasteroids
 * 	File: MainApplet.java
 * 	Coder(s): Rebecca Harris, Julia Sloan, Werner Uetz
 * 	Date: August 16, 2013
 * 	Description: This program utilizes Applet to create a functioning (bl)asteroids game.
 * 				 The point is for the player to shoot the ships/bombs with mouse clicks before they hit earth 
 * 				 (the bottom edge of the Applet). If a bomb's center hits the bottom then the
 * 				 player loses and humanity is destroyed, however if the player gets 100 points
 * 				 they win the game.
 * 				 Sometimes (20% of the time) when a bomb is destroyed it will split into 2 smaller bomblets.
 * 				 Every 10th point the user gets a nuke they can use by pressing the space bar.
 * 				 The nuke is used to destroy all bombs that are currently on the screen. Once the
 * 				 player has used a nuke, the following ships descend faster.
 */

// imports
import java.applet.*; 
import java.awt.event.*; 
import java.awt.*; 
import java.io.File;
import java.util.*;

import javax.imageio.ImageIO;

public class MainApplet extends Applet implements MouseListener, MouseMotionListener, KeyListener
{ 
	// Variable declarations
	// The object we will use to write with instead of the standard screen graphics 
	Graphics bufferGraphics;
	
	// The image that will contain everything that has been drawn on 
	Image offscreen; 
	
	// To get the width and height of the applet. 
	Dimension dim; 
	
	int curX, curY; //mouse coordinates
	Color lineColour = Color.RED;
	boolean nukeGoingOff = false, backgroundImageExists = false, nukeImageExists = false;
	ArrayList<Bomb> bombs = new ArrayList<Bomb>();
	
	Laser myLaser;

	int bombsDestroyed = 0;
	int numNukes = 0;
	int totalScore = 0;

	double bombFrequency = 0.015;

	Image bgImg, nkImg;

	// Method name: init
	// Description: Initializes the applet
	// Param: N/A
	// Returns: N/A void
	public void init()  
	{ 
		// set size of applet
		this.setSize(600, 600);

		// make applet size immutable
		dim = getSize(); 

		// add listeners
		addMouseListener(this); 
		addMouseMotionListener(this);
		addKeyListener(this);

		// check to see if the nuke and background images exist
		backgroundImageExists =	getBackgroundImage();
		nukeImageExists = getNukeImage();

		// double buffering assistance - to prevent flickering
		offscreen = createImage(dim.width,dim.height); 

		bufferGraphics = offscreen.getGraphics(); 
	} 

	// Method name: humanityDestroyed
	// Description: when a bomb's center hits the bottom edge of the applet the user has lost
	//				this ends the game
	// Param: Graphics object to edit screen
	// Returns: N/A void
	public void humanityDestroyed(Graphics g)
	{
		g.setColor(Color.WHITE);
		bufferGraphics.clearRect(0,0,dim.width,dim.width); 
		bufferGraphics.setColor(Color.RED);
		
		// losing message on screen
		bufferGraphics.drawString("Humanity has been destroyed, you have failed! Game ending in 5 seconds.", 100, 300);
		bufferGraphics.setColor(Color.BLACK);
		bufferGraphics.drawString("Nukes: " + numNukes, 500, 20);
		bufferGraphics.drawString("Score:  " + totalScore, 500, 35);
		g.drawImage(offscreen,0,0,this);
		
		// wait then end the program
		try {Thread.sleep(5000);} catch (InterruptedException e) {System.out.println(e.getMessage());}
		System.exit(0);
	}
	
	// Method name: humanitySaved
	// Description: when the user has earned 100 points they have won the game, this then ends the game
	// Param: Graphics object to edit screen
	// Returns: N/A void
	public void humanitySaved(Graphics g)
	{
		g.setColor(Color.WHITE);
		bufferGraphics.clearRect(0,0,dim.width,dim.width); 
		bufferGraphics.setColor(Color.RED);
		
		// success message on screen
		bufferGraphics.drawString("Humanity has been saved, you're our hero! Game ending in 5 seconds.", 100, 300);
		bufferGraphics.setColor(Color.BLACK);
		bufferGraphics.drawString("Nukes: " + numNukes, 500, 20);
		bufferGraphics.drawString("WINNING Score:  " + totalScore, 445, 35);
		g.drawImage(offscreen,0,0,this); 
		
		// wait then end the program
		try {Thread.sleep(5000);} catch (InterruptedException e) {System.out.println(e.getMessage());}
		System.exit(0);
	}

	// Method name: getBackgroundImage
	// Description: retrieves the background image
	// Param: N/A
	// Returns: true/false if the image is successfully found and loaded
	public boolean getBackgroundImage()
	{
		try {
			File bg = new File("../images/background.jpg");
			bgImg = ImageIO.read(bg);
			return true;
		}
		catch (Exception ex)	{	return false;	}

	}
	
	// Method name: setBackground
	// Description: sets the background image to the background if it was found
	// Param: N/A
	// Returns: N/A void
	public void setBackground() 
	{

		if(backgroundImageExists)
		{
			bufferGraphics.drawImage(bgImg, 0, 0, this);
		}

	}

	// Method name: getNukeImage
	// Description: retrieves the nuke image
	// Param: N/A
	// Returns: true/false if the image is successfully found and loaded
	public boolean getNukeImage()
	{
		try {
			File nk = new File("../images/mushcloud.jpg");
			nkImg = ImageIO.read(nk);
			return true;
		}
		catch (Exception ex)	{	return false;	}

	}
	
	// Method name: setNukeImage
	// Description: sets the nuke image to the background if it was found
	// Param: N/A
	// Returns: N/A void
	public void setNukeImage() 
	{

		if(nukeImageExists)
		{
			bufferGraphics.drawImage(nkImg, 0, 0, this);
		}

	}

	// Method name: paint
	// Description: displays the content on the screen using the double buffer
	// Param: Graphics object to edit the screen 
	// Returns: N/A void
	public void paint(Graphics g)  
	{ 
		// if the user won the game
		if(totalScore >= 100) {
			humanitySaved(g);
			return;
		}		

		// if the user used a nuke - use nuke background
		if(nukeGoingOff == true){
			setNukeImage();
			g.drawImage(offscreen,0,0,this); 
			repaint();

			try {Thread.sleep(1600);} catch (InterruptedException e) {System.out.println("Error");}
			
			nukeGoingOff = false;
		}
		else // if the user did not use a nuke - use regular background
		{
			setBackground();
		}

		// display laser
		if(myLaser != null)
		{
			bufferGraphics.setColor(myLaser.getColour());
			bufferGraphics.drawLine(myLaser.getHStart(), myLaser.getVStart(), myLaser.getX(), myLaser.getY());
		}

		// the laser shooter at the bottom of the screen
		bufferGraphics.setColor(Color.GRAY);
		bufferGraphics.fillOval(275, 580, 50, 50);
		
		// creates all the bombs
		for(int i = 0; i < bombs.size(); ++i)
		{
			// draw the bomb with the ship image
			Bomb b = bombs.get(i);
			bufferGraphics.drawImage(b.getImage(), b.getX(), b.getY(), b.getWidth(), b.getHeight(), this);
			
			if(b.getY() >= (600 - (b.getRadius()*.5))) {
				humanityDestroyed(g);
				return;
			}

			// update the bomb position
			b.updatePos();
		}
		
		// add the score and nuke count to the screen
		bufferGraphics.setColor(Color.WHITE);
		bufferGraphics.drawString("Nukes: " + numNukes, 500, 20);
		bufferGraphics.drawString("Score:  " + totalScore, 500, 35);
		
		// draw the offscreen image to the screen like a normal image. 
		// Since offscreen is the screen width we start at 0,0. 
		g.drawImage(offscreen,0,0,this); 
		try {Thread.sleep(20); } catch (InterruptedException e) {}
		repaint();
	} 

	// Method name: update
	// Description: used for double buffering to prevent flickering. New bombs are also made.
	// Param: Graphics object to update screen
	// Returns: N/A void
	public void update(Graphics g) 
	{ 
		paint(g); 
		if((Math.random()* 1) < 0.02 )
		{
			bombs.add(new Bomb());
		}
	} 

	// Method name: mouseMoved event
	// Description: updates the current x and y coordinate variables with the mouse
	// Param: MouseEvent event object
	// Returns: N/A void
	public void mouseMoved(MouseEvent e) {
		curX = e.getX(); 
		curY = e.getY(); 
		repaint(); 
	}

	// Method name: mousePressed
	// Description: displays/shoots the laser and checks to see if a bomb was hit
	//				plays the sound associated with the laser
	// Param: MouseEvent event object
	// Returns: N/A void
	public void mousePressed(MouseEvent e) {
		myLaser = new Laser(curX, curY, lineColour, 600, dim.width/2);

		try
		{
			// play the laser sound when the laser is fired
			new Thread(
					new Runnable() {
						public void run() {
							try {
								GameSounds.playLaser();
							} catch (Exception e) {
								System.out.println(e.getMessage());
							}
						}
					}).start();
		}
		catch(Exception e1)
		{
			System.out.println(e1.getMessage());
		}

		// check if a bomb was hit when the laser was fired
		for(int i = 0; i < bombs.size() ; ++i)
		{
			if(bombs.get(i).intersects(curX, curY))
			{
				if(bombs.get(i).attack() <= 0)
				{
					try
					{
						// if the bomb was destroyed, play an explooooosion sound
						new Thread(
								new Runnable() {
									public void run() {
										try {
											GameSounds.playExplosion();
										} catch (Exception e) {
											System.out.println(e.getMessage());
										}
									}
								}).start();
					}
					catch(Exception e1)
					{
						System.out.println(e1.getMessage());
					}

					// edit score and number of bombs destroyed accordingly
					bombsDestroyed += 1;
					totalScore += 1;//total score is never reset

					// if there was more than 10 bombs destroyed, increase a nuke
					if(bombsDestroyed >= 10)
					{
						numNukes += 1;
						bombsDestroyed -= 10;//bombs destroyed is reset
					}

					// check to see if the bomb is a splitter
					if(bombs.get(i).isSplitter() == false) // the bomb is not a splitter
					{
						// remove the bomb from the array of bombs
						bombs.remove(i);
					}
					else // the bomb is a splitter
					{
						// create the bomblets from the parent bomb
						Bomb parentBomb = bombs.get(i);
						bombs.remove(i); // remove the parent bomb
						bombs.add(new Bomb(parentBomb, parentBomb.getX() - parentBomb.getRadius()));
						bombs.add(new Bomb(parentBomb, parentBomb.getX() + parentBomb.getRadius()));

						return;
					}
				}

			}

		}
	}

	// Method name: mouseReleased
	// Description: destroy laser - it is no longer being shot
	// Param: MouseEvent event object
	// Returns: N/A void
	public void mouseReleased(MouseEvent e) {
		myLaser = null;
	}

	// Method name: keyTyped
	// Description: if the user pressed the space bar to use a nuke - check
	//				check to see they have enough to use one then destroy all the bombs
	//				on the screen.
	// Param: KeyEvent event object
	// Returns: N/A void
	@Override
	public void keyTyped(KeyEvent e) 
	{
		// if keyboard button pressed was the space bar
		if(e.getKeyChar() == KeyEvent.VK_SPACE )
		{
			// if they have an available nuke
			if(numNukes > 0)
			{
				nukeGoingOff = true;
				bombFrequency += 0.015;	

				// because they used a nuke, we need increase the difficulty by making
				// the bombs move faster
				Bomb.increaseVelocity();

				//add points to score and increase the number of bombs they destroyed
				totalScore += bombs.size();
				bombsDestroyed += bombs.size();

				// if they destroyed a large number of bombs, we need to increase the
				// number of nukes after using a nuke 
				if(bombsDestroyed >=10)
				{ 
					int nukeBombs = (int)((double)bombsDestroyed / 10.0);
					numNukes += nukeBombs;
					bombsDestroyed = nukeBombs % 10;
				}

				try
				{
					// play the nuke sound
					new Thread(
							new Runnable() {
								public void run() {
									try {
										GameSounds.playNuke();
									} catch (Exception e) {
										System.out.println(e.getMessage());
									}
								}
							}).start();
				}
				catch(Exception e1)
				{
					System.out.println(e1.getMessage());
				}

				//release nuke - use up a nuke and get rid of all bombs on screen
				bombs.removeAll(bombs);
				numNukes -= 1;
			}
		}
	}

	//auto implemented methods
	@Override
	public void keyPressed(KeyEvent e) {	}
	public void keyReleased(KeyEvent e) {	}
	public void mouseEntered(MouseEvent e) {	}
	public void mouseExited(MouseEvent e) {	}
	public void mouseDragged(MouseEvent e)  { 	} 
	public void mouseClicked(MouseEvent e)	{
		myLaser = null; // empty laser
	}
} 

