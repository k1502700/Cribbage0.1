package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Game {

    boolean playing = true;
    boolean outOfCards = false;
    boolean gameOver = false;
    int gameNum = 0;
    String name = "Dennis";
    boolean humanGame = false;
    public Hand winner = new Hand("DummyHand", this);

    public Game(int num, Launcher launcher){
        this.humanGame = launcher.human;
        if (humanGame){
            System.out.println("Enter your name");
            InputStream streamer = System.in;
            InputStreamReader sr = new InputStreamReader(streamer);
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            try {
                name = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        gameNum = num;
        String p1AI;
        if (humanGame){
            p1AI = "Human";
        }
        else{
            p1AI = launcher.ai1;
        }
        String p2AI = launcher.ai2;

        if((gameNum%2)==0){
            hand2 = new Hand("John", p2AI, this);
            hand1 = new Hand(name, p1AI, this);
        }
        else{
            hand2 = new Hand("John", p2AI, this);
            hand1 = new Hand(name, p1AI, this);

        }

        players = new ArrayList<>();
        players.add(hand1);
        players.add(hand2);

        dealer = hand1;
        nonDealer = hand2;

        int i = 1;
        while (!gameOver){
            cribbageRound(i++);
        }
        launcher.length += i;
    }


    public Game(boolean dudGame){
        //Do nothing, Use with care
    }

    Evaluator e = new Evaluator("Cribbage");

    Deck deckManager;
    Deck discardManager;
    ArrayList<Deck> dicardPiles = new ArrayList<Deck>();
    Hand cribManager = new Hand("Crib", this);
    ArrayList<Card> deck;
    Hand hand1;
    Hand hand2;
    Hand hand3;
    Hand hand4;
    public Hand dealer;
    Hand nonDealer;

    ArrayList<Hand> players = new ArrayList<>();


    public void initialize(){
        deckManager = new Deck();
        deck = deckManager.shuffleDeck();
        discardManager = new Deck(false);
        //System.out.println(deck);

//        hand1 = new Hand("Preacher");
//        hand2 = new Hand("Dennis");
        //hand3 = new Hand("Tulip");
        //hand4 = new Hand("Cassidy");

//        players = new ArrayList<>();
//        players.add(hand1);
//        players.add(hand2);
        //players.add(hand3);
        //players.add(hand4);

//        dealer = hand1;
//        nonDealer = hand2;
    }



    public void cribbageRound(int round) {
        System.out.println("Game: " + gameNum + " = Round: " + round + " ==========");
        Game:

        initialize();
        dealToAll(6);
//        hand1.addCard(new Card("2","S"));
//        hand1.addCard(new Card("3","S"));
//        hand1.addCard(new Card("3","H"));
//        hand1.addCard(new Card("4","S"));

//        for (int i = 0; i < 4; i++){
//            dealTo(hand2);
//            dealTo(cribManager);
//        }

        System.out.println("Discarding cards:");
        System.out.println(players);
//        for (Hand player : players) {
//            cribManager.addCard(
//                    player.makeMove(this, "DiscardMoveX"));
//            cribManager.addCard(
//                    player.makeMove(this, "DiscardMoveX"));
//        }
        System.out.println("The crib belongs to " + dealer + " this round");
        cribManager.addCard(
                hand1.makeMove(this, "DiscardMove"));
        cribManager.addCard(
                hand1.makeMove(this, "DiscardMove"));
        cribManager.addCard(
                hand2.makeMove(this, "DiscardMove"));
        cribManager.addCard(
                hand2.makeMove(this, "DiscardMove"));



        cribManager.sort();
        System.out.print(players + " --- ");
        System.out.println(cribManager);
        System.out.println();


        Card firstCard = deckManager.dealCard();
//        Card firstCard = new Card("4", "H");
        if (firstCard.face == "J") {
            dealer.win(2);
        }

        ArrayList<Card> hand1Copy = (ArrayList<Card>) hand1.getDeckList().clone();
        ArrayList<Card> hand2Copy = (ArrayList<Card>) hand2.getDeckList().clone();

//        e.getCribbageHandScore(hand1, firstCard);
//        e.getCribbageHandScore(hand2, firstCard);
//        e.getCribbageHandScore(cribManager, firstCard);

//        System.out.println(hand1.getDeckList());
//        System.out.println(hand1Copy);
//        hand1.playFirstCard();
//        System.out.println(hand1.getDeckList());
//        System.out.println(hand1Copy);

        Hand currentPlayer = dealer;
        Hand otherPlayer = nonDealer;
        Hand tempHand;

//        System.out.println(discardManager);
//        System.out.println(getCount());

        boolean firstRound = true;
        outOfCards = false;
        while (!outOfCards) {
            discardManager.discardAll();
            if (firstRound){
                //discardManager.addCard(firstCard);
                System.out.println("Flip Card: " + firstCard);
                firstRound = false;
            }

            Boolean wasNoPlay = false;
            playing = true;
            Hand lastPlayer = hand1;
            while (playing) {

                if (dealer.getDeckList().size() == 0 && nonDealer.getDeckList().size() == 0) {
//                    System.out.println("OutOfCards");
                    outOfCards = true;
                    break;
                }

                if (currentPlayer.getDeckList().size() == 0) {//switch
//                    System.out.println("Switch");
                    tempHand = currentPlayer;
                    currentPlayer = otherPlayer;
                    otherPlayer = tempHand;
                    break;
                }

                Card lastPlayed = currentPlayer.makeMove(this, "Play");
                if (lastPlayed.getId() == 99) {
                    System.out.println(currentPlayer + " had nothing to play");
                    if (wasNoPlay) {
                        playing = false;
                    }
                    wasNoPlay = true;
                } else if (discardManager.getValueSum() + lastPlayed.getValue() > 31) {
                    System.out.println("Wrong card played");
                    if (wasNoPlay) {
                        playing = false;
                    }
                    wasNoPlay = true;
                } else {
                    discardManager.addCard(lastPlayed);
                    wasNoPlay = false;
                    lastPlayer = currentPlayer;
                    //e.getCribbagePlayScore(currentPlayer, discardManager);
                    int score = e.getCribbagePlayScore(currentPlayer, discardManager);
//                    if (score != 0){
//                        System.out.println("got the score of: " + score);
//                    }
                }

                tempHand = currentPlayer;
                currentPlayer = otherPlayer;
                otherPlayer = tempHand;
            }
            lastPlayer.win(1);
            System.out.println(lastPlayer + " played the last card");
            System.out.println();

        }

        //end:



        while (hand1.getDeckList().size()>0){
//            System.out.println("ERRORERRORERRORERRORERRORERRORERRORERRORERRORERROR");
            hand1.silentlyPlayFirstCard();
        }
        while (hand2.getDeckList().size()>0){
//            System.out.println("ERRORERRORERRORERRORERRORERRORERRORERRORERRORERROR");
            hand2.silentlyPlayFirstCard();
        }

        hand1.drawMultiple(hand1Copy);
        hand2.drawMultiple(hand2Copy);


        e.getCribbageHandScore(hand1, firstCard);
        e.getCribbageHandScore(hand2, firstCard);
        dealer.win(e.getCribbageHandScore(cribManager, firstCard));

        while (hand1.getDeckList().size()>0){
//            System.out.println("ERRORERRORERRORERRORERRORERRORERRORERRORERRORERROR");
            hand1.silentlyPlayFirstCard();
        }
        while (hand2.getDeckList().size()>0){
//            System.out.println("ERRORERRORERRORERRORERRORERRORERRORERRORERRORERROR");
            hand2.silentlyPlayFirstCard();
        }
        cribManager = new Hand("Crib", this);


        tempHand = dealer;
        dealer = nonDealer;
        nonDealer = tempHand;

        System.out.println("The current score is: " + hand1 + " <===> " + hand2);
        System.out.println("======= End of round " + round + " =======");
        System.out.println();
    }

    public void pokerRound(){
        initialize();
        dealToAll(5);
        for (Hand hand: players){
            e.getPokerScore(hand);
        }
    }

    public void dealToAll(int numOfCards){
        for (int i = 0; i < numOfCards; i++){
            dealToAll();
        }
    }

    public void dealToAll(){
        for (Hand h: players) {
            dealTo(h);
        }
    }

    public void dealTo(Hand h){
        h.draw(deckManager.dealCard());
    }

    public ArrayList<Hand> getPlayers() {
        return players;
    }

    public ArrayList<Card> getDiscardPile() {
        return (ArrayList<Card>) discardManager.deckList.clone();
    }

    public Evaluator getEvaluator() {
        return e;
    }

    public int getCount(){
        return discardManager.getValueSum();
    }

    public void endGame(Hand winnerHand){
        playing = false;
        outOfCards = true;
        gameOver = true;
        winner = winnerHand;
    }
}
