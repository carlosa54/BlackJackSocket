package app;


import java.io.*;
import java.net.*;
import java.util.*;

public class GameMain {
	public static void main(String[] args) throws Exception {
		GameMain client = new GameMain();
		client.run();
		
	}

	private void run() throws Exception {
		
		Scanner kb = new Scanner(System.in);
		
		try{
		@SuppressWarnings("resource")
		Socket Sock = new Socket("localhost",4444);
		ObjectOutputStream printS = new ObjectOutputStream(Sock.getOutputStream());
		System.out.println("Enter your username:");
		printS.writeObject(kb.next());
		
		ObjectInputStream inputR = new ObjectInputStream(Sock.getInputStream());
		String message;
		while(true){
		message = (String) inputR.readObject();
		System.out.println(message);
		if(message.equalsIgnoreCase("Q")){
			System.out.println("entro");
			printS.writeObject(kb.next());
		}
		}
		}catch(Exception ex){
			System.out.println("Cannot connect to Server. Try again!");
			}
		kb.close();
	}

}
