package core;

import java.util.ArrayList;

public class Evaluator {

    String game = "";

    public Evaluator(String gameType){
        game = gameType;
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
                case "1":
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
