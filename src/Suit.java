/**
 *
 * This class is based off of Orcale's Suit and Suit3 classes.
 * Use in conjunction with Rank enum and Deck class to create a deck of Cards.
 *
 */

public enum Suit {
    HEARTS,
    DIAMONDS,
    SPADES,
    CLUBS;

    //for pretty printing in the terminal
    public static String suitToString(Suit suit){
        String suitName =
        switch (suit){
            case HEARTS -> "Hearts";
            case DIAMONDS -> "Diamonds";
            case SPADES -> "Spades";
            case CLUBS -> "Clubs";
        };
        return suitName;
    }

    //for pretty printing out hands
    public static char suitToChar(Suit suit){
        char suitChar = switch (suit){
            case HEARTS -> 'H';
            case DIAMONDS -> 'D';
            case SPADES -> 'S';
            case CLUBS -> 'C';
        };
        return suitChar;
    }
}
