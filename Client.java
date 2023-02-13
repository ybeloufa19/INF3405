import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	private static Socket socket;
	private static String ip = "";
	private static int port = 0;
	private static Scanner scan = new Scanner(System.in);
	public static void main(String[] args ) throws Exception 
	{
		// Adresse et port du serveur
		String serverAddress = "";
		int serverPort = 0;
		
		while(!getAddress());
		serverAddress =ip;
		serverPort = port;

		
		// Création d'une nouvelle connexion avec le serveur
		socket = new Socket(serverAddress,serverPort);
		System.out.format("Serveur lancé sur [%s:%d]", serverAddress,serverPort);
		
		// Création d'un canal entrant pour recevoir les messages envoyés par le serveur
		DataInputStream in = new DataInputStream(socket.getInputStream());
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		
		// Attente de la réception d'un mesage envoyé par le serveur sur le canal
		String helloMessageFromServer = in.readUTF();
		System.out.println(helloMessageFromServer);
		
		//************************************************************************ A REECRIRE
		while(true){			
			String command = "";
			command = scan.nextLine();			
			out.writeUTF(command);
			
			String[] commands = command.split(" ");
			/*if (commands[0] == "exit") 
				System.exit(0);
			/*while(in.available() != 0){
				String serverComs = in.readUTF();
				if (serverComs.isEmpty()) break;
				System.out.println(serverComs);
			}*/
		}
		//*************************************************************************
		
		// fermeture de la connexion avec le serveur
		//socket.close();
	}
	public static Boolean getAddress() 
	{
		System.out.println("What is your address ?");
		ip = getIp();
		port = getPort();
		
		Boolean isIpValid = validateIP(ip);
		Boolean isPortValid = validatePort(port);

		return(isIpValid && isPortValid);
	}
	
	
	
	public static String getIp() 
	{
		String ip = "";
		int octet ;
		System.out.println("Enter Ip ex :127.0.0.1") ;
		System.out.println("IP :");
		ip = scan.nextLine();
		String[] input = ip.split("[.]",4+1);
		
		for(int i = 0; i<4;i++)	
		{	
			try 
			{
				 octet = Byte.parseByte(input[i]);
				
			}
			catch(Exception e) 
			{
				System.out.println(" You have entered " +input[i] +" that is not a valid number");
			}
		}
		
		return ip;
	}
	public static Boolean validateIP(String ip) 
	{
		String[] octetIp = ip.split("[.]",4+1);
		for(int i = 0; i<4;i++)	
		{
			int ipInt = Integer.parseInt(octetIp[i]);
		
			if (ipInt < 0 || ipInt> 255 ) 
			{
				System.out.println(" the number "+ ipInt +" is not between 0 and 255" );
				return false;
			}
		}
		return true;
	}
	
	public static int getPort() 
	{
		int port = 0;
		System.out.println("Enter Port between 5000 and 5050 :") ;
		System.out.println("Port : ");
		String portInput  = scan.nextLine();
		
				try 
				{
					port = Integer.parseInt(portInput);
				}
				catch(Exception e) 
				{

						System.out.println(port +" must be a valid number");

				}
				
		return port;
	}	
	
	public static Boolean validatePort(int port)
	{
		if (port < 5000 || port > 5050 ) 
		{
			System.out.println(" the number "+ port +" is not between 5000 and 5050" );
			return false;
		}
		return true;
	}
	
}

