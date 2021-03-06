import java.util.ArrayList;

public class Hand {

    ArrayList<Card> card;

	int HCP;
	int[] numSuit;
	int[] HCPSuit;

	//tells you what high cards a suit has by adding adjusted values to a single int
	//J=1,Q=2,K=4,A=8, so a 13 means you have a JKA
	int[] HCSuit;

	//says what the highest card in each suit is
	int[] highestSuit;

	//initialize
	public Hand(){
        card = new ArrayList<Card>(0);
		HCP = 0;
		//making num suit count to 4 was just more convenient
		numSuit     = new int[5];
		HCPSuit     = new int[5];
		HCSuit      = new int[5];
		highestSuit = new int[5];
		for(int count = 0; count < numSuit.length; count++){
			numSuit[count]     = 0;
			HCPSuit[count]     = 0;
			HCSuit[count]      = 0;
			highestSuit[count] = 0;
		}
	}

  void addCard(Card newCard){
    card.add(newCard);
    processCard(newCard);
  }

  //HCP,numSuit,HCPSuit,HCSuit
  //Maintains data when card is added
  void processCard(Card newCard){
    int rank    = newCard.getRank();
    int suit    = newCard.getIntSuit();
    int adjRank = 1;
    int goesTo  = 0;

    numSuit[suit]++;

    if(rank > 10){
      HCP += (rank-10);
      HCPSuit[suit] += (rank-10);

      adjRank=adjRank<<(rank-11);

      HCSuit[suit] += adjRank;
    }

    //updates biggest card in each suit
    if(rank > highestSuit[suit]) { highestSuit[suit] = rank; }
  }

  int  getNumberOfCards()         { return card.size();       }
  Card getCard(int c)             { return card.get(c);       }
  int  getHCP()                   { return HCP;               }
  int  getNumberOfSuit(int suit)  { return numSuit[suit];     }
  int  getHCPInSuit(int suit)     { return HCPSuit[suit];     }
  int  getHCsInSuit(int suit)     { return HCSuit[suit];      }
  int  getHighestInSuit(int suit) { return highestSuit[suit]; }
  ArrayList<Card> getHand()       { return card;              }
  void removeCard(Card c)         { card.remove(c);           }

}
