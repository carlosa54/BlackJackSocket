package algorithms;

import java.util.ArrayList;

/**
 * @author Carlos
 *
 */
public class Player {

	

	private String name;
	
	private ArrayList<Card> hand = new ArrayList<Card>();
	
	private int numCards;
	
	public Player(String aName){
		this.name = aName;
		this.emptyHand();
	}

	/**
	 * Borra la mano de cartas que tenga el jugador
	 */
	private void emptyHand() {
		hand.clear();
	}
	
	public boolean addCard(Card aCard){
		this.hand.add(this.numCards,aCard);
		this.numCards++;
		return (this.getHandSum() <= 21);
	}
	public int getHandSum() {
		
		int handSum = 0;
		int cardNum;
		int numAces= 0;
		
		for(int c = 0; c<this.numCards;c++){
			//da el numero de carta 
			cardNum = this.hand.get(c).getNumber();
			if(cardNum == 1){ // si es una A
				numAces++;
				handSum +=11;
			}else if(cardNum > 10){ //si es JQK
				handSum += 10;
			}else{
				handSum +=cardNum;
			}
		}
		// si hay A y el total es mayor de 21 la A se convierte en 1
		while (handSum > 21 && numAces > 0){
			handSum -= 10;
			numAces--;
		}
		return handSum;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	
	public String printHand(boolean showFirstCard){
		String name = this.name + "'s cards:";
		
		for(int c= 0; c< this.numCards;c++){
			if(c==0 && !showFirstCard){
				 name += "[hidden]";
			}else{
				name += " " +this.hand.get(c).toString();
			}
			
		}
		return name + "\n";
	}
	
}
