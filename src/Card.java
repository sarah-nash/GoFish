public class Card {
    private Rank value;
    private Suit suit;

    public Card(Rank value, Suit suit){
        this.value = value;
        this.suit = suit;
    }

    public Rank getValue(){
        return value;
    }

    public Suit getSuit(){
        return suit;
    }


/*
    public static void main(String[] args) {
        Card aSpade = new Card(1, "Spade");
        System.out.println(aSpade.getSuit());
        System.out.println(aSpade.getValue());
    }
*/
}
