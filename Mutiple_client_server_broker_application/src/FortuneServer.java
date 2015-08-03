import java.net.*; 
import java.io.*; 

public class FortuneServer extends Thread
{ 
 protected static Socket clientSocket;
 private static BufferedReader input = new BufferedReader(new
         InputStreamReader(System.in));
 /*
 * INSTRUCTION : SERVER NEVER SHUT DOWN, IF THERE IS ANY KIND OF PROBLEM WITH CLIENT CONNECTION
 * FOR EXAMPLE: GOOGLE WILL NOT GO DOWN IF CONNECTION BETWEEN YOUR PC AND GOOGLE MACHINE WILL BREAK.
 * 
 */
 
 
 public static void main(String[] args) throws IOException 
   { 
    ServerSocket serverSocket = null; 
    System.out.println("Welcome, Fortune Cookie Server");
    System.out.println();
    
    //GET IP ADDRESS OF SERVER
    System.out.println("Do you want to register? ");
    if(!input.readLine().equalsIgnoreCase("y"))
    	System.exit(0);
  //STORES - BROKER IP ADDRESS
    System.out.println("Enter Broker IP: ");
    String ip = input.readLine();
    // BROKER PORT
    System.out.println("Enter Broker Port: ");
    int port = Integer.parseInt(input.readLine());
    System.out.println();
    
    System.out.println("Please Enter the IP address of this server: ");
    String ipaddress= input.readLine();
    //System.out.println("IP address of Fortune cookie server is:"+ipaddress);
    connect_server(ip, port, "server/fortune/"+ipaddress+"/10008");
    System.out.println("SUCCESS: SERVER REGISTERED");
    System.out.println();
    
    try { 
        
        //SERVER PORT
         serverSocket = new ServerSocket(10008);
         //String server_name= "fortune";
         //System.out.println ("Connection Socket Created");
         //String []  serverInfo = detail.split("/");
         try { 
              while (true)
                 {
            	  
            	  System.out.println("Do you want to unregister?");
                  if(input.readLine().equalsIgnoreCase("y"))
                	  break;
            	  
                     //SERVER STARTED
                  System.out.println ("Fortune Server Waiting for Connection");
                  // ACCEPTING REQUEST - FOR EACH CLIENT NEW THREAD
                  FortuneServer fortuneServer = new FortuneServer 
        (serverSocket.accept());
                  
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
         System.err.println("Could not listen on port: 10008."); 
         System.exit(1); 
        } 
    finally
        {
         try {
             //ERROR - SERVER SOCKET CLOSE 
        	 
             serverSocket.close();
             connect_server(ip, port, "server/fortune");
             System.out.println("SUCCESS: UNREGISTRED");
             }
         catch (IOException e)
             { 
              System.err.println("Could not close port: 10008."); 
              System.exit(1); 
             } 
        }
   }

 private FortuneServer (Socket clientSoc)
   {
       //CLIENT THREAD CREATED
    clientSocket = clientSoc;
    // FOR EACH CLIENT - DIFFERENT THREAD
    start();
   }

 @Override
 public void run()
   {
    //System.out.println ("New Communication Thread Started");

    try 
    { 
        BufferedReader in; 
        //OUTPUT STREAM 
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), 
                true)) 
        {
            in = new BufferedReader( 
                    new InputStreamReader( clientSocket.getInputStream()));
            String inputLine;
            //READ NUMBER OF FORTUNE COOKIES CLIENT WANTS
            while ((inputLine = in.readLine()) != null)
            {
                //System.out.println("Hello");
                if (inputLine.equals("Bye."))
                    break;
                int number = Integer.parseInt(inputLine);
                System.out.println ("Server: " + inputLine);
                // SENDS RANDOMLY GENERATED COOKIES
                out.println(new FortuneCookies().getCookies(number));
                
            }
        }
        //CLIENT CLOSE
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
		 //System.out.println("Broker socket is closed!");
    } 
    catch (IOException ex) 
    {
        System.err.println("ERROR: SERVER CANNOT BE CLOSED");
    }
    // return in.readLine();
 }
} 