import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JPanel;

import application.Circle;

public class Panel extends JPanel
	    {
	      // PROPERTIES
	      private final int DEFAULT_WIDTH  = 800;
	      private final int DEFAULT_HEIGHT = 800;
	      private final Color BACK_COLOR   = Color.WHITE;

	      private int x1, y1, x2, y2;

	      private MyMouseHandler handler;
	      private Graphics g;

	      // CONSTRUCTOR
	      public Panel()
	      {
	        setBackground( BACK_COLOR );
	        setPreferredSize( new Dimension( DEFAULT_WIDTH, DEFAULT_HEIGHT ) );

	        handler  = new MyMouseHandler();

	        this.addMouseListener( handler );
	        this.addMouseMotionListener( handler );
	      }

	      // METHOD
	      public void paintComponent(Graphics g)
	      {
	        super.paintComponent(g);
	      }

	      private void setUpDrawingGraphics()
	      {
	        g = getGraphics();
	      }

	      // INNER CLASS
	      private class MyMouseHandler extends MouseAdapter
	      {
	        public void mousePressed( MouseEvent e )
	        {
	        	  x1 = TestDrawingClient.xyin[0];
			      y1=  TestDrawingClient.xyin[1];
		          x2 = TestDrawingClient.xyin[2];
		          y2=  TestDrawingClient.xyin[3];

		          System.out.println("Line Drawn at at X: " + x1 + " Y: " + y1);  

		          g.drawLine(x1,y1,x2,y2);
		         
	        }

	       
	      }
	      public ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
		    public ArrayList<Circle> circles = new ArrayList<Circle>();
		      
		    /**
		     * creates a rectangle in client's panel and sends to server
		     * OR
		     * deletes a rectangle in client's panel and sends delete to server
		     * 
		     * @param erase
		     * @param x
		     * @param y
		     * @param width
		     * @param height
		     */
		  	public void sendRectangle( boolean erase, int x, int y, int width, int height) {
				
		  		
		  			//TODO: remove this triangle
		  		
		  		//TODO: add this Rectangle
		  		
		  		sendMessage("Rec " + eraseNum(erase) + " " + x + " " + y + " " + width + " " + height);
				
			}
		  	
			/**
			 * creates a rectangle in client's panel and sends to server
		     * OR
		     * deletes a rectangle in client's panel and sends delete to server
		     * 
			 * @param erase
			 * @param centerX
			 * @param centerY
			 * @param Radius
			 */
			public void sendCircle( boolean erase, int centerX, int centerY, int Radius) {
				if( erase) {	
				//TODO: remove this circle
				}
				
				//TODO: add  this circle
				sendMessage("Cir " + eraseNum(erase) + " " + centerX + " " + centerY + " " + Radius);
			}
			
			/**
			 * creates a Media object in client's panel and sends to server
			 * OR
			 * deletes a rectangle in client's panel and sends delete to server
			 * 
			 * @param erase
			 * @param x
			 * @param y
			 * @param http
			 */
			public void sendMedia( boolean erase, int x, int y, String http ) {
				if (erase) {
					//TODO: remove this media
				}
				//TODO: draw this media
				
				sendMessage("Med " + eraseNum(erase) + " " + x + " " + y + " " + http );
			}
			
			public void sendAll() {
				//TODO: sends all stored shapes and drawings
				}
				
			
			
			/**
			 * sends the command string to the server to be broadcast
			 * @param s
			 */
			
			public void sendMessage(String s) {
				//TODO
			}
			
			/**
			 * determines numeric equivalent to the erase boolean and returns that value
			 * @param erase
			 * @return
			 */
			private int eraseNum(boolean erase) {
				int eraseNum = 0;
				if(erase)
					eraseNum = 1;
				return eraseNum;
			}
			
			
			/**
			 * takes an incoming string, decodes and performs all commands
			 * @param s
			 */	
			public void recieveIntruction(String s) {
				
				Scanner scnr = new Scanner(s);
				
				if (s.substring(0, 2).equals("Drw")) {
					if (scnr.nextInt() == 1) {
						//TODO: search draw array for this line to remove it
					}
					//TODO: draw line
				}
				else if(s.substring(0, 2).equals("Rec")) {
					if(scnr.nextInt() == 1) {
						//TODO: search rectangle array for this Rectangle to remove it
					}
					//Rectangle rectangle =
					//new Rectangle(scnr.nextInt, scnr.nextInt, scnr.nextInt,scnr.nextInt);
					//rectangleArray.add(rectangle);
				}
				else if(s.substring(0, 2).equals("Cir")) {
					if(scnr.nextInt() == 1) {
						//TODO: search circle array for this circle to remove it
					}
					//Circle circle = new Circle(scnr.nextInt, scnr.nextInt, scnr.nextInt, scnr.nextInt);
					//circleArray.add(circle);
					//
				}
				else if(s.substring(0, 2).equals("Med")) {
					if(scnr.nextInt() == 1) {
						//TODO: search media array for this Media to remove it
					}
					//TODO: draw Media
				}
			}
	    }