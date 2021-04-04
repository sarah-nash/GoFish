import java.util.ArrayList;

public class Player {
    private ArrayList<Card> hand;
    private boolean isHuman;
    public String name;
    private int numPairs;
    private int numBooks;

    public Player(boolean isHuman, String name){
        this.isHuman = isHuman;
        this.hand = new ArrayList<>();
        this.name = name;
        numPairs = 0;
    }

    /**
     * Adds a Card to the player's hand.
     * @param newCard the Card which will be added.
     */
    public void addCard(Card newCard){
        hand.add(newCard);
    }

    /**
     * Gets the player's current hand.
     * @return hand (ArrayList<Card>) the Cards in a player's hand
     */
    public ArrayList<Card> getHand(){
        return hand;
    }

    /**
     * Increments the numPairs counter by 1.
     */
    public void addPair(){
        numPairs++;
    }

    /**
     * Increments the numBooks counter by 1.
     */
    public void addBook(){
        numBooks++;
    }

    /**
     * Returns the number of pairs in a player's hand.
     * @return numPairs (int) pairs in the player's hand.
     */
    public int getNumPairs(){
        return numPairs;
    }

    public int getNumBooks(){
        return numBooks;
    }

    /**
     * Used for determining whether the player is human, to display text to console
     * or when to use random selection for cards in the hand.
     * @return isHuman (boolean)
     */
    public boolean isHuman(){
        return isHuman;
    }

    /**
     * Prints the player's hand as neat text.
     * @param hand (ArrayList<Card>) The cards in the player's hand.
     */
    public static void printHand(ArrayList<Card> hand){
        Card card = hand.get(0);
        System.out.print("\t[" + Rank.rankToAbbrev(card.getValue()) + Suit.suitToChar(card.getSuit()) );
        for(int i=1; i<hand.size(); i++){
            card = hand.get(i);
            System.out.print( ", " + Rank.rankToAbbrev(card.getValue()) + Suit.suitToChar(card.getSuit()) );
        }
        System.out.print("]\n");
    }


}
