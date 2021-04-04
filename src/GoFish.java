/**
 * Compilation: javac GoFish.java
 * Execution: java GoFish
 *
 * This program allows the user to play GoFish against the computer.
 *
 * I actually programmed GoFish to be played by having players form pairs of cards
 * (because that's how I play GoFish) and saw afterwards that the Official rules play with books.
 * So I adjusted it to be played with either rule type :-)
 *
 * In the pairs gameplay, the current player can ask their opponent for a value in their hand,
 * but the opponent will only hand over at most 1 card with the same value.
 * Players will earn one point for every pair they make.
 *
 * In the books gameplay, the current player can ask their opponent for all cards of a certain value,
 * and the opponent will give everything they have for a max of 3 cards.
 * Players will earn one point for every book (4 of a kind) they make.
 *
 * Object Oriented Programming
 * Spring 2021
 * @author Sarah Nash
 * @author Dr. Caitrin Eaton
 *
 *
 */

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GoFish {

    private Scanner scan = new Scanner(System.in);
    private Random rand = new Random();
    public GameMode gameMode;


    /**
     * Actually runs the game of Go Fish.
     */
    public GoFish() {
        System.out.println(
                "Welcome to Go Fish!" +
                        "\nPlease enter your name: ");
        String name = scan.next();
        System.out.println("Welcome, " + name + "! "
                + "This game has two possible styles: Pairs and Books."
                + "\nThe goal of Pairs is to get as many pairs (2 of a kind) as possible, "
                + "while the goal of Books is to get as many books (4 of a kind) as possible. "
                + "\nWhich rules would you like to play by? (Pairs or Books):  ");

        String mode = scan.next();
        gameMode = chooseGameMode(mode);

        System.out.println("Excellent! Playing " + GameMode.modeToString(gameMode) + "...");

        Player human = new Player(true, name);
        Player computer = new Player(false, "Computer");
        Player[] players = {human, computer};

        Deck deck = new Deck();
        deck.shuffleDeck();

        //deal cards to the players

        deck.dealCards( players, 7);

        //and check everyone's hand for books or pairs
        if(gameMode.equals(GameMode.PAIRS)){
            System.out.println("\n\tChecking for pairs...");
            for (Player player : players) {
                checkPairs(player);
                System.out.println(player.name + " has " + player.getNumPairs() + " pairs total.");
            }
        }
        else{ //gameMode is Books...
            System.out.println("\n\tChecking for books...");
            for (Player player : players) {
                checkBooks(player);
                System.out.println(player.name + " has " + player.getNumBooks() + " books total.");
            }
        }


        //next, take turns until the game ends
        int turn = 0;
        while (!deck.isEmpty() || !players[0].getHand().isEmpty() || !players[1].getHand().isEmpty()) {
            Player player = players[turn%2];
            takeTurn(deck, player, players[(turn+1)%2]);
            turn++;
        }


        //once the deck and hands are empty, show the score
        if(deck.isEmpty() && players[0].getHand().isEmpty() && players[1].getHand().isEmpty()){
            Player winner = checkWin(players[0], players[1]);
            System.out.println("\nSince there are no more cards in play, the game is over!");
            System.out.println("\n=== F I N A L  S C O R E ===");
            if(winner==null){
                System.out.println("\nIt's a draw!");
            }
            else{
                System.out.println("\n" + winner.name + " is the winner of this match!");
            }

            if(gameMode == GameMode.PAIRS){
                System.out.println(
                        players[0].name + " had " + players[0].getNumPairs() + " pairs, " +
                                " while " +
                                players[1].name + " had " + players[1].getNumPairs() + " pairs. ");
            }
            else{
                System.out.println(
                        players[0].name + " had " + players[0].getNumBooks() + " books, " +
                                " while " +
                                players[1].name + " had " + players[1].getNumBooks() + " books.");
            }


        }
    }

    /**
     * Allows the player to take their turn until their hand is empty or the opponent does not have the card they want.
     * @param deck (ArrayList<Card>) the current cards in the deck.
     * @param player (Player) the current player
     * @param opponent (Player) the current player's opponent
     */
    public void takeTurn(Deck deck, Player player, Player opponent){
        Rank wantedRank;

        System.out.println("\n===" + player.name + "'s turn!===");

        if(player.getHand().isEmpty()){
            System.out.println("... but their hand is empty! Go fish!");
            goFish(deck, player);
        }

        else{
            if(player.isHuman()){ //let the player ask for a value in their hand
                System.out.println("Here's your hand: ");
                Player.printHand(player.getHand());
                wantedRank = chooseRank(player);
            }
            else{ //if the player is the computer, then randomly choose a value to ask for
                int pos = rand.nextInt(player.getHand().size());
                wantedRank = player.getHand().get(pos).getValue();
            }

            boolean haveAny = doYouHaveAny(player, opponent, wantedRank); //ask opponent for card(s) with that value

            if(haveAny){
                takeTurn(deck, player, opponent); //go again
            }
            else{
                goFish(deck, player);
            }
        }
    }

    /**
     * Allows the player to draw a card from the top of the deck, as long as it is not empty.
     * If the player is the user, the name of the card prints out.
     * @param deck (ArrayList<Card>) the current state of the deck
     * @param player (Player) the player drawing a card
     */
    public void goFish(Deck deck, Player player) {
        ArrayList<Card> hand = player.getHand();

        if (deck.isEmpty()) {
            System.out.println("....but the deck is empty!");
        }
        else { //else, draw from the deck
            Card topCard = deck.getTopCard(); //but, don't add it to the hand yet

            if (player.isHuman()) { //if human, tell them what card was drawn
                System.out.println(player.name + " drew the " + topCard.toString() + " from the deck. ");
            }

            boolean pairMade = false;
            if (gameMode.equals(GameMode.PAIRS)) { //check just the last card against the rest for a pair
                for (Card card : hand) {
                    Rank value = card.getValue();
                    if (value.equals(topCard.getValue())) {
                        player.addPair();
                        System.out.println(
                                player.name + " made a pair of " + Rank.rankToString(value)
                                        + "s, and now has " + player.getNumPairs() + " total pairs! ");
                        hand.remove(card);
                        pairMade = true;
                        break;
                    }
                }
                if (!pairMade) {
                    hand.add(topCard);
                }
            }

            else { //check just the value of the card for books
                int numSameValue = 0; //remember that the topCard isn't in the hand yet
                for (Card card : hand) {
                    if (card.getValue().equals(topCard.getValue())) {
                        numSameValue++;
                    }
                }

                if (numSameValue == 3) { //if a book is made, remove cards from hand and dont give the top card
                    player.addBook();
                    System.out.println(
                            player.name + " made a book of " + Rank.rankToString(topCard.getValue())
                            + "'s, and now has " + player.getNumBooks() + " total books!");
                    removeCards(player, topCard.getValue(), gameMode); //then remove them from the deck
                }
                else{ //if they didn't make a book, give them the card
                    hand.add(topCard);
                }
            }
        }
    }

    /**
     * Allows the current player to ask their opponent for cards of a certain value.
     * Returns a boolean to indicate whether the current player was successful in obtaining a card they want.
     * @param current (Player) the current player
     * @param opponent (Player) the current player's opponent
     * @param targetRank (Rank) the value being asked for
     * @return (boolean) true if the opponent gave over at least one card, false otherwise.
     */
    public boolean doYouHaveAny(Player current,Player opponent, Rank targetRank){

        ArrayList<Card> opponentCards = removeCards(opponent, targetRank, gameMode);
        System.out.println(current.name + " asks " + opponent.name
                + " if they have any " + Rank.rankToString(targetRank) + "s.");

        if(!opponentCards.isEmpty()){ //if the opponent gives the player at least one card
            if(gameMode.equals(GameMode.PAIRS)){
                Card opponentCard = opponentCards.get(0);
                current.addPair();
                removeCards(current, targetRank, gameMode);

                System.out.println("Yes! " + opponent.name + " hands over the " + opponentCard.toString() + ".");
                System.out.println(
                        current.name +  " made a pair of " + Rank.rankToString(targetRank) +
                                "s, and now has " + current.getNumPairs() + " total pairs. Go again!");
            }

            else{ //looking for Books
                System.out.println("Success!");
                for(Card card:opponentCards){
                    current.addCard(card);
                    System.out.println(opponent.name + " hands over the " + card.toString() + ".");
                }

                //now, check the current player's hand to see if that was enough for a book
                boolean bookFound = checkBooks(current);

                if(bookFound){
                    current.addBook();
                    removeCards(current, targetRank, gameMode);
                    System.out.println(
                            current.name +  " made a books of " + Rank.rankToString(targetRank) +
                                    "s, and now has " + current.getNumBooks() + " total books. Go again!");
                }
                else{
                    System.out.println("It's not enough for a book, but you may still go again!");
                }
            }
            return true;
        }
        else{ //the opponent didn't have any cards of that value
            System.out.println("No " + Rank.rankToString(targetRank) + "'s. Go Fish!");
            return false;
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

    /**
     * Lets the player ask the opponent for cards of a certain value, ensures that value is in their hand,
     * and returns the value as a Rank.
     * @param player (Player) the current player
     * @return (Rank) value: the value the player asks for.
     */
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

    /**
     * Lets the player choose to play by forming pairs or forming books.
     * @param mode the player's requested mode to play in
     * @return (GameMode) gameMode: either pairs or books rules
     */
    public GameMode chooseGameMode(String mode){
        GameMode gameMode;
        if (mode.toLowerCase().equals("pair") || mode.toLowerCase().equals("pairs")){
            gameMode = GameMode.PAIRS;
        }
        else if (mode.toLowerCase().equals("book") || mode.toLowerCase().equals("books")){
            gameMode = GameMode.BOOKS;
        }
        else{
            System.out.println("Not a valid choice! Try 'pairs' or 'books'");
            mode = scan.next();
            gameMode = chooseGameMode(mode);
        }
        return gameMode;
    }

    /**
     * Checks the player's hand for pairs in general, prints the pair that was found, and removes cards from the player's hand.
     * @param player (Player)
     * @return
     */
    public boolean checkPairs(Player player){
        ArrayList<Card> hand = player.getHand();

        boolean foundPair = false;
        for (int i=0; i<hand.size()-1; i++){
            Card card1 = hand.get(i);
            for (int j=i+1; j<hand.size(); j++){
                Card card2 = hand.get(j);

                if (card1.getValue().equals(card2.getValue())) {
                    hand.remove(j);
                    hand.remove(i);
                    i--;

                    player.addPair();
                    System.out.println(player.name + " found a pair: " +
                            "\n\t" + card1.toString() + " and "  + card2.toString()); //prints the pair found
                    foundPair=true;
                    break; //only looks for one pair, not all cards with same value

                }

            }
        }
        return foundPair;
    }

    /**
     * Searches the player's hand for a book.
     * @param player (Player) the current player
     * @return (boolean) true if a book was found, false otherwise.
     */
    public boolean checkBooks(Player player){
        ArrayList<Card> hand = player.getHand();
        int numSameValue;

        if(hand.size()>=4){
            for(int i=0; i<hand.size(); i++){
                numSameValue=0;

                Rank value1 = hand.get(i).getValue();
                for(int j=i+1; j<hand.size(); j++){
                    Rank value2 = hand.get(j).getValue();

                    if(value1.equals(value2)){
                        numSameValue++;
                    }
                }

                if (numSameValue==3){ //3, since we are not including the initial card
                    removeCards(player,value1, GameMode.BOOKS);
                    return true;
                }
            }
        }
        return false; //else no books found

    }


    /**
     * Removes all cards of a certain value from the player's hand if the gameMode is books.
     * Will remove only one card if the gameMode is pairs.
     * @param player (Player) the current player
     * @param value (Rank) the value to be removed
     * @param gameMode (GameMode)
     * @return (ArrayList<Card>) removedCards: the cards removed from the current player's hand.
     */
    public ArrayList<Card> removeCards(Player player, Rank value, GameMode gameMode){
        ArrayList<Card> hand = player.getHand();
        ArrayList<Card> removedCards= new ArrayList<>();
        for (Card card:hand){
            if(card.getValue().equals(value)){
                removedCards.add(card);
                if (gameMode.equals(GameMode.PAIRS)) { //only return that one value
                    hand.remove(card);
                    return removedCards;
                }
            }
        }
        for(Card card:removedCards){
            hand.remove(card);
        }
        return removedCards;
    }

    /**
     * Checks how many pairs or books each player made, and returns the winner of the game.
     * @param player1 (Player) the first Player
     * @param player2 (Player) the second Player
     * @return (Player) returns the winning Player, or null if it is tie.
     */
    public Player checkWin(Player player1, Player player2){
        int score1,score2;

        if(gameMode == GameMode.PAIRS){
            score1 = player1.getNumPairs();
            score2 = player2.getNumPairs();
        }
        else{ //gameMode == GameMode.BOOKS
            score1 = player1.getNumBooks();
            score2 = player2.getNumBooks();
        }

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
        new GoFish(); //play GoFish
    }

}
