import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Date;

public class Server {
	
	private static ServerSocket Listener;
	private static String ip = "";
	private static int port = 0;
	// Application Serveur
	private static Scanner scan = new Scanner(System.in);
	public static void main(String[] args) throws Exception
	{
		// Compteur incr�ment� � chaque connexion d'un client au serveur
		int clientNumber = 0;
		
		// Adresse et port du serveur
		String serverAddress = "";
		int serverPort = 0;
		
		while(!getAddress());
		
		serverAddress =ip;
		serverPort = port;

		// Cr�ation de la connexion pour communiquer avec les clients
		Listener = new ServerSocket();
		Listener.setReuseAddress(true);
		
		InetAddress serverIP = InetAddress.getByName(serverAddress);
		
		// Association de l'adresse et du port � la connexion
		Listener.bind(new InetSocketAddress(serverIP,serverPort));
		
		System.out.println(serverAddress +":"+ serverPort + "-" + getTime() );
		
		try 
		{
			// � chaque fois qu'un nouveau client se connecte on execute la contion run de l'objet ClientHandler
			
			while(true)
			{
				// Important la fonction accept() est bloquante: attend qu'un prochain client se connecte
				// Une nouvelle connection : incr�mente le compteur clientNumber
				new ClientHandler(Listener.accept(),clientNumber++).start();
				
			}
			
		}
		finally 
		{
			// Fermeture de la connexion
			Listener.close();
		}
		
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
				 octet = Integer.parseInt(input[i]);
				
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
	private static String getTime()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd @ HH:mm:ss");
		return formatter.format(new Date());
	}

	}
	
