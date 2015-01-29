/** Title: Blasteroids
 * 	File: Laser.java
 * 	Coder(s): Rebecca Harris, Julia Sloan, Werner Uetz
 * 	Date: August 16, 2013
 * 	Description: Handles the laser used to shoot the bombs
 */

import java.awt.Color;

public class Laser
{
	// variables for the laser
	private int x;
	private int y;
	private int vStart;
	private int hStart;
	private Color colour;
	
	// laser default constructor
	public Laser()
	{
		this.x = 0;
		this.y = 0;
		this.colour = Color.WHITE;
		this.vStart = 600;
		this.hStart = 300;
	}
	
	// laser constructor to set the details of the laser
	public Laser(int nX, int nY, Color nColour, int nVStart, int nHStart)
	{
		this.x = nX;
		this.y = nY;
		this.colour = nColour;
		this.vStart = nVStart;
		this.hStart = nHStart;
	}
	
	// Getters and setters for the laser's variables
	public int getX()
	{
		return this.x;
	}
	
	public void setX(int nX)
	{
		this.x = nX;
	}
	
	public int getY()
	{
		return this.y;
	}
	
	public void setY(int nY)
	{
		this.y = nY;
	}
	
	public Color getColour()
	{
		return this.colour;
	}
	
	public void setColour(Color c)
	{
		this.colour = c; 
	}
	
	public int getVStart()
	{
		return this.vStart;
	}
	
	public int getHStart()
	{
		return this.hStart;
	}
}
	//end class