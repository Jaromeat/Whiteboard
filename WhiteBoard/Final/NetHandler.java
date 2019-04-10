package Final;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;

import com.sun.javafx.geom.Rectangle;

public class NetHandler {
	
	final public static String host = "localhost";
	
	Socket RoutingSocket;
	Socket ChatSocket;
	DataInputStream in;
	DataOutputStream out;
	Scanner chat;
	boolean run;
	String nextOutput = null;
	String prevOutput = null;
	String nextInput = null;
	FinalClientUsingJavaFX test = new FinalClientUsingJavaFX();
	ArrayList<String> inQueue;
	ArrayList<String> outQueue;

	public NetHandler() throws IOException 
	{
	
	ChatSocket = new Socket("10.200.246.25", 50000);
	
	System.out.println("Connection Successful");
	
	inQueue = new ArrayList<String>();
	outQueue = new ArrayList<String>();
	run = true;
	
	runClientThreads();
		
	}
	

	public void runClientThreads() {
		Thread input = new Thread(new Runnable() 
				{

					public void run() 
					{
						
						while(run)
						{
							try
							{
								in = new DataInputStream(ChatSocket.getInputStream());
								nextInput = in.readUTF();
								if(nextInput != null) {
									System.out.println(nextInput);
									
								    		
								    		if (nextInput.substring(0, 2).equals("Drw")) {
								                
								               test.getGraphics().strokeLine(Integer.parseInt(nextInput.substring(4, 7)), 
								            		   Integer.parseInt(nextInput.substring(8, 11)), Integer.parseInt(nextInput.substring(13, 7)),
								            		   Integer.parseInt(nextInput.substring(4, 7)));
								            }
								            else if(nextInput.substring(0, 2).equals("Rec")) {
								                
								                Rectangle rectangle = new Rectangle(Integer.parseInt(nextInput.substring(4, 7)), 
									            		   Integer.parseInt(nextInput.substring(8, 11)), Integer.parseInt(nextInput.substring(13, 7)),
									            		   Integer.parseInt(nextInput.substring(4, 7)));
								               // test.getPane().getChildren().add(rectangle);
								            }
								           else if(nextInput.substring(0, 2).equals("Cir")) {
								                
								                //Circle circle = new Circle(scnr.nextInt, scnr.nextInt, scnr.nextInt, scnr.nextInt);
								                //circleArray.add(circle);
								                //
								            }
								            else if(nextInput.substring(0, 2).equals("Med")) {
								                
								                //TODO: draw Media
								            }
								   }
							}
						
							catch(IOException e)
							{
								e.printStackTrace();
							}
						}
						
					}
			
				});
		input.start();
		Thread output = new Thread(new Runnable() 
		{

			public void run() 
			{
				try 
				{
					out = new DataOutputStream(ChatSocket.getOutputStream());
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
				while(run)
				{
					System.out.println(nextOutput);

					if(!outQueue.isEmpty()) {
						
						nextOutput = outQueue.remove(outQueue.size() - 1);
						
						try 
						{
							out.writeUTF(nextOutput);
							System.out.println(nextOutput);
							nextOutput = null;
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
						
					
					}
					
				}
			}
	
		});
		output.start();
	}

	public void closeConnection() throws IOException
	{
		out.close();
		in.close();
		ChatSocket.close();
	}
	
	public void send(String output) {
		outQueue.add(output);
	}

	public static void main(String[] args) throws IOException {
		NetHandler client = new NetHandler();
	}
}