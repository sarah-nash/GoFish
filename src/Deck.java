/**
 *
 * This class is based off of Orcale's Deck and Deck3 classes.
 * Use in conjunction with Rank and Suit enums to create a deck of Cards.
 */

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    public static int numSuits = 4;
    public static int numRanks = 13;
    private ArrayList<Card> cards;

    public Deck(){
        this.cards = new ArrayList<>();
        for(Suit suit: Suit.values()){
            for (Rank rank: Rank.values()){
                Card newCard = new Card(rank, suit);
                cards.add(newCard);
            }
        }
    }

    /**
     * Gets the top card and removes it from the deck.
     * @return (Card) topCard the first card in the deck.
     */
    public Card getTopCard(){
        Card topCard = cards.get(0);
        cards.remove(0);
        return topCard;
    }

    /**
     * Randomly shuffles the deck.
     */
    public void shuffleDeck( ){
        Collections.shuffle(this.cards);
//        System.out.println("\tSHUFFLED DECK: ");
//        for (Card card:deck){
//            System.out.println(card.getValue() + " of " + card.getSuit());
//        }
    }

    /**
     * Gives each player (int, suggested 5-7) a starting hand.
     * @param players (Player[]) the players receiving cards
     * @param numCards (int) the number of cards each player will receive
     */
    public void dealCards( Player[] players, int numCards){
        System.out.println("Dealing cards...");
        for(int i=0; i<numCards; i++){
            for(Player player:players){

                Card newCard = cards.get(0);
                if(player.isHuman()){
                    System.out.println("\t" + player.name + " receives the " +
                            Rank.rankToString(newCard.getValue()) + " of " + Suit.suitToString(newCard.getSuit()) + ".");
                }
                player.addCard(newCard); //give each player a starting hand
                cards.remove(0); //and remove the card from the deck
            }
        }
    }

    public boolean isEmpty(){
        return cards.isEmpty();
    }


}
