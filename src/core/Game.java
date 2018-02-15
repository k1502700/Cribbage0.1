package core;

import java.util.ArrayList;

public class Game {

    public Game(){
        for (int i = 0; i < 1; i++) {
            System.out.println();
            System.out.println("Round: " + i);
            cribbageRound();
        }
    }

    Evaluator e = new Evaluator("Cribbage");

    Deck deckManager;
    Deck discardManager;
    ArrayList<Deck> dicardPiles = new ArrayList<Deck>();
    Hand cribManager = new Hand("Crib");
    ArrayList<Card> deck;
    Hand hand1;
    Hand hand2;
    Hand hand3;
    Hand hand4;
    Hand dealer;
    Hand nonDealer;//TODO: later implement switching - now only 2 player mode

    ArrayList<Hand> players = new ArrayList<>();

    public void initialize(){
        deckManager = new Deck();
        deck = deckManager.shuffleDeck();
        discardManager = new Deck(false);
        //System.out.println(deck);

        hand1 = new Hand("Preacher");
        hand2 = new Hand("Dennis");
        //hand3 = new Hand("Tulip");
        //hand4 = new Hand("Cassidy");

        players = new ArrayList<>();
        players.add(hand1);
        players.add(hand2);
        //players.add(hand3);
        //players.add(hand4);

        dealer = hand1; //TODO: this needs to be iterated
        nonDealer = hand2;



    }



    public void cribbageRound(){

        initialize();
        dealToAll(6);
//        hand1.addCard(new Card("K","S"));
//        hand1.addCard(new Card("A","S"));
//        hand1.addCard(new Card("Q","S"));
//        hand1.addCard(new Card("J","S"));
//
//        for (int i = 0; i < 4; i++){
//            dealTo(hand2);
//            dealTo(cribManager);
//        }

        System.out.println("Discarding cards:");
        System.out.println(players);
        for (Hand player: players) {
            cribManager.addCard(
                    player.makeMove(this, "DiscardMove"));
            cribManager.addCard(
                    player.makeMove(this, "DiscardMove"));
        }
        cribManager.sort();
        System.out.print(players + " --- ");
        System.out.println(cribManager);
        System.out.println();


        Card firstCard = deckManager.dealCard();
//        Card firstCard = new Card("5", "S");
        if (firstCard.face == "J"){
            dealer.addPoints(2);
        }
        discardManager.addCard(firstCard);

        ArrayList<Card> hand1Copy = (ArrayList<Card>) hand1.getDeckList().clone();
        ArrayList<Card> hand2Copy = (ArrayList<Card>) hand2.getDeckList().clone();

        e.getCribbageHandScore(hand1, firstCard);
        e.getCribbageHandScore(hand2, firstCard);
        e.getCribbageHandScore(cribManager, firstCard);


//        System.out.println(hand1.getDeckList());
//        System.out.println(hand1Copy);
//        hand1.playFirstCard();
//        System.out.println(hand1.getDeckList());
//        System.out.println(hand1Copy);


        Hand currentPlayer = nonDealer;
        Hand otherPlayer = dealer;
        Hand tempHand;

        System.out.println(discardManager);
        System.out.println(getCount());

        Boolean wasNoPlay = false;
        Boolean playing = true;
        while (playing){

            if (dealer.getDeckList().size() == 0 && nonDealer.getDeckList().size() == 0){
                break;
            }

            if (currentPlayer.getDeckList().size() == 0){//switch
                tempHand = currentPlayer;
                currentPlayer = otherPlayer;
                otherPlayer = tempHand;
            }

            Card lastPlayed = currentPlayer.makeMove(this, "Play");
            if (discardManager.getValueSum() + lastPlayed.getId() > 31){
                if (wasNoPlay){
                    playing = false;
                    //TODO: implement +1 Score after last play
                }
                wasNoPlay = true;
                //System.out.println("ERROR - HIGHER THAN 31!!");
            }
            else{
                discardManager.addCard(lastPlayed);
                wasNoPlay = false;
            }

            tempHand = currentPlayer;
            currentPlayer = otherPlayer;
            otherPlayer = tempHand;




        }
    }


        //players.get(0).makeMove(this, "DiscardMove");
        //TODO: implement discard piles, like a pot thing, the cribManager, etc!!


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

    public int getCount(){
        return discardManager.getValueSum();
    }
}
