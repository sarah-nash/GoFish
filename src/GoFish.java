import java.lang.reflect.Array;
import java.sql.SQLOutput;
import java.util.*;

public class GoFish {
    private final ArrayList<Card> deck;
    private Scanner scan = new Scanner(System.in);
    private Random rand = new Random();
    public GameMode gameMode;

    public GoFish() {
        System.out.println(
                "Welcome to Go Fish!" +
                        "\nPlease enter your name: ");
        String name = scan.next();
        System.out.println("Welcome, " + name + "! Dealing cards now...");

        Player human = new Player(true, name);
        Player computer = new Player(false, "Computer");
        Player[] players = {human, computer};

        deck = Deck.makeDeck();
        Deck.shuffleDeck(deck);

        //deal the cards, and let each player check their hand for pairs
        Deck.dealCards(deck, players, 7);
        System.out.println("\n\tChecking for pairs...");
        for (Player player : players) {
            checkPairs(player);
            System.out.println(player.name + " has " + player.getNumPairs() + " pairs total.");
        }

       //play until everyone runs out of cards
        int turn = 0;
        while (!deck.isEmpty() || !players[0].getHand().isEmpty() || !players[1].getHand().isEmpty()) {
            Player player = players[turn%2];
            takeTurn(deck, player, players[(turn+1)%2]);
            turn++;
        }

        //once the deck and hands are empty, show the score
        if(deck.isEmpty() && players[0].getHand().isEmpty() && players[1].getHand().isEmpty()){
            Player winner = checkWin(players[0], players[1]);
            if(winner==null){
                System.out.println("\nIt's a draw!");
            }
            else{
                System.out.println("\n" + winner.name + " is the winner of this match!");
            }

            System.out.println(
                    players[0].name + " had " + players[0].getNumPairs() + " pairs, " +
                    " while " +
                    players[1].name + " had " + players[1].getNumPairs() + " pairs, ");


        }
    }

    public boolean checkPairs(Player player){
//        ArrayList<Card> handCopy = new ArrayList<>(hand);
        ArrayList<Card> hand = player.getHand();
//        int numPairs=0;

        boolean foundPair = false;
        for (int i=0; i<hand.size()-1; i++){
            for (int j=i+1; j<hand.size(); j++){
                Rank value1 = hand.get(i).getValue();
                Rank value2 = hand.get(j).getValue();
                if (value1.equals(value2)) {
                    Suit suit1 = hand.get(i).getSuit();
                    Suit suit2 = hand.get(j).getSuit();
                    hand.remove(j);
                    hand.remove(i);
                    i--;

                    player.addPair();
                    System.out.println(player.name + " found a pair: ");
                    System.out.println("\t" + Rank.rankToString(value1) + " of " + Suit.suitToString(suit1)+ " and "  + Rank.rankToString(value2) + " of " + Suit.suitToString(suit2));
                    foundPair=true;
                    break;

                }

            }
        }
        return foundPair;
    }

    public Rank chooseRank(Player player){
        ArrayList<Card> hand = player.getHand();
        System.out.println("What value would you like to ask your opponent for? Choose 2-10, J,Q,K, or A: ");
        String value = scan.next();
        Rank wantedRank = getRank(value);

        //check that the card is in the hand
        for(Card card:hand){
           if (card.getValue().equals(wantedRank)) {
               return wantedRank;
           }
        }
        System.out.println("You must pick a value already in your hand!");
        return chooseRank(player);

    }

    public boolean doYouHaveAny(Player current,Player opponent, Rank targetRank){
        Card opponentCard = removeCard(opponent, targetRank);
        System.out.println(current.name + " asks " + opponent.name + " if they have any " + Rank.rankToString(targetRank) + "s.");

        if( opponentCard != null){
            current.addPair();
            removeCard(current, targetRank);
            Suit suit = opponentCard.getSuit();
            System.out.println("Yes! " + opponent.name + " hands over the " +
                    Rank.rankToString(targetRank) + " of " + Suit.suitToString(suit) + ".");
            System.out.println(
                    current.name +  " made a pair of " + Rank.rankToString(targetRank) +
                            "s, and now has " + current.getNumPairs() + " total pairs. Go again!");
            return true;
        }
        else{
            System.out.println("No " + Rank.rankToString(targetRank) + "'s. Go Fish!");
            return false;
        }
    }

