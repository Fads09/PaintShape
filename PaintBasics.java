package javafolder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.JColorChooser;

import java.util.Vector;

public class PaintBasics extends Frame
{
	private static final long serialVersionUID = 1L;

	// the current color
	private Color curColor = Color.black;
	private boolean filled;
	// a menu
	private MenuBar menuBar;
	private Menu testMenu;
	private Menu testMenu2;
	private MenuItem FillItem, deleteItem, deleteShape, colorItem, color, exitItem;
	private MenuItem LineShape, RectangleShape, EllipseShape, PolygonShape, FreeHandDrawing;
	private CheckboxMenuItem Selection,blackBgItem; 
	private PaintShape selected; 
	private int ShapeState = 0;
	private Vector<Point2D> points = new Vector<Point2D>();
    private Vector<PaintShape> ShapeVector = new Vector<PaintShape>();

	//Constructor
	PaintBasics()
	{
		//Enables the closing of the window.
		addWindowListener(new MyFinishWindow());
		// we need mouse input, so need to handle the events associated with it
		addMouseListener(
				new MouseAdapter()
				{
					public void mousePressed(MouseEvent evt)
					{
						// handle mouse down events, in this case that means
						// accumulate points
						
						//If selection was clicked
						
						if(Selection.getState() == true)
						{
							
							//for all shapes
							for(int i = 0; i < ShapeVector.size(); i++)
							{
								if(ShapeVector.get(i).shape().getBounds().contains(evt.getPoint()))
								{
									//Assign that shape to selected
									selected = ShapeVector.get(i);
								}
							 }
						 }
						 else
						 {
							selected = null;
							points.add(new Point2D.Float(evt.getX(), evt.getY())); 
							//Line
							if(ShapeState == 1)
							{
								//If we have 2 points
								if(points.size() == 2)
								{
									Point2D.Float p1 = (Point2D.Float)points.get(0);//get the first point 
									Point2D.Float p2 = (Point2D.Float)points.get(1);//get the second point
									Line2D.Float l = new Line2D.Float(p1.x, p1.y, p2.x, p2.y);
									PaintShape w = new PaintShape(l, filled, curColor);
									ShapeVector.add(w);//stored
									clearPoints();
								 }
							 }
							 //Rectangle
							 if(ShapeState == 2)
							 {
								//It should be two points
								if(points.size() == 2)
								{
									Point2D.Float p1 = (Point2D.Float)points.get(0);//get the first point 
									Point2D.Float p2 = (Point2D.Float)points.get(1);//get the second point
									//Find the min
									int x = (int) Math.min(p1.x,p2.x);
									int y = (int) Math.min(p1.y,p2.y);
									//width
									int w = (int) Math.abs(p1.x - p2.x);
									//Height
									int h = (int) Math.abs(p1.y - p2.y);
									Rectangle2D.Float U = new Rectangle2D.Float(x, y, w, h);
									PaintShape K = new PaintShape(U, filled, curColor);
									ShapeVector.add(K);
									clearPoints();
								 }
							  }
							 //Polygon
							  if(ShapeState == 3)
							  {
								if(points.size() > 3)//If we have more than 3 points
								{
									Point2D.Float p1 = (Point2D.Float)points.get(0);//get the first point
									Point2D.Float p2 = (Point2D.Float)points.get(points.size() - 1);//get the last point
									//Difference between x and y
									int x = (int)(p2.x - p1.x);
									int y = (int)(p2.y - p1.y);
									//Calculate the distance
									int z = (int) Math.sqrt((x*x)+(y*y));
									//If the distance is less than 5px
									if(z <= 5)
									{
										Polygon p = new Polygon();//polygon
										//Connect the point
										for(int i = 0; i < points.size() - 1; i++)
										{
											Point2D.Float o = (Point2D.Float)points.get(i);
											p.addPoint((int)(o.x), (int)(o.y));
										}
											PaintShape s = new PaintShape(p, filled, curColor);
											ShapeVector.add(s);
									 }
								}
							}
							//Ellipse
							if(ShapeState == 4)
							{
								if(points.size() == 2)
								{
									Point2D.Float p1 = (Point2D.Float)points.get(0);//get the first point 
									Point2D.Float p2 = (Point2D.Float)points.get(1);//get the second point
									//Find the min
									int x = (int) Math.min(p1.x,p2.x);
									int y = (int) Math.min(p1.y,p2.y);
									//Width
									int w = (int) Math.abs(p1.x - p2.x);
									//Height
									int h = (int) Math.abs(p1.y - p2.y);
									Ellipse2D.Float E = new Ellipse2D.Float(x, y, w, h);
									PaintShape O = new PaintShape(E, filled, curColor);
									ShapeVector.add(O);
									clearPoints();
								}
							}
							//Free Hand drawing 
							if(ShapeState == 6)
							{
								Path2D.Double path = new Path2D.Double();
								Point2D.Float p1 = new Point2D.Float(evt.getX(), evt.getY());
								path.moveTo(p1.x, p1.y);
								PaintShape w = new PaintShape(path, filled, curColor);
								ShapeVector.add(w);
								clearPoints();
							 }
						}
						repaint();
					}
					public void mouseReleased(MouseEvent evt)
					{
						if(ShapeState == 6)
						{
							// handle mouse up events
							PaintShape c = ShapeVector.get(ShapeVector.size() -1);
							((Path2D.Double) c.shape()).lineTo(evt.getX(), evt.getY());
							repaint();
						}
					}
				}
				);
		addMouseMotionListener(
				new MouseAdapter()
				{
					public void mouseDragged(MouseEvent evt)
					{
						if(ShapeState == 6)
						{
							// handle mouse moved events
							PaintShape c = ShapeVector.get(ShapeVector.size()- 1);
							((Path2D.Double) (c.shape())).lineTo(evt.getX(), evt.getY());
							repaint();
						}
					 }
				}
				);
		createMenu();
	}
	private void createMenu()
	{
		// build a menu
		menuBar = new MenuBar();
		// main menu bar
		setMenuBar(menuBar);
		
				// a menu
				testMenu = new Menu("Shapes");
		   
		      
				menuBar.add(testMenu);
				// clear points menu
				LineShape = new MenuItem("Line");
				testMenu.add(LineShape);
				LineShape.addActionListener(
						new ActionListener()
						{
							public void actionPerformed(ActionEvent evt)
							{
								ShapeState = 1;
							}
						}
						);
				
				// Rectangle 
				RectangleShape = new MenuItem("Rectangle");
				testMenu.add(RectangleShape);
				RectangleShape.addActionListener(
						new ActionListener()
						{
							public void actionPerformed(ActionEvent evt)
							{
								ShapeState = 2;
							}
						}
						);
				// Polygon
				PolygonShape = new MenuItem("Polygon");
				testMenu.add(PolygonShape);
				PolygonShape.addActionListener(
						new ActionListener()
						{
							public void actionPerformed(ActionEvent evt)
							{
								ShapeState = 3;
							}
						}
						);
				// Ellipse 
			    EllipseShape = new MenuItem("Ellipse");
				testMenu.add(EllipseShape);
				EllipseShape.addActionListener(
						new ActionListener()
						{
							public void actionPerformed(ActionEvent evt)
							{
								ShapeState = 4;
							}
						}
						);
				// Free Hand
				FreeHandDrawing = new MenuItem("Free Hand");
				testMenu.add(FreeHandDrawing);
				FreeHandDrawing.addActionListener(
						new ActionListener()
						{
							public void actionPerformed(ActionEvent evt)
							{
								ShapeState = 6;
							}
						}
						);
				
		
		// a menu
		testMenu2 = new Menu("Test");
		menuBar.add(testMenu2);
		
		// clear points menu
		deleteItem = new MenuItem("Delete");
		testMenu2.add(deleteItem);
		// a MenuItem uses an ActionListener to handle events
		deleteItem.addActionListener(
				new ActionListener()
				{
					// when clicked the assigned function gets called
					public void actionPerformed(ActionEvent evt)
					{
						// handle this menu's assigned action, in this case clear all collected points
						clearPoints();
						//remove the shape
						
					}
				}
				);		
				// Remove the shape selected
				deleteShape = new MenuItem("DeleteShape");
				testMenu2.add(deleteShape);
				// a MenuItem uses an ActionListener to handle events
				deleteShape.addActionListener(
						new ActionListener()
						{
							// when clicked the assigned function gets called
							public void actionPerformed(ActionEvent evt)
							{
								//remove the shape
								if(selected != null)
								{
									ShapeVector.remove(selected);
									selected = null;
									repaint();
								}
							}
						}
						);		
		
				
		// color
		colorItem = new MenuItem("Color");
		testMenu2.add(colorItem);
		// the color dialog
		colorItem.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						
						Color c = JColorChooser.showDialog(null, "Select drawing color", curColor);
						curColor = c;
						
						repaint();
					}
				}
				);
		// color
		color = new MenuItem("Color2");
		testMenu2.add(color);
		// the color dialog
		color.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						
						Color t = JColorChooser.showDialog(null, "Select drawing color", curColor);
						curColor = t;
						if(selected != null)
						{
							selected.setColour(t);
						}
						
						repaint();
					}
				}
				);
		//Fill
		FillItem = new MenuItem("Fill Item");
		testMenu2.add(FillItem);
		FillItem.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						//If it's not selected
						if(selected != null)
						{
							selected.setFill(!selected.getFill());
						}
						repaint();
					}
				});
		
		// toggle background
		blackBgItem = new CheckboxMenuItem("Black background");
		testMenu2.add(blackBgItem);
		blackBgItem.addItemListener(
				new ItemListener()
				{
					public void itemStateChanged(ItemEvent evt)
					{
						// when the check mark is on, draw a black background
						// and white otherwise
						// getState() returns a boolean, true for 'checked'
						if(blackBgItem.getState() == true)
						{
							setBackground(Color.black);
						}
						else
						{
							setBackground(Color.white);
						}
						// cause a call to paint() because the background color
						// choice has an effect on the drawing color
						repaint();
					}
				});
		    //Selection
			Selection = new CheckboxMenuItem("Selection");
			testMenu2.add(Selection);
			Selection.addItemListener(
					new ItemListener()
					{
						public void itemStateChanged(ItemEvent evt)
						{
							
						}
					});
		
		// exit
		exitItem = new MenuItem("Exit");
		testMenu2.add(exitItem);
		// the color dialog
		exitItem.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						// the parameter of exit() can indicate an error code
						// zero means the program exited without error
						System.exit(0);
					}
				}
				);
		
	}
	
	protected void setColour(Color green) {
		// TODO Auto-generated method stub
		
	}

	// clear all control points
	private void clearPoints()
	{
		points.clear();
		// call repaint to make the deletion visible; try disabling this call and see what happens
		repaint();
	}

	// main drawing routine
	public void paint(Graphics g)
	{
		//In order to use Java 2D, it is necessary to cast the Graphics object
		//into a Graphics2D object.
		Graphics2D g2d = (Graphics2D) g;
		if(selected != null)
		{
			
			g2d.setPaint(Color.blue);
			g2d.draw(selected.shape().getBounds());
		}

		// query if this menu is checked
		// when the background is black, draw white by default
		if(blackBgItem.getState() == true)
		{
			g2d.setColor(Color.white);
		}
		// otherwise draw with the current color
		else
		{
			g2d.setColor(curColor);
		}
		
		
		
		for(int i = 0; i < points.size(); i++)
		{
			Point2D.Float p = (Point2D.Float)points.get(i);
			Line2D.Float line = new Line2D.Float(p.x - 5, p.y, p.x + 5, p.y);
			g2d.draw(line);
			line = new Line2D.Float(p.x, p.y - 5, p.x, p.y + 5);
			g2d.draw(line);
		}
		//
		for(int j = 0; j < ShapeVector.size(); j++)
		{
			PaintShape x = (PaintShape)ShapeVector.get(j);
		    x.draw(g2d);//call the draw method from PaintShape
		}
		
	
	}
	

	public static void main(String[] argv)
	{
		//Generate the window.
		PaintBasics f = new PaintBasics();
		
		//Define a title for the window.
		f.setTitle("Java 2D paint basics");      
		//Definition of the window size in pixels
		f.setSize(800, 600);
		//Show the window on the screen.
		f.setVisible(true);
	}
}