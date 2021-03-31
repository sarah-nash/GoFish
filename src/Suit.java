public enum Suit {
    HEARTS,
    DIAMONDS,
    SPADES,
    CLUBS;

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