     public Card removeCard(Player player, Rank value){
        ArrayList<Card> hand = player.getHand();
        for (Card card:hand){
            if(card.getValue().equals(value)){
                hand.remove(card);
                return card;
            }
        }
        return null;
     }

    public void goFish(ArrayList<Card> deck, Player player) {
        ArrayList<Card> hand = player.getHand();
        if (deck.isEmpty()) {
            System.out.println("....but the deck is empty!");
        } else {
            Card topCard = deck.get(0);
            boolean pairMade = false;

            if (player.isHuman()) {
                System.out.println(player.name + " drew the " +
                        Rank.rankToString(topCard.getValue()) + " of " + Suit.suitToString(topCard.getSuit())
                        + " from the deck. ");
            }
            deck.remove(0);

            for (Card card : hand) {
                Rank value = card.getValue();
                if (value.equals(topCard.getValue())) {
                    player.addPair();
                    System.out.println(player.name + " made a pair of " + Rank.rankToString(value) + "s, and now has " + player.getNumPairs() + " total pairs! ");
                    hand.remove(card);
                    pairMade = true;
                    break;
                }
            }
            if (!pairMade) {
                hand.add(topCard);
            }
        }
    }

    public void takeTurn(ArrayList<Card> deck, Player player, Player opponent){
        Rank wantedRank;

        System.out.println("\n\t" + player.name + "'s turn!");
        if(player.getHand().isEmpty()){
            System.out.println("... but their hand is empty! Go fish!");
            goFish(deck, player);
        }

        else{
            if(player.isHuman()){
                System.out.println("Here's your hand: ");
                Player.printHand(player.getHand());
                wantedRank = chooseRank(player);
            }
            else{
                int pos = rand.nextInt(player.getHand().size());
                wantedRank = player.getHand().get(pos).getValue();
            }
            boolean haveAny = doYouHaveAny(player, opponent, wantedRank);

            if(haveAny){
                takeTurn(deck, player, opponent);
            }
            else{
                goFish(deck, player);
            }
        }
    }

    /**
     * Translates player input from String to Rank
     * @param rank (String) the player's chosen input rank from the terminal
     * @return (Rank) value: the value the player asks for
     */
    public Rank getRank(String rank){
        Rank value;
        switch (rank.toLowerCase()) {
            case "2":
                value = Rank.TWO;
                break;
            case "3":
                value = Rank.THREE;
                break;
            case "4":
                value = Rank.FOUR;
                break;
            case "5":
                value = Rank.FIVE;
                break;
            case "6":
                value = Rank.SIX;
                break;
            case "7":
                value = Rank.SEVEN;
                break;
            case "8":
                value = Rank.EIGHT;
                break;
            case "9":
                value = Rank.NINE;
                break;
            case "10":
                value = Rank.TEN;
                break;
            case "j":
                value = Rank.JACK;
                break;
            case "q":
                value = Rank.QUEEN;
                break;
            case "k":
                value = Rank.KING;
                break;
            case "a":
                value = Rank.ACE;
                break;
            default:
                System.out.println("Not a valid choice! Try 2-10, J, Q, K, or A");
                String newValue = scan.next();
                value = getRank(newValue);
                break;
        }
        return value;
    }

    public Player checkWin(Player player1, Player player2){
        int score1 = player1.getNumPairs();
        int score2 = player2.getNumPairs();
        if (score1>score2){
            return  player1;
        }
        else if (score1<score2){
            return player2;
        }
        else{
            return null;
        }
    }

    public static void main(String[] args) {
         new GoFish();

    }



}
