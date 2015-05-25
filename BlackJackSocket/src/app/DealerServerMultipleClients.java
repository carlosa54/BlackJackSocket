package app;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import algorithms.Deck;
import algorithms.Player;

public class DealerServerMultipleClients {
	public static void main(String[] args) throws Exception {
		
		DealerServerMultipleClients server = new DealerServerMultipleClients();
		server.runServer();
		
		
	}
	
	Player dealer = new Player("Dealer");
	Deck elDeck = new Deck(6, true);
	ArrayList<Player> users = new ArrayList<Player>();
	int count = 1; // cuenta las conecciones
	SockServer[] sockServer = new SockServer[ 10 ];
	ServerSocket ServSock;
	ExecutorService executor = Executors.newFixedThreadPool(10);
	int playersleft = 3;
	
	public void runServer() throws IOException{
		
		
		
		try{
		
			ServSock = new ServerSocket(4444);
			
			while(true){
				try{
			sockServer[count] = new SockServer(count);
			// Espera y acepta las conecciones de cada cliente
			sockServer[count].waitForConnection();
			// launch that server object into its own new thread
			executor.execute(sockServer[ count ]);
			// then, continue to create another object and wait (loop)
				}catch (Exception ex){
					
				}finally 
				{
					++count;
				} // end finally
			}
			
		}catch(Exception ex){
			System.err.println("Server error");
		}
		
		
	}
	
	private class SockServer implements Runnable{
		
		private Socket socket; // connection to client
		private ObjectOutputStream printS; // output stream to client
		private ObjectInputStream inputR; // input stream from client
		private int myConID;
		public SockServer(int counterIn)
		{
			myConID = counterIn;
		}
		
		public void run() {
		
			try{
				
				
				printS = new ObjectOutputStream(socket.getOutputStream());
				printS.flush();
				inputR = new ObjectInputStream(socket.getInputStream());
				
				String name = (String) inputR.readObject();
				users.add(new Player(name));
				System.out.println(users.get(myConID-1) + " has entered the lobby.");
				
				if(users.size() == 3){
					System.out.println("Game will start");
				
					dealCards();
				}// end if users == 3
				processConnections();
				
				
				/*Para repartir las cartas a cada usuario pero falta el dealer
				 * for(int i = 0; i <= users.size();i++){
					users.get(i).addCard(elDeck.nextCard());
				}*/
				
				/*Por si quiero salir del loop dependiendo de los usuarios en el server 
				 * if(users.size() == 3){
					System.out.println("Game will start");
					everyoneInLobby = true;
				}*/
				
			}catch(Exception ex){
				System.out.println("Error in SockServer class");
				System.exit(0);
			} finally{
				closeConnection();
			}
			
			
		}// run method
		
		private void closeConnection() {

			 
			try {
				printS.close();
				inputR.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}

		/**
		 * Procesa cada conections y busca si dieron hit o stand
		 * @throws IOException 
		 * 
		 */
		private void processConnections() throws IOException {
			String message = null;
			try{
			do{
				
				message = ( String ) inputR.readObject();
				if(message.compareToIgnoreCase("h") == 0){
					
					hit();
				}
				if(message.compareToIgnoreCase("s") == 0){
					this.sendMessage("Wait");
					playersleft--;
					check();
			}
			}while(true);
			}catch(Exception ex){
				
			}
		}

		

		private void check() throws IOException {
			if(playersleft == 0){
				goDealer();
			}
		}

		/**
		 * Anade una carta al que solicite
		 * @throws IOException 
		 */
		private void hit() throws IOException {
			users.get(myConID-1).addCard(elDeck.nextCard());
			sockServer[myConID].sendMessage(users.get(myConID-1).printHand(true));
			
			if(users.get(myConID-1).getHandSum() > 21){
				sockServer[myConID].sendMessage(users.get(myConID-1).printHand(true));
				sockServer[myConID].sendMessage("Busted:");
				sockServer[myConID].sendMessage("You have busted with "+ users.get(myConID-1).getHandSum() +". Dealer wins");
				playersleft--;
			}
			if(playersleft == 0){
					goDealer();
				}
		}
/*
		private void hitQuestion() throws IOException {
			for(int i=1;i<=3;i++){
			sockServer[i].sendMessage("You have "+ users.get(i-1).getHandSum()+ " Would you like to Hit or Stand? (Enter H or S)");
			sockServer[i].sendMessage("q");
			ans = inputR.toString(); 	
			sockServer[i].sendMessage(ans);
			if(ans.compareToIgnoreCase("H") == 0){
				
			}else{
				meDone = true;
				countEveryone++;
			}
			}
		}*/

		private void goDealer() throws IOException {
			boolean dealerDone = false;
			
			
			while(!dealerDone){
					
			if(dealer.getHandSum() < 17){
				dealer.addCard(elDeck.nextCard());
				
				for (int i=1;i< count;i++){
				sockServer[i].sendMessage("The Dealer hits\n");
				sockServer[i].sendMessage(dealer.printHand(true));
				}
				}
				//Si el dealer se paso de 21
				 if(dealer.getHandSum() > 21){
					dealerDone = true;
			} if(dealer.getHandSum()>17 && dealer.getHandSum()<=21 ){
				for (int i=1;i< count;i++){
					if(users.get(i-1).getHandSum() <=21){
				sockServer[i].sendMessage(dealer.printHand(true));
				sockServer[i].sendMessage("The dealer stays\n");
				}
				}
				dealerDone = true;
			}
			
			
			}
			
			resultados();
			
		}

		private void resultados() throws IOException {
			
			int dealerSum = dealer.getHandSum();
			for (int i=1;i< count;i++) {
				
			int mySum = users.get(i-1).getHandSum();
			if(!(mySum >21)){
			if(dealer.getHandSum() > 21){
					if(users.get(i-1).getHandSum() <=21)	
					sockServer[i].sendMessage("Dealer busted with "+ dealer.getHandSum()+" "+ users.get(i-1).toString() +" won");
			}
			else if (mySum > dealerSum && mySum <=21){
				sockServer[i].sendMessage("You win!");
			}else if( mySum == dealerSum){
				sockServer[i].sendMessage("Push");
			}else{
				sockServer[i].sendMessage("Dealer wins with " + dealer.getHandSum());
			}
			}
			}
		}

		private void waitForConnection() throws IOException
		{

			System.out.println( "Waiting for connection " + myConID + "\n" );
			socket = ServSock.accept(); //  Acepta la coneccion de los clientes            
			System.out.println( "Connection " + myConID + " received from: " +
					socket.getInetAddress().getHostName() );
		}
		
		private void sendMessage( String message) throws IOException{
			printS.writeObject(message);
		}
		
		private void dealCards() throws IOException{
			
			//repartir las cartas
			for(int d = 0; d< 2;d++){
				for(int p=0;p<users.size();p++){
					users.get(p).addCard(elDeck.nextCard());
				}
				dealer.addCard(elDeck.nextCard());
			}
			for(int i =1; i<=users.size();i++){
				sockServer[i].sendMessage(dealer.printHand(false));
				sockServer[i].sendMessage(users.get(i-1).printHand(true));
				//Para salir de la mano si el usuario tiene 21
				if(users.get(i-1).getHandSum() == 21){
					sockServer[i].sendMessage("BlackJack! "+ users.get(i-1).toString() +" won");
					playersleft--;
				}
			}
		}
		
		
	}
	
	
	
	
}
