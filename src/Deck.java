import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    public Deck(){
    }

    /**
     * Generated a deck with all Suits and Values in the enum's.
     * @return deck (ArrayList<Card>) the newly constructed deck.
     */
    public static ArrayList<Card> makeDeck(){
        ArrayList<Card> deck = new ArrayList<>();
        for(Suit suit: Suit.values()){
            for (Rank rank: Rank.values()){
                Card newCard = new Card(rank, suit);
                deck.add(newCard);
            }
        }
        return deck;
    }

    /**
     * Randomly shuffles the deck.
     * @param deck (ArrayList<Card>) the pre-shuffled deck.
     */
    public static void shuffleDeck(ArrayList<Card> deck){
        Collections.shuffle(deck);
//        System.out.println("\tSHUFFLED DECK: ");
//        for (Card card:deck){
//            System.out.println(card.getValue() + " of " + card.getSuit());
//        }
    }

    /**
     * Gives each player (int, suggested 5-7) a starting hand.
     * @param deck (ArrayList<Card>) the deck of 52 cards
     * @param players (Player[]) the players receiving cards
     * @param numCards (int) the number of cards each player will receive
     */
    public static void dealCards(ArrayList<Card> deck, Player[] players, int numCards){
        for(int i=0; i<numCards; i++){
            for(Player player:players){

                Card newCard = deck.get(0);
                if(player.isHuman()){
                    System.out.println("\t" + player.name + " receives the " +
                            Rank.rankToString(newCard.getValue()) + " of " + Suit.suitToString(newCard.getSuit()) + ".");
                }
                player.addCard(newCard); //give each player a starting hand
                deck.remove(0); //and remove the card from the deck
            }
        }
    }

}
