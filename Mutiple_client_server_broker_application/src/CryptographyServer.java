import java.net.*; 
import java.io.*; 

public class CryptographyServer extends Thread
{	
	
	private static BufferedReader input = new BufferedReader(new
        InputStreamReader(System.in)); 
	//Socket to register and unregister this server
	protected Socket clientSocket;
	
/**
 * FOR ENCRYPTION DECRYPTION METHOD - PLEASE READ CAESER CIPHER 
 * @param args
 * @throws IOException 
 */
 public static void main(String[] args) throws IOException 
   { 
    ServerSocket serverSocket = null; 
    //STORES - BROKER IP ADDRESS
    System.out.println("Enter Broker IP: ");
    String ip = input.readLine();
    // BROKER PORT
    System.out.println("Enter Broker Port: ");
    int port = Integer.parseInt(input.readLine());
    //GET IP ADDRESS OF SERVER
    System.out.println();
    System.out.println("Please enter the ip of this server: ");
    String ipaddress= input.readLine();
    //System.out.println("IP address of Fortune cookie server is:"+ipaddress);
    connect_server(ip, port, "server/cryptographic/"+ipaddress+"/10005");
    System.out.println();
    System.out.println("SUCCESS: REGISTERED");
    //String []  serverInfo = detail.split("/");
    try { 
        //SERVER SOCKET
         serverSocket = new ServerSocket(10005); 
         //System.out.println ("Connection Socket Created");
       
         
         try { 
              while (true)
                 {
            	  System.out.println("Do you want to unregister?");
                  if(input.readLine().equalsIgnoreCase("y"))
                	  break;
            	  
                  System.out.println ("Cryptography server waiting for Connection");
                  new CryptographyServer (serverSocket.accept()); 
                 }
             } 
         catch (IOException e) 
             { 
              System.err.println("Accept failed."); 
              System.exit(1); 
             } 
        } 
    catch (IOException e) 
        { 
         System.err.println("Could not listen on port: 10005."); 
         System.exit(1); 
        } 
    finally
        {
    	 
    	
         try {
        	
              serverSocket.close();
              connect_server(ip, port, "server/cryptographic");
              System.out.println("SUCCESS: UNREGISTERED");
              //System.out.println("Server is closed");
             }
     
         catch (IOException e)
             { 
              System.err.println("Could not close port: 10005."); 
              System.exit(1); 
             }
         
        }
   }

 private CryptographyServer (Socket clientSoc)
   {
    clientSocket = clientSoc;
    start();
   }

 @Override
 public void run()
   {
    //System.out.println ("New Communication Thread Started");

    try { 
         PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), 
                                      true); 
         BufferedReader in = new BufferedReader( 
                 new InputStreamReader( clientSocket.getInputStream())); 

         String inputLine; 
         CaeserCipher cc = new CaeserCipher();
         while ((inputLine = in.readLine()) != null) 
             {
                 if (inputLine.equals("Bye.")) 
                  break; 
                String output = "";
                String [] s = inputLine.split("/");
                if(s[0].equalsIgnoreCase("encrypt"))
                {
                    output = cc.encrypt(s[1]);
                }
                else 
                {
                    output = cc.decrypt(s[1]);
                }
                
                //INPUT AT SERVER
              System.out.println ("Server: " + inputLine); 
              out.println(output); 
              
             } 

         out.close(); 
         in.close(); 
         clientSocket.close(); 
        } 
    catch (IOException e) 
        { 
         System.err.println("Problem with Communication Server");
         System.exit(1); 
        } 
    }
//FUNCTION - TO CONNECT TO BROKER
public static void connect_server(String ip, int port, String str) 
        throws IOException
{
    String serverHostname = new String (ip);
     System.out.println ("Attemping to connect to host " +
            serverHostname + " on port "+port+".");

    Socket echoSocket = null;
    PrintWriter out = null;
    BufferedReader in = null;

    try 
    {
        //CONNECTION TO SERVER
        echoSocket = new Socket(serverHostname, port);
        //OUTPUT STREAM
        out = new PrintWriter(echoSocket.getOutputStream(), true);
        
        in = new BufferedReader(new InputStreamReader(
                                    echoSocket.getInputStream()));
    } catch (UnknownHostException e) {
        System.err.println("Don't know about host: " + serverHostname);
        System.exit(1);
    } catch (IOException e) {
        System.err.println("Couldn't get I/O for "
                           + "the connection to: " + serverHostname);
        System.exit(1);
    }
        
	    out.println(str);
        
	    //System.out.println("Output: " + in.readLine());

	out.close();
	in.close();
	//stdIn.close();
	 try 
     {
		 echoSocket.close();
     } 
     catch (IOException ex) 
     {
         System.err.println("Socket cannot be closed");
     }
     
	
    //return in.readLine();
}
} 