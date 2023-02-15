import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
	
	private static void readCommand(String command, DataOutputStream out) throws Exception
	{	
		String[] commands = command.split(" ",2);
		
		
		switch (commands[0]) // need to write the function for the commands
		{ 
		case "cd":
			//cd(commands[1]);
			if(commands.length < 2) {
				System.out.println("Il manque 1 argument pour la commande cd");
				break;
			}
			break;
			
		case"ls":
			//ls(commands[1]);
			break;
			
		case "mkdir":
			if(commands.length < 2) {
				System.out.println("Il manque 1 argument pour la commande mkdir");
				break;
			}
			String mkdirConfirmation = in.readUTF();
			System.out.println(mkdirConfirmation);
			break;
			
		case "upload":
			if(commands.length < 2) {
				System.out.println("Il manque 1 argument pour la commande upload");
				break;
			}
			upload(commands[1]);
			break;
			
		case "download":
			if(commands.length < 2) {
				System.out.println("Il manque 1 argument pour la commande download");
				break;
			}
			download(commands[1]);
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

		inputStream.close();
		
		String uploadConfirmation = in.readUTF();
		System.out.println(uploadConfirmation);
		
	}
	private static void download(String filename) throws Exception
	{
		File outputFile = new File(currentDirectory + "\\" + filename);
		FileOutputStream outputStream = new FileOutputStream(outputFile);
		final int bufferLength = 4096;
		byte[] buffer = new byte[bufferLength];
		int bytesNumber;
		long fileLength = in.readLong();
		
		while(fileLength > 0) {
			bytesNumber = in.read(buffer);
			outputStream.write(buffer,0,bytesNumber);
			fileLength = fileLength - bufferLength;
			System.out.print("\n"+fileLength);
		}
		
		outputStream.close();
		System.out.print("\nOutput stream closed");
		String uploadConfirmation = in.readUTF();
		System.out.println(uploadConfirmation);		
	}
}

