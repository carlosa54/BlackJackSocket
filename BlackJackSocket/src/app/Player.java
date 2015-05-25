package app;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class Player extends JFrame 
{
	public static void main( String[] args )
	   {
	      Player application = new Player("localhost"); 

	      

	      application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	      application.runClient(); // run client application
	   }
	private JButton Hit;
	private JButton Stay;
	private JPanel buttons;
	private JTextArea displayArea; // display information to user
	private ObjectOutputStream output; // output stream to server
	private ObjectInputStream input; // input stream from server
	private String message = ""; // message from server
	private String chatServer; // host server for this application
	private Socket client; // socket to communicate with server
	// initialize chatServer and set up GUI
	public Player( String host )
	{
		super( "Player" );

		chatServer = host; // set server to which this client connects
		
		buttons = new JPanel();
		buttons.setLayout(new GridLayout(1,2));
		Hit = new JButton("Hit");
		Stay = new JButton("Stay");
		
		Hit.addActionListener(
				new ActionListener() 
				{
					//Si presiona hit le envia el mensaje al server
					public void actionPerformed( ActionEvent event )
					{
						sendData( "h" );
					} 
				} 
				); 
		
		Stay.addActionListener(
				new ActionListener() 
				{
					//Si presiona stand le envia el mensaje al server
					public void actionPerformed( ActionEvent event )
					{
						sendData( "s" );
					} 
				} 
				); // end call to addActionListener

		buttons.add(Hit, BorderLayout.SOUTH);
		buttons.add(Stay, BorderLayout.SOUTH);
		buttons.setVisible(true);
		add(buttons,BorderLayout.SOUTH);
		displayArea = new JTextArea(); // create displayArea
		add( new JScrollPane( displayArea ), BorderLayout.CENTER );

		setSize( 300, 300 ); 
		setVisible( true ); 
	} // end Client constructor

	
	public void runClient() 
	{
		try 
		{
			connectToServer(); 
			getStreams(); 
			processConnection();
		} // end try
		catch ( EOFException eofException ) 
		{
			displayMessage( "\nClient terminated connection" );
		} // end catch
		catch ( IOException ioException ) 
		{} // end catch
		finally 
		{
			closeConnection(); // close connection
		} // end finally
	} // end method runClient

	// connect to server
	private void connectToServer() throws IOException
	{      
		displayMessage( "Attempting connection\n" );

		//Crea el socket
		client = new Socket( InetAddress.getByName( chatServer ), 4444 );

		
		displayMessage( "Connected to: " + 
				client.getInetAddress().getHostName() );
	} // end method connectToServer

	
	private void getStreams() throws IOException
	{
		
		output = new ObjectOutputStream( client.getOutputStream() );      
		output.flush(); // flush output buffer to send header information

		
		input = new ObjectInputStream( client.getInputStream() );

	} // end method getStreams

	
	private void processConnection() throws IOException
	{
		String text = JOptionPane.showInputDialog("Enter username");
		sendData(text);

		do 
		{ 
			try
			{
				message = ( String ) input.readObject(); // read new message
				displayMessage( "\n" + message ); // display message
				if (message.compareToIgnoreCase("Busted:") == 0 || message.compareToIgnoreCase("Wait") == 0){
					buttons.setVisible(false);				
				}
				
			} // end try
			catch ( ClassNotFoundException classNotFoundException ) 
			{
				displayMessage( "\nUnknown object type received" );
			} // end catch

		} while ( !message.equals( "SERVER>>> TERMINATE" ) );
	} // end method processConnection

	// Cierra la coneccion
	private void closeConnection() 
	{
		displayMessage( "\nClosing connection" );
		

		try 
		{
			output.close(); 
			input.close(); 
			client.close(); 
		} // end try
		catch ( IOException ioException ) 
		{} // end catch
	} // end method closeConnection

	// Envia mensaje al server
	private void sendData( String message )
	{
		try 
		{
			output.writeObject(  message );
			output.flush(); // flush data
			
		} 
		catch ( IOException ioException )
		{
			displayArea.append( "\nError writing object" );
		} // end catch
	} 

	//Metodo que escribe en el displayArea
	private void displayMessage( final String messageToDisplay )
	{
		SwingUtilities.invokeLater(
				new Runnable()
				{
					public void run() // updates displayArea
					{
						displayArea.append( messageToDisplay );
					} 
				}  
				); 
	} // end displayMessage


	

	
}//end player class
