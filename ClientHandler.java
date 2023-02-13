import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientHandler extends Thread { // Pour traiter la demande de chaque client sur un socket particulier
	
	private Socket socket;
	private int clientNumber;
	private DataOutputStream out;
	private DataInputStream in;
	private String currentDirectory= "";
	
	public ClientHandler(Socket socket, int clientNumber)
	{
		this.socket = socket;
		this.clientNumber = clientNumber;
		System.out.println("New connection with client #" + clientNumber + " at "+ socket);
		currentDirectory = System.getProperty("user.dir");
	}
	
	public void run()
	{
		try 
		{
			System.out.print("Entered run function");
			DataInputStream in = new DataInputStream(socket.getInputStream());	
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());// cr�ation du canal d'envoi
			out.writeUTF("Hello from server - you are client #"+ clientNumber);// envoi de message
			readCommand(in.readUTF().toString());
		}
		catch(Exception e) 
		{
			System.out.println("Error handling client#"+ clientNumber + ":" + e);
		}
		finally 
		{
			try 
			{
				socket.close();
			}
			catch(IOException e)
			{
				System.out.println("Couldn't close the socket");
			}
			System.out.println("\nConnection with client #" + clientNumber + " closed");
		}
	}
	private static String getTime()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd @ HH:mm:ss");
		return formatter.format(new Date());
	}

	private void readCommand(String command) throws Exception
	{	
		String[] commands = command.split(" ",2);
		
		System.out.print("["+socket.getInetAddress()+ ":" +socket.getLocalPort()+ ":" + socket.getPort()+getTime() +"] "+command); //lequel use entre local et port
		
		switch (commands[0]) // need to write the function for the commands
		{ 
		case "cd":
			cd(commands[1]);
			break;
			
		case"ls":
			ls(commands[1]);
			break;
			
		case "mkdir":
			mkdir(commands[1]);
			break;
			
		case "upload":
			upload(commands[1]);
			break;
			
		case "download":
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
	private void exit() throws Exception
	{		
		socket.close();
		System.out.print("Vous avez été déconnecté avec succès.");
		//in.close(); cause des erreurs
		//out.close(); cause des erreurs
	}
	private void mkdir(String commands) throws Exception // a test
	{
		File directory = new File(currentDirectory + "\\" + commands);
		if( directory.exists()) 
		{
			 throw new Exception ("Directory already exists");	 
		}
		else 
		{
			directory.mkdirs();
			out.writeUTF("Folder " + commands + "has been created");
			out.flush();
		}
	}
	private void download(String commands) 
	{
		
	}
	private void upload(String commands) 
	{
		
	}
	private void cd(String commands) 
	{
		
	}
	private void ls(String commands) 
	{
		
	}
}
