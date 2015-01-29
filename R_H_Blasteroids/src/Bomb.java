/** Title: Blasteroids
 * 	File: Bombs.java
 * 	Coder(s): Rebecca Harris, Julia Sloan, Werner Uetz
 * 	Date: August 16, 2013
 * 	Description: Contains all info and methods associated with each bomb such as
 * 				 increasing the velocity, updating position, checking if it's a
 * 				 splitter, etc.
 */

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bomb {

	// variables
	private int x;
	private int y;
	private int radius;
	private int velocity;
	private int initialStrength;
	private int remainingStrength;
	private boolean isSplitter;
	private Color colour;
	
	// variables needed to make the bomb images ships
	private Image img; //default image
	private int height;
	private int width;
	private File[] fArray = new File[]{ new File("../images/ship1.png"),
	new File("../images/ship2.png"),
	new File("../images/ship3.png"),
	new File("../images/ship4.png"),
	new File("../images/ship5.png"),
	new File("../images/ship6.png"),
	new File("../images/ship7.png")
	};

	//damaged ship images
	private File[] fdmgArray = new File[]{ new File("../images/ship1dmg.png"),
			new File("../images/ship2dmg.png"),
			new File("../images/ship3dmg.png"),
			new File("../images/ship4dmg.png"),
			new File("../images/ship5dmg.png"),
			new File("../images/ship6dmg.png"),
			new File("../images/ship7dmg.png")
			};
	//critical damage images
	private File[] fcritArray = new File[]{ new File("../images/ship1crit.png"),
			new File("../images/ship2crit.png"),
			new File("../images/ship3crit.png"),
			new File("../images/ship4crit.png"),
			new File("../images/ship5crit.png"),
			new File("../images/ship6crit.png"),
			new File("../images/ship7crit.png")
			};
	
	private int fNum;	//random file number
	
	private static int velFrequency = 1;//only one variable for all Bombs

	private javax.swing.JColorChooser j = new javax.swing.JColorChooser();

	// Constructor: Bomb
	// Description: Constructor for parent bombs
	// Param: N/A
	// Returns: N/A void
	public Bomb()
	{
		this.x = (int)(Math.random() * 375 + 100);
		this.y = 0;
		this.velocity = (int)(Math.random()* 1 + velFrequency);
		
		//This sets the height of the bomb
		this.height = (int)(Math.random()* 50 + 40);	
		this.width = (int)(Math.random()* 50 + 40);
		
		//create a radius based on the greatest width/height
		if (this.height > this.width)
			this.radius = this.height;
		else
			this.radius = this.width;
		
		this.fNum = (int) (Math.random()* 6);
		
		//set the image based on the file selected
		try
		{
			img = ImageIO.read(fArray[this.fNum]);
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
		
		this.initialStrength = (int)(Math.random()* 30 + 10);
		this.remainingStrength = this.initialStrength;
		
		// Increases splitter chance to 20% to increase game difficulty
		this.isSplitter = (Math.random()* 1) > 0.8 ? true : false;
	}

	// Constructor: Bomb constructor for bomblets
	// Description: Constructor for bomblet
	// Param: Bomb object of parent, int for x position of parent
	//must be called twice, one for each of the positions for deltaX
	public Bomb(Bomb other, int deltaX)
	{
		//creates a bomb based on a parent bomb
		this.x = deltaX;
		this.y = other.getY();
		this.velocity = other.getVelocity();
		this.radius = other.getRadius() /2;
		this.height = (int)(Math.random()* 80 + 40)/2;	//set the height
		this.width = (int)(Math.random()* 80 + 40)/2;	//set the width
		
		//set radius based on height and width
		if (this.height > this.width)
			this.radius = this.height/2;
		else
			this.radius = this.width/2;		
		
		this.initialStrength = other.getInitialStrength();
		this.remainingStrength = other.getInitialStrength();
		this.isSplitter = false;//bomblets cannot be splitters
		
		//Set the image file
		try
		{
			img = ImageIO.read(fArray[this.fNum]);	//temp until forloop
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}

	}
	
	// Getter to see if bomb is a splitter or not
	public boolean isSplitter()
	{
		return this.isSplitter;
	}

	// Method name: updatePos
	// Description: Update the y position of the bomb, accommodating for the velocity of the bomb
	// Param: N/A
	// Returns: N/A void
	public void updatePos()
	{
		this.y = this.y + this.velocity;
	}

	// Getters
	int getX()
	{
		return x;
	}
	
	int getY()
	{
		return y;
	}
	
	int getRadius()
	{
		return radius;
	}
	
	int getInitialStrength()
	{
		return this.initialStrength;
	}
	
	int getRemainingStrength()
	{
		return this.remainingStrength;
	}
	
	int getVelocity()
	{
		return this.velocity;
	}
	
	Color getColour()
	{
		return this.colour;
	}
	
	Image getImage()
	{
		return this.img;
	}
	int getHeight()
	{
		return this.height;
	}
	int getWidth()
	{
		return this.width;
	}


	// Method name: attack
	// Description: when the laser has hit the bomb, performs damage calculation as well
	//				as alters the color of the bomb to represent the damage done
	// Param: N/A
	// Returns: int - the remaining life/strength of the bomb
	public int attack()//has been attacked
	{
		//decrease strength/life of the bomb by 10
		remainingStrength -= 10;
		
		// get new color
		double red = (1.0 - (double)remainingStrength/(double)initialStrength)*255.0;;
		double blue = (double)remainingStrength/(double)initialStrength*255.0;

		try 
		{
			//Change the state of ship based on damage received
			if (remainingStrength <= 10)
				this.img = ImageIO.read(fcritArray[fNum]);
			else if (remainingStrength <= 20)
				this.img = ImageIO.read(fdmgArray[fNum]);
		}
		catch(IOException e)
		{
			//for when the color has gone into the negative. it is not a valid color. 
			//but it will be removed from the list of active bombs regardless.
		}

		return remainingStrength;
	}


	// Method name: intersects
	// Description: checks to see if the laser has hit the bomb (if it is within the bomb's radius)
	// Param: double mouse X and Y coordinates
	// Returns: true or false if it does or does not intersect with the bomb
	public boolean intersects(double mouseX, double mouseY)
	{
		//the laser scores a hit if the distance from the 
		//co-ordinates of the mouse to the circle that represents the bomb 
		//is equal to or less than the radius of the bomb
		if( (mouseX - this.x) <= (this.radius) && (mouseY - this.y ) <= (this.radius) && (mouseX - this.x) >= 0 && (mouseY - this.y ) >= 0 )
			return true;
		else
			return false;

	}

	// Method name: increaseVelocity
	// Description: Increases the velocity of the bomb - movement becomes faster with each call to
	//				this method.
	// Param: N/A
	// Returns: N/A void
	public static void increaseVelocity()
	{
		velFrequency += 1;
	}
}
