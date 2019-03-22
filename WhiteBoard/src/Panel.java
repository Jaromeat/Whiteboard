import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

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

		          System.out.println("Mouse is being dragged at X: " + x1 + " Y: " + y1);  

		          g.drawLine(x1,y1,x2,y2);

		          x2=x1;
		          y2=y1;
	        }

	       
	      }
	    }