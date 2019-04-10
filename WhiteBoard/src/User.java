package src;

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
    public User(Socket socket) {
        this.socket = socket;

        startStream(); 
    }

    public void startStream() { //inits streams after socket is initiated (through ss.accept())
        try {
        	id = idOffset;
        	idOffset++;
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            
        }catch(IOException e) { e.printStackTrace(); }
    }

    public void closeStream() throws IOException { //throws exception which is caught in run()
            out.close(); 
            in.close();
            socket.close();
    }

    public void sendMessage(String string) {
        try {
            out.writeUTF(string + "\n");
        }catch(IOException e) { e.printStackTrace(); }
    }
    public void sendAll(String string) {
    	try {
    		selectUserlist = Server.userlist;
    		
    		for (int i = 0; i<selectUserlist.size(); i++) {
    			if( selectUserlist.get(i).id == this.id)
    				selectUserlist.remove(i);
    		}
            
            
    		for(int i = 0; i < selectUserlist.size(); i++)
    		selectUserlist.get(i).out.writeUTF(string + "\n");
    		
    		System.out.println(string);
    		
    	} catch(IOException e) { e.printStackTrace(); }
    }
   
    public void run() { //when thread starts, accept/handle data
        String input;
        
        try {
        	
        	
        	
            while((input = in.readUTF()) != null) {
                if(input.compareTo("{quit}") == 0) { //TODO: change to a close button
                	closeStream();
                	
                	
                }
                else {
                	sendAll(input);
                	
                }
            }
            closeStream();
        } catch(IOException e) { 
        	e.printStackTrace();
        }
    }
}