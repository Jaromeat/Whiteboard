
import java.net.*;
import java.util.Scanner;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


	public class TestDrawingClient {
	
	    public static final int[] xyin = null;

		public static void main(String[] args) throws IOException {
	    	ServerSocket socket = new ServerSocket(6688);
	    	Socket clientSocket = socket.accept();
	    	System.out.println("Connected");
	    	
	    	ObjectInputStream oin = new ObjectInputStream(clientSocket.getInputStream());
	    	try {
				int[] xyin = (int[])oin.readObject();
			} catch (ClassNotFoundException e) {
				
			}
	    	
	    	System.out.println(oin.toString());
	    	
	    	
	    	JFrame frame = new JFrame( "Run Panel" );
	    	    frame.setDefaultCloseOperation(3);

	    	    Panel panel = new Panel();
	    	    frame.add( panel );

	    	    frame.pack();
	    	    frame.setVisible( true );
	    	    oin.close();
	    	    
	    	
	    }
	    
	    
	   
}