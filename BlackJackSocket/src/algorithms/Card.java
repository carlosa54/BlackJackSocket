package algorithms;

public class Card {

	private Suit mySuit;
	// A = 1, J = 11, Q= 12, K=13
	private int myNumber;
	
	public Card(Suit suit,int num){
		this.mySuit = suit;
		this.myNumber = num;
	}
	
	public int getNumber(){
		return myNumber;
	}
	
	public String toString(){
		switch(myNumber){
		case 1:
			return  "A of " + mySuit;
		case 11:
			return  "J of " + mySuit;
		case 12:
			return  "Q of " + mySuit;
		case 13:
			return  "K of " + mySuit;
		default:
			return myNumber + " of " + mySuit;
		}
			
	}
}
