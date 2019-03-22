import java.io.IOException;
	import java.io.ObjectOutputStream;
	import java.net.Socket;
	import java.util.Scanner;
	
public class TestDrawingClient2 {
	

	/**
	 *
	 * @author Avery Munoz
	 */
	
	    public static void main(String[] args) throws IOException {
	        Socket echoSocket = new Socket("10.200.178.137", 6688);
	ObjectOutputStream out = new ObjectOutputStream(echoSocket.getOutputStream());
	        int[] xycords = new int[4];
	        boolean run = true;
	        Scanner input = new Scanner(System.in);
	        
	        while(run)
	        {
	        xycords[0] = 400;
	        xycords[1] = 400;
	        xycords[2] = -400;
	        xycords[3] = -400;
	              
	        out.writeObject(xycords);
	        }
	        

	        
	    
	    
	}
}
