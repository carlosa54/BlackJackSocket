package algoritms;
import java.util.Random;

public class Deck {

	private Card[] myCards;
	// Para saber cuantas cartas quedan en el deck
	private int numCards;
	
	public Deck(){
		this(1,false);
	}
	
	public Deck(int numDecks, boolean shuffle){
		// cada deck tiene por default 52 cartas
		this.numCards = numDecks * 52;
		this.myCards = new Card[this.numCards];
		
		// empezar las cartas en 0 para crearlas en el for
		int c= 0;
		for(int d= 0; d< numDecks; d++){
			
			for(int s=0; s < 4;s++){
				 
				for(int n= 1; n<=13; n++){
					
					//Para cada valor del suit le va a dar 13 numeros diferentes
					this.myCards[c] = new Card(Suit.values()[s],n);
					//incrementar variable c para seguir con la siguiente carta
					c++;
				}
			}
		}
		if (shuffle){
			this.shuffle();
		}
	}

	private void shuffle() {

		Random rnd = new Random();
		//temp para hacerle swap a las cartas en el array
		Card temp;
		int s;
		for(int i = 0; i < this.numCards; i++){
			//numero random entre 0 y 52
			s = rnd.nextInt(this.numCards);
			
			temp = this.myCards[i];
			this.myCards[i] = this.myCards[s];
			this.myCards[s] = temp;
			
		}
		
	}
	
	public Card nextCard(){
		Card tope = this.myCards[0];
		
		return tope;
	}
}
