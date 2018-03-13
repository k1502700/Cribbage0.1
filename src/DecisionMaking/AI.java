package DecisionMaking;

import core.Card;
import core.Deck;
import core.Game;
import core.Hand;
import core.Evaluator;

import java.util.ArrayList;

public class AI extends DecisionMaker{
    Card futureMove = new Card("X");
    boolean futureSet = false;

    public AI(){

    }

    @Override
    public Card makeMove(Game gameState, String moveType, Hand currentPlayer) {
        if (moveType == "Play"){
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


        if (moveType == "DiscardMove"){
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




//
//                    for (Card c: currentDiscard){
//                        for (Card d: potentialHand){
//                            if (c.getId() == d.getId() && c.getSuit() == d.getSuit()){
//                                potentialHand.remove(d);
//                            }
//                        }
//                    }
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
                possibleFlipCards.playGivenCard(c);
            }

            ArrayList<Card> flipcards = possibleFlipCards.getDeckList();
            ArrayList<Float> averageScores = new ArrayList<>();
            ArrayList<Integer> maxScores = new ArrayList<>();
            ArrayList<Integer> flipScores = new ArrayList<>();
            Hand dummyPlayer = new Hand();
            Evaluator e = new Evaluator("Cribbage");

            for (int p = 0; p < potentialHnads.size(); p++) {
                for (int f = 0; f < flipcards.size(); f++){
                    dummyPlayer = new Hand();
                    dummyPlayer.drawMultiple((ArrayList<Card>) potentialHnads.get(p).clone());
                    flipScores.add(f, e.getCribbageHandScore(dummyPlayer, flipcards.get(f)));
                }
                float a = 0;
                for (int i: flipScores){
                    a += i;
                }
                a = a/flipScores.size();
                averageScores.add(a);
            }
            System.out.println(averageScores);



























//            int count = gameState.getCount();
//            for (Card c: currentPlayer.getDeckList()){
//                if (c.getValue() + count <= 31){
//                    viableMoves.add(c);
////                    System.out.println(currentPlayer + " played " + c + ", the count is: " + (count+c.getValue()));
////                    return currentPlayer.playGivenCard(c);
//                }
            }
//
//            int maxScore = 0;
//            int maxAverage = 0;
//            ArrayList
//
//            Deck possibleFlipCards = new Deck(true);
//            for (Card c: currentPlayer.getDeckList()){
//                possibleFlipCards.playGivenCard(c);
//            }
//            int score = 0;
//            for (Card c: viableMoves){
//
//
//
//
//
//
//
//
//
//
//                for (Card f: possibleFlipCards.getDeckList())
//                    score = gameState.getEvaluator().getCribbageHandScore(currentPlayer, f);
//                    if (score > maxScore){
//
//                    }
//            }
//
//
//
//        }










        return currentPlayer.playFirstCard();
    }

    /*
        //System.out.print("AI TRIGGERED: ");
        Card returnCard;

        switch (moveType){
            case "DiscardMove":
                //TODO: Find Jesus
                ArrayList<Card> handList = currentPlayer.getDeckList();
                Collections.shuffle(handList);
//                returnCard = handList.get(0);
                if (currentPlayer.discardCard(handList.get(0))){
                    return handList.get(0);
                }
                else{
                    System.out.println("Something went wrong while trying to discard a card (AI.java)");
                }



                break;
            default:
                System.out.println("No specific move set found (AI.java)");
                break;
        }




        return new Card("0");
    }*/

}
