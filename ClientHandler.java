import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
			this.in = new DataInputStream(socket.getInputStream());	
			this.out = new DataOutputStream(socket.getOutputStream());// cr�ation du canal d'envoi
			out.writeUTF("Hello from server - you are client #"+ clientNumber);// envoi de message
			while(true) {				
				readCommand(in.readUTF().toString());
			}
		}
		catch(Exception e) 
		{
			System.out.println("\nError handling client#"+ clientNumber + ": " + e);
		}
		finally 
		{
			try 
			{
				socket.close();
			}
			catch(IOException e)
			{
				System.out.println("\nCouldn't close the socket");
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
		
		System.out.print("["+socket.getInetAddress()+":" + socket.getPort()+" - "+getTime() +"] "+command +"\n");
		
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
			
			default:
				out.writeUTF("Unknown command : " + commands[0]);
				out.flush();
				break;
		}
	}
	
	private void mkdir(String fileName) throws Exception
	{
		File directory = new File(currentDirectory + "\\" + fileName);
		if( directory.exists()) 
		{
			out.writeUTF("Directory already exists");	 
		}
		else 
		{
			directory.mkdirs();
			out.writeUTF("Le dossier " + fileName + " a été créé."); 
			out.flush();
		}
	}
	private void download(String filename) throws Exception 
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
		Thread.sleep(200);
		System.out.print("\nInput stream closed");
		out.writeUTF("\nLe fichier " + filename + " à bien été téléchargé.");		
	}
	
	private void upload(String filename) throws Exception
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
		}
		
		outputStream.close();
		out.writeUTF("\nLe fichier " + filename + " à bien été téléversé.");		
	}
	private void cd(String commands) 
	{
		
	}
	private void ls(String commands) 
	{
		
	}
}
