package Final;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class User extends Thread { 
    DataInputStream in;
    DataOutputStream out;
    BufferedReader bufferIn;
    ArrayList<User> selectUserlist;
    static int idOffset = 0;
    int id;
    

    Socket socket;
    /**
     * 1-arg constructor
     * starts a stream and sets the socket
     * @param socket
     */
    public User(Socket socket) {
        this.socket = socket;

        startStream(); 
    }
    /**
     * sets user id and opens an input and output stream
     */
    public void startStream() { //inits streams after socket is initiated (through ss.accept())
        try {
        	id = idOffset;
        	idOffset++;
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            
        }catch(IOException e) { e.printStackTrace(); }
    }
    /**
     * closes all streams and sockets
     * @throws IOException
     */
    public void closeStream() throws IOException { //throws exception which is caught in run()
            out.close(); 
            in.close();
            socket.close();
            Server.userlist.remove(this);
    }
    /**
     * sends a string across output stream
     * @param string
     */
    public void sendMessage(String string) {
        try {
            out.writeUTF(string + "\n");
        }catch(IOException e) { e.printStackTrace(); }
    }
    
    /**
     * senda a string to all users except this one
     * @param string
     */
    public void sendAll(String string) {
    	try {
    		selectUserlist = Server.userlist;
    		
    		for (int i = 0; i<selectUserlist.size(); i++) {	//removes current user
    			if( selectUserlist.get(i).id == this.id)
    				selectUserlist.remove(i);
    		}
            
            
    		for(int i = 0; i < selectUserlist.size(); i++)	//sends to everyone else
    		selectUserlist.get(i).out.writeUTF(string + "\n");
    		
    		System.out.println(string);
    		
    	} catch(IOException e) { e.printStackTrace(); }
    }
   
    /**
     * when thread starts, accept/handle data
     */
    public void run() { 
        String input;
        
        try {
        	
        	
        	System.out.println("User " + this.id + " has connected");
            while((input = in.readUTF()) != null) {
               
               	sendAll(input);		//sends input out to all other users
               
            }
            closeStream();
        } catch(IOException e) { 
        	
        	System.out.println("User " + this.id + " has disconnected");
        }
    }
}