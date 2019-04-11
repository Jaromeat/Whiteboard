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
	
	ChatSocket = new Socket("10.200.208.146", 50000);
	
	System.out.println("Connection Successful");
	
	
	run = true;
	
	runClientThreads();
		
	}
	

	public void runClientThreads() {
		Thread input = new Thread(new Runnable() 
				{

					public void run() 
					{
						inQueue = test.getInQueue();
						try {
							in = new DataInputStream(ChatSocket.getInputStream());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						while(run)
						{
							try
							{
								
								nextInput = in.readUTF();
								if(nextInput != null) {
									System.out.println(nextInput);   
								    inQueue.add(nextInput);
								         
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
				outQueue = new ArrayList<String>();
				try 
				{
					out = new DataOutputStream(ChatSocket.getOutputStream());
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
				while(true)
				{		
					try {
						Thread.sleep(10);
						//System.out.println(nextOutput);
						if(outQueue.size() != 0) {
							nextOutput = outQueue.remove(0);
							if (nextOutput == null) {
								continue;
							}
							System.out.println(nextOutput);
								out.writeUTF(nextOutput);
								out.flush();
								//System.out.println(nextOutput);
						}	
					}
					catch (IOException e) 
					{
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
						
						nextOutput = null;
					}
					
				}
	
		});output.start();
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
	public Socket getSocket()
	{
		return ChatSocket;
		
	}
	

	public static void main(String[] args) throws IOException {
		NetHandler client = new NetHandler();
	}
}