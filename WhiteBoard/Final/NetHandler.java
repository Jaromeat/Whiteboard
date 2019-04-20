package Final;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

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
	
	ArrayList<String> outQueue;
	

	public NetHandler() throws IOException 
	{
	
	ChatSocket = new Socket(host, 50001);		//connect to server
	
	System.out.println("Connection Successful");
	
	
	run = true;
	
	runClientThreads();			//start threads
		
	}
	
	/**
	 * runs input and output thread
	 */
	public void runClientThreads() {
		Thread input = new Thread(new Runnable() //INPUT THREAD
				{

					public void run() 
					{
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
								Thread.sleep(10);
								nextInput = in.readUTF();		//check for input every 10 milliseconds
								if(nextInput != null) {			
									System.out.println(nextInput);   
								    FinalClientUsingJavaFX.getInQueue().add(nextInput);	//add any input to the input queue and print it
								         
								   } 
							}
							
							catch(IOException | InterruptedException e)
							{
								e.printStackTrace();
							}
						}
						
					}
			
				});
		input.start();
		Thread output = new Thread(new Runnable() // OUTPUT THREAD
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
						if(outQueue.size() != 0) {					//if the output queue isnt empty remove an entry
							nextOutput = outQueue.remove(0);
							if (nextOutput == null) {
								continue;
							}
							System.out.println(nextOutput);
								out.writeUTF(nextOutput);			//print the output and send it to the server
								out.flush();						//clear the stream
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
					
						
						nextOutput = null;			//clear nextOutput
					}
					
				}
	
		});output.start();
	}
	/**
	 * closes all datastreams and sockets in use
	 * @throws IOException
	 */
	public void closeConnection() throws IOException
	{
		out.close();
		in.close();
		ChatSocket.close();
	}

	/**
	 * add output to the output queue
	 * @param output
	 */
	public void send(String output) {
		outQueue.add(output);
	}
	
	/**
	 * getter method for socket
	 * @return
	 */
	public Socket getSocket()
	{
		return ChatSocket;
		
	}
	

	public static void main(String[] args) throws IOException {
		NetHandler client = new NetHandler();
	}
}