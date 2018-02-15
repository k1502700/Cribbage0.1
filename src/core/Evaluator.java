package core;

import java.util.ArrayList;

public class Evaluator {

    String game = "";

    public Evaluator(String gameType){
        game = gameType;
    }

    public int getCribbageHandScore(Hand currentPlayer, Card flipCard){

        System.out.println("Inspecting " + currentPlayer + " + [" + flipCard + "]");

        if (currentPlayer.name == "Crib"){
            System.out.println("Found the Crib");
        }

        Hand calculator = new Hand("Calculator");

        for (Card c: currentPlayer.getDeckList()){
            calculator.addCard(c);
        }

        calculator.addCard(flipCard);

        calculator.sort();

        ArrayList<Card> cList = calculator.getDeckList();//card List
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
                case 14:
                    numberofA++;
                    break;
                default:
                    System.out.println("Invalid card id input for evaluation");
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
                currentPlayer.win(2);//TODO: double pair still 2 points
                System.out.println("Found Pair");
            }
            if (cardAmount == 3){
                currentPlayer.win(6);
                System.out.println("Found Three of a Kind");
            }
            if (cardAmount == 4){
                currentPlayer.win(12);
                System.out.println("Found PFour of a Kindair");
            }
            if (cardAmount == 5){
                System.out.println("----------------------------*ALERT*----------------------------");
                System.out.println("Something went seriously wrong, there are 5 instances of a card");
                System.out.println("---------------------------------------------------------------");
            }
        }

        //------------Straight-------------
        //TODO: 3,4,4,5

        if (cList.size() == 5) {

            straight = true;
            Boolean straight5 = false;

            for (int i = 0; i < cList.size() - 1; i++) {
                if (!(cList.get(i).id == cList.get(i + 1).id - 1)) {
                    straight = false;
                }
            }
            if (straight) {
                straight5 = true;
                currentPlayer.win(5);
                System.out.println("Found Straight 5");
            }

            Boolean straight4A = true;
            Boolean straight4B = true;

            if (!straight5) {
                for (int i = 0; i < cList.size() - 2; i++) {
                    if (!(cList.get(i).id == cList.get(i + 1).id - 1)) {
                        straight4A = false;
                    }
                }
                for (int i = 1; i < cList.size() - 1; i++) {
                    if (!(cList.get(i).id == cList.get(i + 1).id - 1)) {
                        straight4B = false;
                    }
                }
            }

            if ((straight4A || straight4B) && !straight5) {
                currentPlayer.win(4);
                System.out.println("Found Straight 4");
            } else if (!(straight4A || straight4B || straight5)) {

                Boolean straight3A = true;
                Boolean straight3B = true;
                Boolean straight3C = true;

                for (int i = 0; i < cList.size() - 3; i++) {
                    if (!(cList.get(i).id == cList.get(i + 1).id - 1)) {
                        straight3A = false;
                    }
                }
                for (int i = 1; i < cList.size() - 2; i++) {
                    if (!(cList.get(i).id == cList.get(i + 1).id - 1)) {
                        straight3B = false;
                    }
                }
                for (int i = 2; i < cList.size() - 1; i++) {
                    if (!(cList.get(i).id == cList.get(i + 1).id - 1)) {
                        straight3C = false;
                    }
                }

                if (straight3A || straight3B || straight3C) {
                    currentPlayer.win(3);
                    System.out.println("Found Straight 3");
                }
            }
        }
        else if (cList.size() == 4){

            straight = true;
            Boolean straight4 = false;

            for (int i = 0; i < cList.size()-1; i++){
                if (!(cList.get(i).id == cList.get(i+1).id -1)){
                    straight = false;
                }
            }
            if (straight) {
                straight4 = true;
                currentPlayer.win(4);
                System.out.println("Found Straight 4");
            }

            Boolean straight3A = true;
            Boolean straight3B = true;

            if (!straight4){
                for (int i = 0; i < cList.size()-2; i++){
                    if (!(cList.get(i).id == cList.get(i+1).id -1)){
                        straight3A = false;
                    }
                }
                for (int i = 1; i < cList.size()-1; i++){
                    if (!(cList.get(i).id == cList.get(i+1).id -1)){
                        straight3B = false;
                    }
                }
            }

            if ((straight3A || straight3B) && !straight4){
                currentPlayer.win(3);
                System.out.println("Found Straight 3");
            }
        }

        //------------Flush--------------

        int numberofS = 0;
        int numberofH = 0;
        int numberofD = 0;
        int numberofC = 0;

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
                    System.out.println("Suit not recognized!");
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
                    System.out.println("Found Flush 4");
                }
                else if (suitAmount == 5){
                    currentPlayer.win(5);
                    System.out.println("Found Flush 5");
                }
            }
        }

        //------------sum15-------------

        if (cList.size() == 4){
            System.out.println("scanning size 4");
            int c1 = cList.get(0).getValue();
            int c2 = cList.get(1).getValue();
            int c3 = cList.get(2).getValue();
            int c4 = cList.get(3).getValue();

            if (c1 + c2 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c1 + c2);}
            if (c1 + c3 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c1 + c3);}
            if (c1 + c4 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c1 + c4);}
            if (c2 + c3 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c2 + c3);}
            if (c2 + c4 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c2 + c4);}
            if (c3 + c4 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c3 + c4);}

            if (c1 + c2 + c3 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c1 + c2 + c3);}
            if (c1 + c2 + c4 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c1 + c2 + c4);}
            if (c1 + c3 + c4 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c1 + c3 + c4);}
            if (c2 + c3 + c4 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c2 + c3 + c4);}

            if (c1 + c2 + c3 + c4 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c1 + c2 + c3 + c4);}
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

            if (v1 + v2 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c1 + c2);}
            if (v1 + v3 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c1 + c3);}
            if (v1 + v4 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c1 + c4);}
            if (v1 + v5 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c1 + c5);}
            if (v2 + v3 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c2 + c3);}
            if (v2 + v4 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c2 + c4);}
            if (v2 + v5 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c2 + c5);}
            if (v3 + v4 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c3 + c4);}
            if (v3 + v5 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c3 + c5);}
            if (v4 + v5 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c4 + c5);}

            if (v1 + v2 + v3 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c1 + c2 + c3);}
            if (v1 + v2 + v4 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c1 + c2 + c4);}
            if (v1 + v2 + v5 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c1 + c2 + c5);}
            if (v1 + v3 + v4 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c1 + c3 + c4);}
            if (v1 + v3 + v5 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c1 + c3 + c5);}
            if (v2 + v3 + v4 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c2 + c3 + c4);}
            if (v2 + v3 + v5 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c2 + c3 + c5);}
            if (v2 + v4 + v5 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c2 + c4 + c5);}

            if (v1 + v2 + v3 + v4 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c1 + v2 + c3 + c4);}
            if (v1 + v2 + v3 + v5 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c1 + c2 + c3 + c5);}
            if (v1 + v2 + v4 + v5 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c1 + c2 + c4 + c5);}
            if (v1 + v3 + v4 + v5 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c1 + c3 + c4 + c5);}
            if (v2 + v3 + v4 + v5 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c2 + c3 + c4 + c5);}

            if (v1 + v2 + v3 + v4 + v5 == 15){currentPlayer.win(2); System.out.println("Found 15 " + c1 + c2 + c3 + c4 + c5);}
        }

        System.out.println("Inspected " + currentPlayer + " + [" + flipCard + "]");

        return 1;
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
                case "1":   //TODO: This is wrong
                    numberof2++;
                    break;
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
                    System.out.println("Invalid input");
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
                    System.out.println("Suit not recognized!");
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
                System.out.println("###Royal Flush###" + hand);
            }
            else {
                System.out.println("Straight Flush " + hand);
            }
            return 10;
        }


        for (int maxAmount: vList) {
            if (maxAmount == 4) {
                hand.win(9);
                System.out.println("Four of a kind " + hand);
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
                        System.out.println("Full Hpose " + hand);
                        return 8; //found full house
                    }
                }

                hand.win(5);
                System.out.println("Three of a kind " + hand);
                return 5; //found 3
            }
        }

        if (flush){
            hand.win(7);
            System.out.println("Flush " + hand);
            return 7;
        }

        if (straight){
            hand.win(6);
            System.out.println("Straight " + hand);
            return 6;
        }

        vCopy = vList;

        for (int i = 0; i < vCopy.size(); i++) {
            if (vCopy.get(i) == 2) {
                vCopy.remove(i);

                for (int maxAmount: vCopy) {
                    if (maxAmount == 2) {
                        hand.win(4);
                        System.out.println("Two Pair " + hand);
                        return 4; //found double Pairs
                    }
                }

                hand.win(3);
                System.out.println("Pair " + hand);
                return 3; //found Pairs
            }
        }

        hand.win(1);
        System.out.println("High card " + hand);
        return
                1;
    }

}
