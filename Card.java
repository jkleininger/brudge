import javax.swing.ImageIcon;

public class Card {

    int  suit,  value;
    char cSuit, cValue;

    

    public Card(int tvalue, char tsuit){
        tsuit = Character.toUpperCase(tsuit);
        cSuit = tsuit;

        switch(tsuit) {
            case 'C': suit=1; break;
            case 'D': suit=2; break;
            case 'H': suit=3; break;
            case 'S': suit=4; break;
            default:
                suit=0;
                cSuit='O';
                System.out.println("Invalid suit specified");
                break;
        }

        value = tvalue;
        if(value==1) { value = 14; }
        if(value<=9) {
            cValue = (char)('0'+value);
        } else {
            switch(value) {
                case 10: cValue='T'; break;
                case 11: cValue='J'; break;
                case 12: cValue='Q'; break;
                case 13: cValue='K'; break;
                case 14: cValue='A'; break;
                default: System.out.println("Invalid value"); break;
            }
        }
    }

    void printCard()  { System.out.print(cValue + "" + cSuit); }

    int  getValue()   { return value;  }
    char getCValue()  { return cValue; }
    int  getIntSuit() { return suit;   }
    char getSuit()    { return cSuit;  }

}
