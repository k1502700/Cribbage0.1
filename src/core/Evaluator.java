package core;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;

public class Evaluator {

    String game = "";
    PrintStream originalStream = System.out;

    public Evaluator(String gameType){
        game = gameType;
    }

    public Evaluator(String gameType, boolean isMute){
        game = gameType;
        if (isMute){
            originalStream = new PrintStream(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    //Do nothing
                }
            });
        }
    }


    public int getCribbagePlayScore(Hand currentPlayer, Deck discardDeck){
        //TODO: CAREFUL!! will give anyone points if they didn't play a card last turn
        //TODO: only call after successful play

        //------------Pair-------------
        ArrayList<Card> deckList = discardDeck.getDeckList();
        int numOfIdenticals = howmanyIdenticals(deckList);
        int pairPoints = 0;
        switch (numOfIdenticals){
            case 2: pairPoints = 2; break;
            case 3: pairPoints = 6; break;
            case 4: pairPoints = 12; break;
            default: pairPoints = 0; break;
        }
        currentPlayer.win(pairPoints);
        if (numOfIdenticals != 1) {
            originalStream.println("Found " + numOfIdenticals + " same cards");
        }
        //------------Straight---------

        int straightScore = straight(deckList);
        if (straightScore != 0) {
            currentPlayer.win(straightScore);
            originalStream.println("Found " + straightScore + " Straight");
        }

        int score = pairPoints + straightScore;
        //------------sum15------------
        int sum = 0;
        for (Card c: deckList){
            sum = sum + c.value;
        }
        if (sum == 15){
            currentPlayer.win(2);
            score += 2;
            originalStream.println("Found 15");
        }
        if (sum == 31){
            currentPlayer.win(1);
            score += 1;
            originalStream.println("Found 31");
        }

        return score;
    }

    //not quite what i was supposed to count here
    public boolean sum15(ArrayList<Card> deckIn, int sum){
        ArrayList<Card> deck = (ArrayList<Card>) deckIn.clone();
        if (deck.size() >= 1){
            int newSum = deck.get(deck.size()-1).value + sum;
            if (newSum < 15){
                deck.remove(deck.size()-1);
                return sum15(deck, newSum);
            }
            else if (newSum == 15){
                return true;
            }
            else return false;
        }
        else return false;
    }

    public int straight(ArrayList<Card> deckIn){
        ArrayList<Card> deck = (ArrayList<Card>) deckIn.clone();

        if (deck.size() < 3){
            return 0;
        }

        int score = 0;
        for (int i = deck.size() - 3; i >= 0; i--){
            ArrayList<Card> straightChecker = new ArrayList<>();
            for (int j = i; j < deck.size(); j++){
                straightChecker.add(deck.get(j));
            }
            if (isStraight(straightChecker)){
                score = deck.size() - i;
            }
            else return score;
        }
        return score;
    }

    public boolean isStraight(ArrayList<Card> deckIn){
        ArrayList<Card> deck = (ArrayList<Card>) deckIn.clone();
        deck.sort(new Comparator<Card>() {
            public int compare(Card o1, Card o2) { return ((Integer)o1.id).compareTo((Integer)o2.id); }
        });
        boolean straight = true;
        for (int i = 0; i < deck.size()-1; i++){
            if (!(deck.get(i).id == deck.get(i+1).id -1)){
                straight = false;
            }
        }
        return straight;
    }

    public int howmanyIdenticals(ArrayList<Card> deckIn){
        ArrayList<Card> deck = (ArrayList<Card>) deckIn.clone();
        if (deck.size() >= 2){
            if (deck.get(deck.size()-1).id == deck.get(deck.size()-2).id){
                deck.remove(deck.size()-1);
                return howmanyIdenticals(deck) + 1;
            }
            else return 1;
        }
        else return 1;
    }

    public int getCribbageHandScore(Hand currentPlayer, Card flipCard){
        int initialScore = currentPlayer.score;

        originalStream.println("Inspecting " + currentPlayer + " + [" + flipCard + "]");



        Hand calculator = new Hand("Calculator");

        for (Card c: currentPlayer.getDeckList()){
            calculator.addCard(c);
        }

        if (currentPlayer.name == "Crib"){
            originalStream.println("Found the Crib");
        }
        else {
            calculator.addCard(flipCard);
        }

        calculator.sort();

        ArrayList<Card> cList = calculator.getDeckList();//card List
        ArrayList<Card> cListCopy = calculator.getDeckList();//copy of cList
        ArrayList<Integer> idList = new ArrayList<>(); //card id List
        ArrayList<Integer> cAmountList = new ArrayList<>(); //card amount/type List
        ArrayList<Integer> saList = new ArrayList<>(); //suit amount List

        Boolean flush = false;
        Boolean straight = false;

        //------------Pair-------------

        int numberof2 = 0;
        int numberof3 = 0;
        int numberof4 = 0;
        int numberof5 = 0;
        int numberof6 = 0;
        int numberof7 = 0;
        int numberof8 = 0;
        int numberof9 = 0;
        int numberof10 = 0;
        int numberofJ = 0;
        int numberofQ = 0;
        int numberofK = 0;
        int numberofA = 0;

        for (Card card: cList){
            switch (card.id){
                case 1:
                    numberofA++;
                    break;
                case 2:
                    numberof2++;
                    break;
                case 3:
                    numberof3++;
                    break;
                case 4:
                    numberof4++;
                    break;
                case 5:
                    numberof5++;
                    break;
                case 6:
                    numberof6++;
                    break;
                case 7:
                    numberof7++;
                    break;
                case 8:
                    numberof8++;
                    break;
                case 9:
                    numberof9++;
                    break;
                case 10:
                    numberof10++;
                    break;
                case 11:
                    numberofJ++;
                    break;
                case 12:
                    numberofQ++;
                    break;
                case 13:
                    numberofK++;
                    break;
                default:
                    originalStream.println("Invalid card id input for evaluation");
                    break;
            }
        }

        cAmountList.add(numberof2);
        cAmountList.add(numberof3);
        cAmountList.add(numberof4);
        cAmountList.add(numberof5);
        cAmountList.add(numberof6);
        cAmountList.add(numberof7);
        cAmountList.add(numberof8);
        cAmountList.add(numberof9);
        cAmountList.add(numberof10);
        cAmountList.add(numberofJ);
        cAmountList.add(numberofQ);
        cAmountList.add(numberofK);
        cAmountList.add(numberofA);

        for (int cardAmount: cAmountList){
            if (cardAmount == 2){
                currentPlayer.win(2);
                originalStream.println("Found Pair");
            }
            if (cardAmount == 3){
                currentPlayer.win(6);
                originalStream.println("Found Three of a Kind");
            }
            if (cardAmount == 4){
                currentPlayer.win(12);
                originalStream.println("Found Four of a Kind");
            }
            if (cardAmount == 5){
                originalStream.println("----------------------------*ALERT*----------------------------");
                originalStream.println("Something went seriously wrong, there are 5 instances of a card");
                originalStream.println("---------------------------------------------------------------");
            }
        }

        //------------Straight-------------

        if (cList.size() == 5) {

            Card c1 = cList.get(0);
            Card c2 = cList.get(1);
            Card c3 = cList.get(2);
            Card c4 = cList.get(3);
            Card c5 = cList.get(4);

            int iD1 = c1.getId();
            int iD2 = c2.getId();
            int iD3 = c3.getId();
            int iD4 = c4.getId();
            int iD5 = c5.getId();

            Boolean five = false;
            Boolean four = false;

            if (iD1 + 1 == iD2 && iD2 + 1 == iD3 && iD3 + 1 == iD4 && iD4 + 1 == iD5){
                originalStream.println("Found Straight 5 " + c1 + c2 + c3 + c4 + c5); five = true; currentPlayer.win(5);
            }
            if (!five) {
                if (iD1 + 1 == iD2 && iD2 + 1 == iD3 && iD3 + 1 == iD4){ originalStream.println("Found Straight 4 " + c1 + c2 + c3 + c4); four = true; currentPlayer.win(4);}
                if (iD1 + 1 == iD2 && iD2 + 1 == iD3 && iD3 + 1 == iD5){ originalStream.println("Found Straight 4 " + c1 + c2 + c3 + c5); four = true; currentPlayer.win(4);}
                if (iD1 + 1 == iD2 && iD2 + 1 == iD4 && iD4 + 1 == iD5){ originalStream.println("Found Straight 4 " + c1 + c2 + c4 + c5); four = true; currentPlayer.win(4);}
                if (iD1 + 1 == iD3 && iD3 + 1 == iD4 && iD4 + 1 == iD5){ originalStream.println("Found Straight 4 " + c1 + c3 + c4 + c5); four = true; currentPlayer.win(4);}
                if (iD2 + 1 == iD3 && iD3 + 1 == iD4 && iD4 + 1 == iD5){ originalStream.println("Found Straight 4 " + c2 + c3 + c4 + c5); four = true; currentPlayer.win(4);}

                if (!four){
                    if (iD1 + 1 == iD2 && iD2 + 1 == iD3){ originalStream.println("Found Straight 3 " + c1 + c2 + c3); currentPlayer.win(3);}
                    if (iD1 + 1 == iD2 && iD2 + 1 == iD4){ originalStream.println("Found Straight 3 " + c1 + c2 + c4); currentPlayer.win(3);}
                    if (iD1 + 1 == iD2 && iD2 + 1 == iD5){ originalStream.println("Found Straight 3 " + c1 + c2 + c5); currentPlayer.win(3);}
                    if (iD1 + 1 == iD3 && iD3 + 1 == iD4){ originalStream.println("Found Straight 3 " + c1 + c3 + c4); currentPlayer.win(3);}
                    if (iD1 + 1 == iD3 && iD3 + 1 == iD5){ originalStream.println("Found Straight 3 " + c1 + c3 + c5); currentPlayer.win(3);}
                    if (iD1 + 1 == iD4 && iD4 + 1 == iD5){ originalStream.println("Found Straight 3 " + c1 + c4 + c5); currentPlayer.win(3);}
                    if (iD2 + 1 == iD3 && iD3 + 1 == iD4){ originalStream.println("Found Straight 3 " + c2 + c3 + c4); currentPlayer.win(3);}
                    if (iD2 + 1 == iD3 && iD3 + 1 == iD5){ originalStream.println("Found Straight 3 " + c2 + c3 + c5); currentPlayer.win(3);}
                    if (iD2 + 1 == iD4 && iD4 + 1 == iD5){ originalStream.println("Found Straight 3 " + c2 + c4 + c5); currentPlayer.win(3);}
                    if (iD3 + 1 == iD4 && iD4 + 1 == iD5){ originalStream.println("Found Straight 3 " + c3 + c4 + c5); currentPlayer.win(3);}
                }
            }
        }

        else if (cList.size() == 4){

            Card c1 = cList.get(0);
            Card c2 = cList.get(1);
            Card c3 = cList.get(2);
            Card c4 = cList.get(3);

            int iD1 = c1.getId();
            int iD2 = c2.getId();
            int iD3 = c3.getId();
            int iD4 = c4.getId();

            Boolean four = false;

            if (iD1 + 1 == iD2 && iD2 + 1 == iD3 && iD3 + 1 == iD4){ originalStream.println("Found Straight 4 " + c1 + c2 + c3 + c4); four = true; currentPlayer.win(4);}

            if (!four){
                if (iD1 + 1 == iD2 && iD2 + 1 == iD3){ originalStream.println("Found Straight 3 " + c1 + c2 + c3); currentPlayer.win(3);}
                if (iD1 + 1 == iD2 && iD2 + 1 == iD4){ originalStream.println("Found Straight 3 " + c1 + c2 + c4); currentPlayer.win(3);}
                if (iD1 + 1 == iD3 && iD3 + 1 == iD4){ originalStream.println("Found Straight 3 " + c1 + c3 + c4); currentPlayer.win(3);}
                if (iD2 + 1 == iD3 && iD3 + 1 == iD4){ originalStream.println("Found Straight 3 " + c2 + c3 + c4); currentPlayer.win(3);}
            }
        }

        //------------Flush--------------

        int numberofS = 0;
        int numberofH = 0;
        int numberofD = 0;
        int numberofC = 0;

        //noinspection Duplicates
        for (Card card: cList) {
            switch (card.suit){
                case "S":
                    numberofS++;
                    break;
                case "H":
                    numberofH++;
                    break;
                case "D":
                    numberofD++;
                    break;
                case "C":
                    numberofC++;
                    break;
                default:
                    originalStream.println("Suit not recognized!");
                    break;
            }
        }

        saList.add(numberofS);
        saList.add(numberofH);
        saList.add(numberofD);
        saList.add(numberofC);

        for (int suitAmount: saList){
            if (suitAmount > 3){
                if (suitAmount == 4){
                    currentPlayer.win(4);
                    originalStream.println("Found Flush 4");
                }
                else if (suitAmount == 5){
                    currentPlayer.win(5);
                    originalStream.println("Found Flush 5");
                }
            }
        }

        //------------sum15-------------

        if (cList.size() == 4){
            originalStream.println("scanning size 4");
            int c1 = cList.get(0).getValue();
            int c2 = cList.get(1).getValue();
            int c3 = cList.get(2).getValue();
            int c4 = cList.get(3).getValue();

            if (c1 + c2 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c1 + c2);}
            if (c1 + c3 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c1 + c3);}
            if (c1 + c4 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c1 + c4);}
            if (c2 + c3 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c2 + c3);}
            if (c2 + c4 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c2 + c4);}
            if (c3 + c4 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c3 + c4);}

            if (c1 + c2 + c3 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c1 + c2 + c3);}
            if (c1 + c2 + c4 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c1 + c2 + c4);}
            if (c1 + c3 + c4 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c1 + c3 + c4);}
            if (c2 + c3 + c4 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c2 + c3 + c4);}

            if (c1 + c2 + c3 + c4 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c1 + c2 + c3 + c4);}
        }

        else if(cList.size() == 5){
            int v1 = cList.get(0).getValue();
            int v2 = cList.get(1).getValue();
            int v3 = cList.get(2).getValue();
            int v4 = cList.get(3).getValue();
            int v5 = cList.get(4).getValue();

            Card c1 = cList.get(0);
            Card c2 = cList.get(1);
            Card c3 = cList.get(2);
            Card c4 = cList.get(3);
            Card c5 = cList.get(4);

            if (v1 + v2 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c1 + c2);}
            if (v1 + v3 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c1 + c3);}
            if (v1 + v4 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c1 + c4);}
            if (v1 + v5 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c1 + c5);}
            if (v2 + v3 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c2 + c3);}
            if (v2 + v4 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c2 + c4);}
            if (v2 + v5 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c2 + c5);}
            if (v3 + v4 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c3 + c4);}
            if (v3 + v5 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c3 + c5);}
            if (v4 + v5 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c4 + c5);}

            if (v1 + v2 + v3 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c1 + c2 + c3);}
            if (v1 + v2 + v4 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c1 + c2 + c4);}
            if (v1 + v2 + v5 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c1 + c2 + c5);}
            if (v1 + v3 + v4 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c1 + c3 + c4);}
            if (v1 + v3 + v5 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c1 + c3 + c5);}
            if (v2 + v3 + v4 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c2 + c3 + c4);}
            if (v2 + v3 + v5 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c2 + c3 + c5);}
            if (v2 + v4 + v5 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c2 + c4 + c5);}

            if (v1 + v2 + v3 + v4 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c1 + v2 + c3 + c4);}
            if (v1 + v2 + v3 + v5 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c1 + c2 + c3 + c5);}
            if (v1 + v2 + v4 + v5 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c1 + c2 + c4 + c5);}
            if (v1 + v3 + v4 + v5 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c1 + c3 + c4 + c5);}
            if (v2 + v3 + v4 + v5 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c2 + c3 + c4 + c5);}

            if (v1 + v2 + v3 + v4 + v5 == 15){currentPlayer.win(2); originalStream.println("Found 15 " + c1 + c2 + c3 + c4 + c5);}
        }

        originalStream.println("Inspected " + currentPlayer + " + [" + flipCard + "]");
        int finalScore = currentPlayer.getScore() - initialScore;

        return finalScore;
    }

    public int getPokerScore(Hand hand){

        hand.sort();

        ArrayList<Card> cList = hand.getDeckList(); //card List
        ArrayList<Integer> vList = new ArrayList<>(); //card id List
        //ArrayList<String> sList = hand.getSuitList(); //suit List
        ArrayList<Integer> saList = new ArrayList<>(); //suit amount List

//        LinkedList<core.Card> relativeList = new LinkedList<>();

        Boolean flush = false;
        Boolean straight = false;

        Card c0 = cList.get(0);
        Card c1 = cList.get(1);
        Card c2 = cList.get(2);
        Card c3 = cList.get(3);
        Card c4 = cList.get(4);

        int numberof2 = 0;
        int numberof3 = 0;
        int numberof4 = 0;
        int numberof5 = 0;
        int numberof6 = 0;
        int numberof7 = 0;
        int numberof8 = 0;
        int numberof9 = 0;
        int numberof10 = 0;
        int numberofJ = 0;
        int numberofQ = 0;
        int numberofK = 0;
        int numberofA = 0;

        for (Card card: cList){
            switch (card.face){
                case "2":
                    numberof2++;
                    break;
                case "3":
                    numberof3++;
                    break;
                case "4":
                    numberof4++;
                    break;
                case "5":
                    numberof5++;
                    break;
                case "6":
                    numberof6++;
                    break;
                case "7":
                    numberof7++;
                    break;
                case "8":
                    numberof8++;
                    break;
                case "9":
                    numberof9++;
                    break;
                case "10":
                    numberof10++;
                    break;
                case "J":
                    numberofJ++;
                    break;
                case "Q":
                    numberofQ++;
                    break;
                case "K":
                    numberofK++;
                    break;
                case "A":
                    numberofA++;
                    break;
                default:
                    originalStream.println("Invalid input");
                    break;
            }
        }

        vList.add(numberof2);
        vList.add(numberof3);
        vList.add(numberof4);
        vList.add(numberof5);
        vList.add(numberof6);
        vList.add(numberof7);
        vList.add(numberof8);
        vList.add(numberof9);
        vList.add(numberof10);
        vList.add(numberofJ);
        vList.add(numberofQ);
        vList.add(numberofK);
        vList.add(numberofA);

        int numberofS = 0;
        int numberofH = 0;
        int numberofD = 0;
        int numberofC = 0;

        //noinspection Duplicates
        for (Card card: cList) {
            switch (card.suit){
                case "S":
                    numberofS++;
                    break;
                case "H":
                    numberofH++;
                    break;
                case "D":
                    numberofD++;
                    break;
                case "C":
                    numberofC++;
                    break;
                default:
                    originalStream.println("Suit not recognized!");
                    break;
            }
        }

        saList.add(numberofS);
        saList.add(numberofH);
        saList.add(numberofD);
        saList.add(numberofC);

        for (int suitAmount: saList){
            if (suitAmount == 5){
                flush = true;
            }
        }

        straight = true;
        for (int i = 0; i < cList.size()-1; i++){
            if (!(cList.get(i).id == cList.get(i+1).id -1)){
                straight = false;
            }
        }

        if (straight && flush){

            hand.win(10);
            if (cList.get(cList.size()-1).face == "A"){
                originalStream.println("###Royal Flush###" + hand);
            }
            else {
                originalStream.println("Straight Flush " + hand);
            }
            return 10;
        }


        for (int maxAmount: vList) {
            if (maxAmount == 4) {
                hand.win(9);
                originalStream.println("Four of a kind " + hand);
                return 9; //found 4-of-a-kind
            }
        }

        ArrayList<Integer> vCopy = vList;

        for (int i = 0; i < vCopy.size(); i++) {
            if (vCopy.get(i) == 3) {
                vCopy.remove(i);

                for (int maxAmount: vCopy) {
                    if (maxAmount == 2) {
                        hand.win(8);
                        originalStream.println("Full Hpose " + hand);
                        return 8; //found full house
                    }
                }

                hand.win(5);
                originalStream.println("Three of a kind " + hand);
                return 5; //found 3
            }
        }

        if (flush){
            hand.win(7);
            originalStream.println("Flush " + hand);
            return 7;
        }

        if (straight){
            hand.win(6);
            originalStream.println("Straight " + hand);
            return 6;
        }

        vCopy = vList;

        for (int i = 0; i < vCopy.size(); i++) {
            if (vCopy.get(i) == 2) {
                vCopy.remove(i);

                for (int maxAmount: vCopy) {
                    if (maxAmount == 2) {
                        hand.win(4);
                        originalStream.println("Two Pair " + hand);
                        return 4; //found double Pairs
                    }
                }

                hand.win(3);
                originalStream.println("Pair " + hand);
                return 3; //found Pairs
            }
        }

        hand.win(1);
        originalStream.println("High card " + hand);
        return
                1;
    }

}
