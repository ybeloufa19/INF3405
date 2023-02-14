import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	private static Socket socket;
	private static String ip = "";
	private static int port = 0;
	private static DataInputStream in;
	private static DataOutputStream out;
	private static String currentDirectory= "";
	private static Scanner scan = new Scanner(System.in);
	
	public static void main(String[] args ) throws Exception 
	{
		// Adresse et port du serveur
		String serverAddress = "";
		int serverPort = 0;
		currentDirectory = System.getProperty("user.dir");
		
		while(!getAddress());
		serverAddress = ip;
		serverPort = port;

		
		// Création d'une nouvelle connexion avec le serveur
		socket = new Socket(serverAddress,serverPort);
		System.out.format("Serveur lancé sur [%s:%d] \n", serverAddress,serverPort);
		
		// Création d'un canal entrant pour recevoir les messages envoyés par le serveur
		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
		
		// Attente de la réception d'un mesage envoyé par le serveur sur le canal
		String helloMessageFromServer = in.readUTF();
		System.out.println(helloMessageFromServer);
		
		while(true){			
			String command = "";
			command = scan.nextLine();
			out.writeUTF(command);
			
			readCommand(command, out);			
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
	
	private static void readCommand(String command, DataOutputStream out) throws Exception
	{	
		String[] commands = command.split(" ",2);
		
		
		switch (commands[0]) // need to write the function for the commands
		{ 
		case "cd":
			//cd(commands[1]);
			break;
			
		case"ls":
			//ls(commands[1]);
			break;
			
		case "mkdir":
			String mkdirConfirmation = in.readUTF();
			System.out.println(mkdirConfirmation);
			break;
			
		case "upload":
			upload(commands[1]);
			break;
			
		case "download":
			//download(commands[1]);
			break;
			
		case "exit":
			exit();
			break;
			
			default:
				out.writeUTF("Unknown command : " + commands[0]);
				out.flush();
				break;
		}
	}
	private static void exit() throws Exception
	{
		System.out.print("\nVous avez été déconnecté avec succès.");
		socket.close();
		in.close();
		out.close();
		System.exit(0);
		
	}
	
	private static void upload(String filename) throws Exception
	{
		File inputFile = new File(currentDirectory + "\\" + filename);
		long fileLength = inputFile.length();
		out.writeLong(fileLength);
		
		FileInputStream inputStream = new FileInputStream(inputFile);
		byte[] buffer = new byte[4096];
		int bytesNumber;
		while((bytesNumber = inputStream.read(buffer)) > 0){
			out.write(buffer,0,bytesNumber);			
		}
		out.flush();

		inputStream.close();
		System.out.print("\nInput stream closed");
		
		String uploadConfirmation = in.readUTF();
		System.out.println(uploadConfirmation);
		
	}
	
}

