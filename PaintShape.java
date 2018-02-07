package javafolder;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
/*
 * PaintShape class
 * 
 * @Fadoua Amrou
 */
public class PaintShape
{
	private Shape shape;//shape
	private boolean filled;//Filled
	private Color color;//color
	
	
	//Constructor
	public PaintShape(Shape shape, boolean filled, Color color)
	{
		this.color = color;
		this.filled = filled;
		this.shape = shape;
		
	}
	
	public void draw(Graphics2D g)
	{
		
		g.setPaint(color); 
		if(filled == false)
		{
			g.draw(shape);//draw shape
		}
		else
		{
			g.fill(shape);//Fill shape
		}
		
	}
	public Shape shape()
	{
		return shape;
	}
	//Get Color
	public Color getColour()
	{
		 return color;
	}
	//Set Color
	public void setColour(Color coulor)
	{
	    color = coulor;
	}
	//Get Color
	public boolean getFill()
	{
	    return filled;
	}
	//Set Color
	public void setFill(boolean fill)
	{
	    filled = fill;
	}

}

