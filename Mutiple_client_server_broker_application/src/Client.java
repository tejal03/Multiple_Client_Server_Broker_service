import java.io.*;
import java.net.*;

public class Client 
{
    private static BufferedReader input = new BufferedReader(new
         InputStreamReader(System.in));
    public static void main(String[] args) throws IOException 
    {
        System.out.println("Welcome, Client");
        
        //STORES - BROKER IP ADDRESS
        System.out.println("Enter Broker IP: ");
        String ip = input.readLine();
        // BROKER PORT
        System.out.println("Enter Broker Port: ");
        int port = Integer.parseInt(input.readLine());
        System.out.println();
        while(true)
        {
            
            System.out.println("Enter Name of server you want to connect: ");
            //STORES SERVERNAME
            String server_name = input.readLine();
            //GET THE INFORAMATION OF PARTICULAR SERVER FROM THE BROKER
            //IF SERVER DOES NOT FOUND BROKER WIL THROW EXCEPTION
            //System.out.println("check point 1");
            String detail = connect_server(ip, port, "client/"+server_name);
            if(detail == null)
            {
            	System.err.println("ERROR: SERVER DOES NOT EXIST");
                continue;
            }
            	
            //System.out.println("check point 2");
            String []  serverInfo = detail.split("/");
            System.out.println("Server port:"+serverInfo[0]);
            System.out.println("Server ip:"+serverInfo[1]);
            while(true)
            {
                
                //RUNS FORTUNE SERVER
                if(server_name.equalsIgnoreCase("fortune"))
                {
                    System.out.println("How many cookies do you want? : ");
                    String cookie_count = input.readLine();
                    String s = connect_server(serverInfo[1], Integer.parseInt(serverInfo[0]), cookie_count);
                    System.out.println("Output: "+s);
                }
                
                //RUNS CRYPTOGRAPHIC SERVER
                else if(server_name.equalsIgnoreCase("cryptographic"))
                {
                    System.out.println("What do you want to do? Encrypt Or "
                            + "Decrypt");
                    String choice = input.readLine();
                    System.out.println("Please Enter the String : ");
                    String input_string = choice +"/"+input.readLine();
                    String output = connect_server(serverInfo[1], Integer.parseInt(serverInfo[0]), input_string);
                    System.out.println("Output: "+output);
                }
                //INVALID CHOICE THEN BREAK THE LOOP
                else
                    break;
                //WANT TO CONTINUE WITH THE SAME SERVER?
                System.out.println("Do you want to continue with this server? ");
                if(!input.readLine().equalsIgnoreCase("Y"))
                    break;
                
            }
            //WANT TO CONTINUE WITH OTHER SERVER?
            System.out.println("Do you want to continue? ");
            if(!input.readLine().equalsIgnoreCase("Y"))
            break;
            
            
        }
        input.close();
        
        //CLIENT SOCKET CLOSED
        
    }
    //FUNCTION - TO CONNECT TO BROKER OR EITHER SERVER
    public static String connect_server(String ip, int port, String str) 
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
            //System.out.println("Hello");
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
	    String s = in.readLine();  
	    //System.out.println("Output: " + in.readLine());
	    //System.out.println("Output: " + s);
	out.close();
	in.close();
	//stdIn.close();
	
	try 
    {
		echoSocket.close();
		// System.out.println("socket is closed!");
    } 
    catch (IOException ex) 
    {
        System.err.println("ERROR: SOCKET CANNOT BE CLOSED");
    }
        return s;
        
    }
}
