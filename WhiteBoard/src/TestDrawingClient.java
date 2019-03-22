
import java.net.*;
import java.util.Scanner;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TestDrawingClient {

public static int[] xyin;	

	public static void main(String[] args) throws IOException {
		
		ServerSocket socket = new ServerSocket(6688);
		Socket clientSocket = socket.accept();
		System.out.println("Connected");
		boolean run = true;

		ObjectInputStream oin = new ObjectInputStream(clientSocket.getInputStream());
		
		JFrame frame = new JFrame("Run Panel");
		frame.setDefaultCloseOperation(3);

		Panel panel = new Panel();
		frame.add(panel);

		frame.pack();
		frame.setVisible(true);

		
			
			while (run = true) 
			{
				try 
				{
					 int[] input = (int[]) oin.readObject();
					 xyin = input;
				} 
				catch (ClassNotFoundException e) 
				{

				}
			}

		

		

			
		

	}

}