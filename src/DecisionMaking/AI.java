package DecisionMaking;

import core.*;
import java.util.ArrayList;

public class AI extends DecisionMaker{
    Card futureMove = new Card("X");
    boolean futureSet = false;

    public AI(){

    }

    @Override
    public Card makeMove(Game gameState, String moveType, Hand currentPlayer) {
        if (moveType == "Play_Old"){
            int count = gameState.getCount();
            for (Card c: currentPlayer.getDeckList()){
                if (c.getValue() + count <= 31){
                    System.out.println(currentPlayer + " played " + c + ", the count is: " + (count+c.getValue()));
                    return currentPlayer.playGivenCard(c);
                }
            }
            System.out.println(currentPlayer + " has no cards left less than 31");
            return new Card("X", "S");
        }



        if (moveType == "Play"){

            Evaluator e = new Evaluator("Cribbage", true);
            int count = gameState.getCount();
            ArrayList<Card> deckList = (ArrayList<Card>) currentPlayer.getDeckList().clone();
            ArrayList<Card> viableMoves = new ArrayList<>();
            ArrayList<Integer> scores = new ArrayList<>();

            for (Card c: deckList){
                if (c.getValue() + count <= 31){
                    viableMoves.add(c);
//                    System.out.println(currentPlayer + " played " + c + ", the count is: " + (count+c.getValue()));
//                    return currentPlayer.playGivenCard(c);
                }
            }

//            System.out.println("viablemoves: " + viableMoves);

            if (viableMoves.size() < 1){
                System.out.println(currentPlayer + " has no cards left less than 31");
                return new Card("X", "S");
            }

//            System.out.println(viableMoves);
            for (Card c: viableMoves){
                Deck discardDeck = new Deck(false);
                discardDeck.addMultiple((ArrayList<Card>) gameState.getDiscardPile().clone());
                discardDeck.addCard(c);
                Hand dummyPlayer = new Hand("DummyPlayer");
                dummyPlayer.drawMultiple(deckList);
                scores.add(e.getCribbagePlayScore(dummyPlayer, discardDeck));
            }

//            System.out.println(scores);
            Card returnCard = viableMoves.get(0);
            int max = -1;
            for (int i = 0; i < viableMoves.size(); i++){
                if (scores.get(i) > max){
                    max = scores.get(i);
                    returnCard = viableMoves.get(i);
                }
            }
            return currentPlayer.playGivenCard(returnCard);
        }



        if (moveType == "DiscardMove"){
            System.out.println("Discard Analysis");
            if (futureSet){
                futureSet = false;
                return currentPlayer.playGivenCard(futureMove);
            }


            ArrayList<Card> viableMoves = (ArrayList<Card>) currentPlayer.getDeckList().clone();
            ArrayList<ArrayList<Card>> potentialHnads = new ArrayList<>();
            ArrayList<Card> potentialHand = new ArrayList<>();
            ArrayList<Card> currentDiscard = new ArrayList<>();
            ArrayList<ArrayList<Card>> potentialDiscards = new ArrayList<>();
            for (int i = 0; i < viableMoves.size(); i++) {
                currentDiscard.add(viableMoves.get(i));
                for (int j = i + 1; j < viableMoves.size(); j++) {
                    currentDiscard.add(viableMoves.get(j));
                    potentialHand = (ArrayList<Card>) currentPlayer.getDeckList().clone();
                    for (int k = 0; k < potentialHand.size(); k++){
                        for (Card c: currentDiscard) {
                            if (c.getId() == potentialHand.get(k).getId() && c.getSuit() == potentialHand.get(k).getSuit()){
                                potentialHand.remove(potentialHand.get(k));
                            }

                        }
                    }

                    potentialHnads.add((ArrayList<Card>) potentialHand.clone());
                    potentialDiscards.add((ArrayList<Card>) currentDiscard.clone());
                    currentDiscard.remove(viableMoves.get(j));
                }
                currentDiscard.remove(viableMoves.get(i));
            }
            System.out.println(potentialHnads);
            System.out.println(potentialDiscards);

            Deck possibleFlipCards = new Deck(true);
            for (Card c: currentPlayer.getDeckList()){
                possibleFlipCards.discardCard(c);
            }

            ArrayList<Card> flipCards = possibleFlipCards.getDeckList();
            ArrayList<Float> averageScores = new ArrayList<>();
            ArrayList<Float> averageFlipScores = new ArrayList<>();
            ArrayList<Integer> maxScores = new ArrayList<>();
            ArrayList<Integer> flipScores = new ArrayList<>();
            Hand dummyPlayer = new Hand();
            Evaluator e = new Evaluator("Cribbage", true);

            ArrayList<Float> averageCribScores = new ArrayList<>();

            for (int p = 0; p < potentialHnads.size(); p++) {
                for (int f = 0; f < flipCards.size(); f++){
                    dummyPlayer = new Hand("Dummy Player");
                    dummyPlayer.drawMultiple((ArrayList<Card>) potentialHnads.get(p).clone());
                    Deck unknownDeck = new Deck(true);
                    for (Card d: currentPlayer.getDeckList()){
                        unknownDeck.discardCard(d);
                    }
                    unknownDeck.discardCard(flipCards.get(f));

                    ArrayList<Integer> cribScores = new ArrayList<>();
                    for (int g = 0; g < unknownDeck.getDeckList().size()-1; g++){
                        for (int h = g + 1; h < unknownDeck.getDeckList().size(); h++) {
                            Hand crib = new Hand("Crib");
                            crib.draw(unknownDeck.getDeckList().get(g));
                            crib.draw(unknownDeck.getDeckList().get(h));
                            crib.drawMultiple(potentialDiscards.get(p));
                            cribScores.add(e.getCribbageHandScore(crib, flipCards.get(f)));
                        }
                    }
                    float a = 0;
                    for (int i: cribScores){
                        a += i;
                    }
                    a = a/cribScores.size();
                    averageCribScores.add(a);
                    flipScores.add(f, e.getCribbageHandScore(dummyPlayer, flipCards.get(f)));
                }
                float a = 0;
                for (int i: flipScores){
                    a += i;
                }
                a = a/flipScores.size();
                float c = 0;
                for (float i: averageCribScores){
                    c += i;
                }
                c = c/ averageCribScores.size();
                if (gameState.dealer.name == currentPlayer.name){
                    averageScores.add(a + c);
                }
                else{
                    averageScores.add(a - c);
                }
                System.out.print(c+ " ");
            }
            System.out.println();
            System.out.println(averageScores);


            float maxAverage = -1;
            Card toBePlayed = new Card("X");
            for (int i = 0; i < averageScores.size(); i++){
                if (averageScores.get(i) > maxAverage){
                    maxAverage = averageScores.get(i);
                    toBePlayed = potentialDiscards.get(i).get(0);
                    futureMove = potentialDiscards.get(i).get(1);
                    futureSet = true;
                }
            }
            return currentPlayer.playGivenCard(toBePlayed);
        }

        return currentPlayer.playFirstCard();
    }
}
