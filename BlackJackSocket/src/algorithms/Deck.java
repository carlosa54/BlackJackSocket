package algorithms;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Carlos
 *
 */
public class Deck {

	private ArrayList<Card> myCards = new ArrayList<>();
	private int numCards;	// Para saber cuantas cartas quedan en el deck
	
	public Deck(){
		this(1,false);
	}
	
	public Deck(int numDecks, boolean shuffle){
		// cada deck tiene por default 52 cartas
		this.numCards = numDecks * 52;
		
		// empezar las cartas en 0 para crearlas en el for
		int c= 0;
		for(int d= 0; d< numDecks; d++){
			
			for(int s=0; s < 4;s++){
				 
				for(int n= 1; n<=13; n++){
					
					//Para cada valor del suit le va a dar 13 numeros diferentes
					this.myCards.add(c,new Card(Suit.values()[s],n));
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
			
			temp = this.myCards.get(i);
			this.myCards.set(i, this.myCards.get(s));
			this.myCards.set(s, temp);			
		}
		
	}
	
	/**
	 * @return the card on top of the deck
	 */
	public Card nextCard(){		
		return this.myCards.remove(0);
	}
	
	public void printDeck(int numToPrint){
		
		for(int c = 0; c<numToPrint;c++){
			System.out.printf("% 3d/%d %s \n",c+1,this.numCards,this.myCards.get(c).toString());
		}
		System.out.printf("\t\t[%d other] \n", this.numCards - numToPrint);
	}
}
