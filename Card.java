public class Card{

	//suits in order of power
	int suit;
	char cSuit;
	//Jack 11 etc. Ace 1 or 14
	int value;
	char cValue;
	
	public Card(int tvalue, char tsuit){
		//set suit
		if(tsuit == 'C' || tsuit == 'c'){
			suit = 1;
			cSuit = 'C';
		}
		else if(tsuit == 'D' || tsuit == 'd'){
			suit = 2;
			cSuit = 'D';
		}
		else if(tsuit == 'H' || tsuit == 'h'){
			suit = 3;
			cSuit = 'H';
		}
		else if(tsuit == 'S' || tsuit == 's'){
			suit = 4;
			cSuit = 'S';
		}
		else{
			suit = 0;
			cSuit = 'O';
			System.out.println("not a valid suit");
		}
			
		//set value
		if(tvalue == 1 || tvalue == 14 || tvalue == 'A' || tvalue == 'a'){
			value = 14;
			cValue = 'A';
		}
		else if(tvalue <= 9 && tvalue > 0){
			value = tvalue;
			cValue = (char)('0' + tvalue);
		}
		else if(tvalue == 'T' || tvalue == 't' || tvalue == 10){
			value = 10;
			cValue = 'T';
		}
		else if(tvalue == 'J' || tvalue == 'j' || tvalue == 11){
			value = 11;
			cValue = 'J';
		}
		else if(tvalue == 'Q' || tvalue == 'q' || tvalue == 12){
			value = 12;
			cValue = 'Q';
		}
		else if(tvalue == 'K' || tvalue == 'k' || tvalue == 13){
			value = 13;
			cValue = 'K';
		}
		else{
			value = 0;
			cValue = 'O';
			System.out.println("not a valid value");
		}
	}
	
	//print the card
	void printCard(){
		System.out.print(cValue + "" + cSuit);
	}
	
	//Standard get statements
	int getValue(){
		return value;
	}
	
	char getCValue(){
		return cValue;
	}
	
	int getIntSuit(){
		return suit;
	}
	
	char getSuit(){
		return cSuit;
	}
	
}