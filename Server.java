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
		
		getAddress();
		
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
		
		Boolean isIpValid;
		Boolean isPortValid;
		
		do {
			ip = getIp();
		} while(!(isIpValid = validateIP(ip)));
		
		do {
			port = getPort();
		} while(!(isPortValid = validatePort(port)));

		return(isIpValid && isPortValid);
	}
	
	
	
	public static String getIp() 
	{
		String ip = "";
		System.out.println("Enter Ip ex :127.0.0.1") ;
		System.out.println("IP :");
		ip = scan.nextLine();	
		
		return ip;
	}
	public static Boolean validateIP(String ip) 
	{
		String[] octetIp = ip.split("[.]");
		if(octetIp.length != 4) {
			System.out.println("The IP address doesn't follow the format XX.XX.XX.XX" );
			return false;
		}
		for(int i = 0; i<4;i++)	
		{
			try 
			{
				int ipInt = Integer.parseInt(octetIp[i]);
				if (ipInt < 0 || ipInt> 255 ) 
				{
					System.out.println("The number "+ ipInt +" is not between 0 and 255" );
					return false;
				}
			}
			catch(Exception e) 
			{
				System.out.println("\n" + ip +" is not a valid IP");
				return false;
			}			
		}
		return true;
	}
	
	public static int getPort() 
	{
		System.out.println("Enter Port between 5000 and 5050 :") ;
		System.out.println("Port : ");
		String portInput  = scan.nextLine();
		int port = -150000;
		try 
		{
			port = Integer.parseInt(portInput);
		}
		catch(Exception e) 
		{
				System.out.println(portInput +" is not a valid input");
		}
		return port;
				
	}	
	
	public static Boolean validatePort(int port)
	{
		if (port == -150000) {
			return false;
		}
		
		if (port < 5000 || port > 5050 ) 
		{
			System.out.println("The number "+ port +" is not between 5000 and 5050" );
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
	
