package algoritms;

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
		return myNumber + " of " + mySuit;
	}
}
