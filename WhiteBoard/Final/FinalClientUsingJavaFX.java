package Final;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class FinalClientUsingJavaFX extends Application {

   
    public static void main(String[] args) {
    	 NetworkThread netListener = new NetworkThread();
  	     netListener.start();
    	
        launch(args);
    }

   
    private final Color[] palette = {
            Color.BLACK, Color.RED, Color.GREEN, Color.BLUE,
            Color.CYAN, Color.MAGENTA, Color.color(0.95,0.9,0)
    };

    private int currentColorNum = 0;  // The currently selected drawing color,
                                      //   coded as an index into the above array

    public int prevX, prevY;   // The previous location of the mouse, when
                                   // the user is drawing by dragging the mouse.
    public int x , y;   // x-coordinate of mouse.
  
    
    private boolean dragging;   // This is set to true while the user is drawing.

    private Canvas canvas;  // The canvas on which everything is drawn.

    private static GraphicsContext g;  // For drawing on the canvas.
    
    private static Pane root;
    
    private static ArrayList<Rectangle> rectList;
    
    private NetHandler client;
    
    private String input;
    
    public static ArrayList<String> inQueue;
    
    private boolean rectMode = false;

    static boolean serverCon = false;
  

    /**
     * The start() method creates the GUI, sets up event listening, and
     * shows the window on the screen.
     * @throws IOException 
     */
    public void start(Stage stage) throws IOException {
        
    	inQueue = new ArrayList<String>();
    	client = new NetHandler();
        /* Create the canvas and draw its content for the first time. */
        
        canvas = new Canvas(600,400);
        g = canvas.getGraphicsContext2D();
        clearAndDrawPalette();
        
        /* Respond to mouse events on the canvas, by calling methods in this class. */
        
        canvas.setOnMousePressed( e -> mousePressed(e) );
        canvas.setOnMouseDragged( e -> mouseDragged(e) );
        canvas.setOnMouseReleased( e -> mouseReleased(e) );
        
        /* Configure the GUI and show the window. */
        Button rectBtn = new Button("Rectangle");
        root = new Pane(canvas, rectBtn);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Simple Paint");
        stage.show();
        
        rectBtn.setOnAction(new EventHandler < ActionEvent > () {
        	@Override
        	public void handle(ActionEvent e) {
        		rectMode = true;
        	}
        });
       
        }

    /**
     * Fills the canvas with white and draws the color palette and (simulated)
     * "Clear" button on the right edge of the canvas.  This method is called when
     * the canvas is created and when the user clicks "Clear."
     */
    public void clearAndDrawPalette() {

        int width = (int)canvas.getWidth();    // Width of the canvas.
        int height = (int)canvas.getHeight();  // Height of the canvas.

        g.setFill(Color.WHITE);
        g.fillRect(0,0,width,height);

        int colorSpacing = (height - 56) / 7;
        // Distance between the top of one colored rectangle in the palette 
        // and the top of the rectangle below it.  The height of the
        // rectangle will be colorSpacing - 3.  There are 7 colored rectangles,
        // so the available space is divided by 7.  The available space allows
        // for the gray border and the 50-by-50 CLEAR button.

        /* Draw a 3-pixel border around the canvas in gray.  This has to be
             done by drawing three rectangles of different sizes. */

        g.setStroke(Color.GRAY);
        g.setLineWidth(3);
        g.strokeRect(1.5, 1.5, width-3, height-3);

        /* Draw a 56-pixel wide gray rectangle along the right edge of the canvas.
             The color palette and Clear button will be drawn on top of this.
             (This covers some of the same area as the border I just drew. */

        g.setFill(Color.GRAY);
        g.fillRect(width - 56, 0, 56, height);

        /* Draw the "Clear button" as a 50-by-50 white rectangle in the lower right
             corner of the canvas, allowing for a 3-pixel border. */

        g.setFill(Color.WHITE);
        g.fillRect(width-53,  height-53, 50, 50);
        g.setFill(Color.BLACK);
        g.fillText("CLEAR", width-48, height-23); 

        /* Draw the seven color rectangles. */
        
        for (int N = 0; N < 7; N++) {
            g.setFill( palette[N] );
            g.fillRect(width-53, 3 + N*colorSpacing, 50, colorSpacing-3);
        }

        /* Draw a 2-pixel white border around the color rectangle
             of the current drawing color. */

        g.setStroke(Color.WHITE);
        g.setLineWidth(2);
        g.strokeRect(width-54, 2 + currentColorNum*colorSpacing, 52, colorSpacing-1);

    } // end clearAndDrawPalette()


    /**
     * Change the drawing color after the user has clicked the
     * mouse on the color palette at a point with y-coordinate y.
     */
    private void changeColor(int y) {

        int width = (int)canvas.getWidth(); 
        int height = (int)canvas.getHeight(); 
        int colorSpacing = (height - 56) / 7;  // Space for one color rectangle.
        int newColor = y / colorSpacing;       // Which color number was clicked?

        if (newColor < 0 || newColor > 6)      // Make sure the color number is valid.
            return;

        /* Remove the highlight from the current color, by drawing over it in gray.
             Then change the current drawing color and draw a highlight around the
             new drawing color.  */
        
        g.setLineWidth(2);
        g.setStroke(Color.GRAY);
        g.strokeRect(width-54, 2 + currentColorNum*colorSpacing, 52, colorSpacing-1);
        currentColorNum = newColor;
        g.setStroke(Color.WHITE);
        g.strokeRect(width-54, 2 + currentColorNum*colorSpacing, 52, colorSpacing-1);

    } // end changeColor()



  
    public void mousePressed(MouseEvent evt) {

        if (dragging == true)  // Ignore mouse presses that occur
            return;            //    when user is already drawing a curve.
                               //    (This can happen if the user presses
                               //    two mouse buttons at the same time.)
        
        int x = (int)evt.getX();   // x-coordinate where the user clicked.
        int y = (int)evt.getY();   // y-coordinate where the user clicked.

        int width = (int)canvas.getWidth();    // Width of the canvas.
        int height = (int)canvas.getHeight();  // Height of the canvas.
        
        if (x > width - 53) {
            // User clicked to the right of the drawing area.
            // This click is either on the clear button or
            // on the color palette.
            if (y > height - 53)
                clearAndDrawPalette();  //  Clicked on "CLEAR button".
            else
                changeColor(y);  // Clicked on the color palette.
        }
        else if (x > 3 && x < width - 56 && y > 3 && y < height - 3) {
            // The user has clicked on the white drawing area.
            // Start drawing a curve from the point (x,y).
        	
        	//RECTANGLE
        	if (rectMode) {				// rectMode allows user to place one rectangle before continuing to draw
        		Rectangle r = new Rectangle(x, y, 100, 100);
        		root.getChildren().add(r);		//add to Pane
        		rectList.add(r);				//add to Structure
        		rectMode = false;				//allow user to draw again
        		
        		client.send("REC " + x + " " + y + " " + 100 + " " + 100);	//send rectangle command to server
        		
            } else {
            	prevX = x;
            	prevY = y;
            	dragging = true;
            	g.setLineWidth(2);  // Use a 2-pixel-wide line for drawing.
            	g.setStroke( palette[currentColorNum] );
            }
        }

    } // end mousePressed()


    /**
     * Called whenever the user releases the mouse button. Just sets
     * dragging to false.
     */
    public void mouseReleased(MouseEvent evt) {
        dragging = false;
    }


   
    public void mouseDragged( MouseEvent evt) {

        if (dragging == false)
            return;  // Nothing to do because the user isn't drawing.

         x = (int) evt.getX();   // x-coordinate of mouse.
         y = (int) evt.getY();   // y-coordinate of mouse.

        if (x < 3)                          // Adjust the value of x,
            x = 3;                           //   to make sure it's in
        if (x > canvas.getWidth() - 57)       //   the drawing area.
            x = (int)canvas.getWidth() - 57;

        if (y < 3)                          // Adjust the value of y,
            y = 3;                           //   to make sure it's in
        if (y > canvas.getHeight() - 4)       //   the drawing area.
            y = (int) (canvas.getHeight() - 4);
        
       client.send("DRW " + prevX + " " + prevY + " " + x + " " + y); // Send draw command to server
        
        draw( prevX, prevY, x, y);  // Draw the line.
       
        prevX = x;  // Get ready for the next line segment in the curve.
        prevY = y;

    } // end mouseDragged()

    public void draw(int prevX2, int prevY2, int x2, int y2)
    {
    	 g.strokeLine(prevX2, prevY2, x2, y2);  // Draw the line.
    	
    }
    
    public void drawIn(int prevX2, int prevY2, int x2, int y2) {
    	g.strokeLine(prevX2, prevY2, x2, y2);
    }
    public GraphicsContext getGraphics() {
    	return g;
    }
    public static Pane getPane()
    {
    	return root;
    }
    public ArrayList<String> getInQueue() {
		return inQueue;
	}

    public static class NetworkThread extends Thread {

	    public void run(){
	    	 
	    	
			while(serverCon = true)
			{
				if(inQueue.size() != 0) { //only pull if queue has a message
					
					//DECODE
					String formattedIn[] = inQueue.remove(inQueue.size() - 1).split("\\s+"); 
					if (formattedIn[0].equals("Drw")) {
		                
						 g.strokeLine( Integer.parseInt(formattedIn[1]), 
			            		   Integer.parseInt(formattedIn[2]), Integer.parseInt(formattedIn[3]),
			            		   Integer.parseInt(formattedIn[4]));
			              	
			        }
			        else if(formattedIn[0].equals("Rec")) {
			                
			           Rectangle rectangle = new Rectangle(Integer.parseInt(formattedIn[1]), 
			           Integer.parseInt(formattedIn[2]), Integer.parseInt(formattedIn[3]),
			       	   Integer.parseInt(formattedIn[4]));
			           getPane().getChildren().add(rectangle);
			        }
			        else if(formattedIn[0].equals("Cir")) {
			                
			           //Circle circle = new Circle(scnr.nextInt, scnr.nextInt, scnr.nextInt, scnr.nextInt);
			           //circleArray.add(circle);
			           
			        }
			        else if(formattedIn[0].equals("Med")) {
			                
			           //TODO: draw Media
			        }
				} 
			}
	    }
    }
    
    
} // end class SimplePaint