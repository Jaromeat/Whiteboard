package Final;



import java.io.IOException;
import java.net.ServerSocket;

import java.util.ArrayList;


public class Server {
	 private static boolean running = true;

	    public static ArrayList<User> userlist;
	    
	    /**
	     * stops server
	     */
	    public static synchronized void stop() { 
	        if(!running) return;
	        running = false;
	    }

	    public static void main(String[] args) {

	        try {
	        	
	            ServerSocket ss = new ServerSocket(50001);

	            userlist = new ArrayList<User>();
	           
	            User user;
	            
	            System.out.println("Server Start");
	            
	            while(running) {			//assigns any new connection a user class and runs the user's start method
	            	user = new User(ss.accept());

	            	userlist.add(user);
	            	
	            	user.start();
	            	
	            }
	           
	        } catch(IOException e) {
	            e.printStackTrace();
	            }
	    }
	}
