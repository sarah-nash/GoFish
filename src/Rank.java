/**
 *
 * This class is based off of Orcale's Rank and Rank3 classes.
 * Use in conjunction with Suit enum and Deck class to create a deck of Cards.
 */

public enum Rank {
    ACE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    JACK,
    QUEEN,
    KING;

    private Rank() {
    }

    //for pretty formatting in the terminal
    public static String rankToString(Rank rank){
        String value = switch (rank) {
            case ACE -> "Ace";
            case TWO -> "2";
            case THREE -> "3";
            case FOUR -> "4";
            case FIVE -> "5";
            case SIX -> "6";
            case SEVEN -> "7";
            case EIGHT -> "8";
            case NINE -> "9";
            case TEN -> "10";
            case JACK -> "Jack";
            case QUEEN -> "Queen";
            case KING -> "King";
        };
        return value;
    }

    //for pretty printing out hands
    public static String rankToAbbrev(Rank rank){
        String value = switch (rank) {
            case ACE -> "A";
            case TWO -> "2";
            case THREE -> "3";
            case FOUR -> "4";
            case FIVE -> "5";
            case SIX -> "6";
            case SEVEN -> "7";
            case EIGHT -> "8";
            case NINE -> "9";
            case TEN -> "10";
            case JACK -> "J";
            case QUEEN -> "Q";
            case KING -> "K";
        };
        return value;
    }



}
