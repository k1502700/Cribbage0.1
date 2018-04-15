package DecisionMaking;

import core.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AI extends DecisionMaker{
    Card futureMove = new Card("X");
    boolean futureSet = false;
    String level = "Hybrid";//other accepted are: "Full", "Lite", "Hybrid", "Basic"

    public AI(){

    }

    public AI(String level){
        this.level = level;
    }

    @Override
    public Card makeMove(Game gameState, String moveType, Hand currentPlayer) {
        if (moveType == "Play" && level =="Basic"){
            int count = gameState.getCount();
            for (Card c: currentPlayer.getDeckList()){
                if (c.getValue() + count <= 31){
//                    System.out.println(currentPlayer + " played " + c + ", the count is: " + (count+c.getValue()));
                    return currentPlayer.playGivenCard(c);
                }
            }
//            System.out.println(currentPlayer + " has no cards left less than 31");
            return new Card("X", "S");
        }



        if (moveType == "Play"){

            Evaluator e = new Evaluator("Cribbage", true);
            int count = gameState.getCount();
            ArrayList<Card> deckList = (ArrayList<Card>) currentPlayer.getDeckList().clone();
            ArrayList<Card> viableMoves = new ArrayList<>();
            ArrayList<Double> scores = new ArrayList<>();
            ArrayList<Card> unknowns = new Deck(true).getDeckList();

            for (Card c: deckList){
                for (int i = 0; i < unknowns.size(); i++){
                    if (c.getId() == unknowns.get(i).getId() && c.getSuit() == unknowns.get(i).getSuit()){
                        unknowns.remove(i);
                        i--;
                    }
                }
                if (c.getValue() + count <= 31){
                    viableMoves.add(c);
//                    System.out.println(currentPlayer + " played " + c + ", the count is: " + (count+c.getValue()));
//                    return currentPlayer.playGivenCard(c);
                }
            }

//            System.out.println("viablemoves: " + viableMoves);

            if (viableMoves.size() < 1){
//                System.out.println(currentPlayer + " has no cards left less than 31");
                return new Card("X", "S");
            }

            ArrayList<Integer> unknownScores = new ArrayList<>();
            ArrayList<Double> unknownAverages = new ArrayList<>();

            for (Card c: viableMoves){
                for (Card u: unknowns) {
                    Deck discardDeck = new Deck(false);
                    discardDeck.addMultiple((ArrayList<Card>) gameState.getDiscardPile().clone());
                    discardDeck.addCard(c);
                    discardDeck.addCard(u);
                    Hand dummyPlayer = new Hand("DummyPlayer", new Game(true));

                    Hand otherPlayer = new Hand();
                    if (currentPlayer.name == gameState.getPlayers().get(0).name){
                        otherPlayer = gameState.getPlayers().get(1);
                    }
                    else {
                        otherPlayer = gameState.getPlayers().get(0);
                    }

                    if (shouldBeAvoided(c, otherPlayer.cardsPlayed)){
                        unknownScores.add(e.getCribbagePlayScore(dummyPlayer, discardDeck) * 6);
                    }else {
                        unknownScores.add(e.getCribbagePlayScore(dummyPlayer, discardDeck));
                    }
                }

                int sum = 0;
                for (int i : unknownScores) {
                    sum += i;
                }

                double average = (double) sum/(double) unknownScores.size();
                unknownScores = new ArrayList<>();
                unknownAverages.add(average);
            }
//            System.out.println(unknownAverages);
//            System.out.println(viableMoves);
            for (int i = 0; i < viableMoves.size(); i++){
                Deck discardDeck = new Deck(false);
                discardDeck.addMultiple((ArrayList<Card>) gameState.getDiscardPile().clone());
                discardDeck.addCard(viableMoves.get(i));
                Hand dummyPlayer = new Hand("DummyPlayer", new Game(true));
                dummyPlayer.drawMultiple(deckList);
                if (level == "Hybrid" || level == "Full"){
                    scores.add((double) e.getCribbagePlayScore(dummyPlayer, discardDeck) - unknownAverages.get(i));
                }
                else {
                    scores.add((double) e.getCribbagePlayScore(dummyPlayer, discardDeck));
                }
            }

//            System.out.println(scores);
            Card returnCard = viableMoves.get(0);
            double max = -1;
            for (int i = 0; i < viableMoves.size(); i++){
                if (scores.get(i) > max){
                    max = scores.get(i);
                    returnCard = viableMoves.get(i);
                }
            }
            return currentPlayer.playGivenCard(returnCard);
        }

        if ((moveType == "DiscardMove") && (level == "Hybrid")){
//            System.out.println("Hybrid Discard Analysis");
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
//            System.out.println(potentialHnads);
//            System.out.println(potentialDiscards);

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


                Deck unknownDeck = new Deck(true);
                for (Card d: currentPlayer.getDeckList()){
                    unknownDeck.discardCard(d);
                }
                //unknownDeck.discardCard(flipCards.get(f));

                ArrayList<Integer> cribScores = new ArrayList<>();
                for (int g = 0; g < unknownDeck.getDeckList().size()-1; g++){
                    for (int h = g + 1; h < unknownDeck.getDeckList().size(); h++) {
                        Hand crib = new Hand("Crib", new Game(true));
                        crib.draw(unknownDeck.getDeckList().get(g));
                        crib.draw(unknownDeck.getDeckList().get(h));
                        int opponentCribScore = e.getCribbageHandScore(crib, new Card("X"));
                        if (opponentCribScore > 0 && currentPlayer.game.dealer.name == currentPlayer.name){

                        }
                        else {
                            crib.drawMultiple(potentialDiscards.get(p));
                            cribScores.add(e.getCribbageHandScore(crib, new Card("X")));
                        }
                    }
                }
                float a = 0;
                for (int i: cribScores){
                    a += i;
                }
                a = a/cribScores.size();


                for (int f = 0; f < flipCards.size(); f++){//maybe only once
                    dummyPlayer = new Hand("Dummy Player", new Game(true));
                    dummyPlayer.drawMultiple((ArrayList<Card>) potentialHnads.get(p).clone());

                    averageCribScores.add(a);
                    flipScores.add(f, e.getCribbageHandScore(dummyPlayer, flipCards.get(f)));
                }
                a = 0;
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
//                System.out.print(c+ " ");
            }
//            System.out.println();
//            System.out.println(averageScores);


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


        if (moveType == "DiscardMove" && level == "Full"){
//            System.out.println("Full Discard Analysis");
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
//            System.out.println(potentialHnads);
//            System.out.println(potentialDiscards);

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
                for (int f = 0; f < flipCards.size(); f++){//maybe only once
                    dummyPlayer = new Hand("Dummy Player", new Game(true));
                    dummyPlayer.drawMultiple((ArrayList<Card>) potentialHnads.get(p).clone());
                    Deck unknownDeck = new Deck(true);
                    for (Card d: currentPlayer.getDeckList()){
                        unknownDeck.discardCard(d);
                    }
                    unknownDeck.discardCard(flipCards.get(f));

                    ArrayList<Integer> cribScores = new ArrayList<>();
                    for (int g = 0; g < unknownDeck.getDeckList().size()-1; g++){
                        for (int h = g + 1; h < unknownDeck.getDeckList().size(); h++) {
                            Hand crib = new Hand("Crib", new Game(true));
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
//                System.out.print(c+ " ");
            }
//            System.out.println();
//            System.out.println(averageScores);


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

        if ((moveType == "DiscardMove") && (level == "Lite")){
//            System.out.println("Lite Discard Analysis");
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
//            System.out.println(potentialHnads);
//            System.out.println(potentialDiscards);

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


                Deck unknownDeck = new Deck(true);
                for (Card d: currentPlayer.getDeckList()){
                    unknownDeck.discardCard(d);
                }
                //unknownDeck.discardCard(flipCards.get(f));

                ArrayList<Integer> cribScores = new ArrayList<>();
                for (int g = 0; g < unknownDeck.getDeckList().size()-1; g++){
                    for (int h = g + 1; h < unknownDeck.getDeckList().size(); h++) {
                        Hand crib = new Hand("Crib", new Game(true));
                        crib.draw(unknownDeck.getDeckList().get(g));
                        crib.draw(unknownDeck.getDeckList().get(h));
                        crib.drawMultiple(potentialDiscards.get(p));
                        cribScores.add(e.getCribbageHandScore(crib, new Card("X")));
                    }
                }
                float a = 0;
                for (int i: cribScores){
                    a += i;
                }
                a = a/cribScores.size();


                for (int f = 0; f < flipCards.size(); f++){//maybe only once
                    dummyPlayer = new Hand("Dummy Player", new Game(true));
                    dummyPlayer.drawMultiple((ArrayList<Card>) potentialHnads.get(p).clone());

                    averageCribScores.add(a);
                    flipScores.add(f, e.getCribbageHandScore(dummyPlayer, flipCards.get(f)));
                }
                a = 0;
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
//                System.out.print(c+ " ");
            }
//            System.out.println();
//            System.out.println(averageScores);


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
        if ((moveType == "DiscardMove") && (level == "Basic")) {
            return currentPlayer.playFirstCard();
        }


            return currentPlayer.playFirstCard();
    }

    public boolean shouldBeAvoided(Card referenceCard, ArrayList<Card> opponentList){
        Set<String> avoidSet = new HashSet<>();

        for (Card c: opponentList){

            if (c.getId() == 1){
                avoidSet.add("A");
                avoidSet.add("2");
            }
            if (c.getId() == 2){
                avoidSet.add("A");
                avoidSet.add("2");
                avoidSet.add("3");
            }
            if (c.getId() == 3){
                avoidSet.add("2");
                avoidSet.add("3");
                avoidSet.add("4");
            }
            if (c.getId() == 4){
                avoidSet.add("3");
                avoidSet.add("4");
                avoidSet.add("5");
            }
            if (c.getId() == 5){
                avoidSet.add("4");
                avoidSet.add("5");
                avoidSet.add("6");
                avoidSet.add("10");
                avoidSet.add("11");
                avoidSet.add("12");
                avoidSet.add("13");
            }
            if (c.getId() == 6){
                avoidSet.add("5");
                avoidSet.add("6");
                avoidSet.add("7");
                avoidSet.add("9");
            }
            if (c.getId() == 7){
                avoidSet.add("6");
                avoidSet.add("7");
                avoidSet.add("8");
            }
            if (c.getId() == 8){
                avoidSet.add("7");
                avoidSet.add("8");
                avoidSet.add("9");
            }
            if (c.getId() == 9){
                avoidSet.add("8");
                avoidSet.add("9");
                avoidSet.add("10");
                avoidSet.add("6");
            }
            if (c.getId() == 10){
                avoidSet.add("9");
                avoidSet.add("10");
                avoidSet.add("11");
                avoidSet.add("5");
            }
            if (c.getId() == 11){
                avoidSet.add("10");
                avoidSet.add("11");
                avoidSet.add("12");
                avoidSet.add("5");
            }
            if (c.getId() == 12){
                avoidSet.add("11");
                avoidSet.add("12");
                avoidSet.add("13");
                avoidSet.add("5");
            }
            if (c.getId() == 13){
                avoidSet.add("12");
                avoidSet.add("13");
                avoidSet.add("5");
            }
        }

        for (String s: avoidSet){
            try {
                if (Integer.parseInt(s) == referenceCard.getId()){
                    return true;
                }
            }
            catch (NumberFormatException e) {
                //do nothing
            }


        }

        return false;

    }

    public ArrayList<Card> getAvoided(ArrayList<Card> opponentList){
        Set<String> avoidSet = new HashSet<>();
        ArrayList<Card> returnList = new ArrayList<>();




        for (Card c: opponentList){

            if (c.getId() == 1){
                avoidSet.add("A");
                avoidSet.add("2");
            }
            if (c.getId() == 2){
                avoidSet.add("A");
                avoidSet.add("2");
                avoidSet.add("3");
            }
            if (c.getId() == 3){
                avoidSet.add("2");
                avoidSet.add("3");
                avoidSet.add("4");
            }
            if (c.getId() == 4){
                avoidSet.add("3");
                avoidSet.add("4");
                avoidSet.add("5");
            }
            if (c.getId() == 5){
                avoidSet.add("4");
                avoidSet.add("5");
                avoidSet.add("6");
                avoidSet.add("10");
                avoidSet.add("11");
                avoidSet.add("12");
                avoidSet.add("13");
            }
            if (c.getId() == 6){
                avoidSet.add("5");
                avoidSet.add("6");
                avoidSet.add("7");
                avoidSet.add("9");
            }
            if (c.getId() == 7){
                avoidSet.add("6");
                avoidSet.add("7");
                avoidSet.add("8");
            }
            if (c.getId() == 8){
                avoidSet.add("7");
                avoidSet.add("8");
                avoidSet.add("9");
            }
            if (c.getId() == 9){
                avoidSet.add("8");
                avoidSet.add("9");
                avoidSet.add("10");
                avoidSet.add("6");
            }
            if (c.getId() == 10){
                avoidSet.add("9");
                avoidSet.add("10");
                avoidSet.add("11");
                avoidSet.add("5");
            }
            if (c.getId() == 11){
                avoidSet.add("10");
                avoidSet.add("11");
                avoidSet.add("12");
                avoidSet.add("5");
            }
            if (c.getId() == 12){
                avoidSet.add("11");
                avoidSet.add("12");
                avoidSet.add("13");
                avoidSet.add("5");
            }
            if (c.getId() == 13){
                avoidSet.add("12");
                avoidSet.add("13");
                avoidSet.add("5");
            }
        }

        for (String s: avoidSet){
            returnList.add(new Card(s));
        }

        return returnList;
    }

}

