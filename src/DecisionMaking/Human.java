package DecisionMaking;

import core.Card;
import core.Game;
import core.Hand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Human extends DecisionMaker {
    public Human() {

    }

    @Override
    public Card makeMove(Game gameState, String moveType, Hand currentPlayer) {

        if (moveType == "Play") {
            boolean gotInput = false;
            String inputString = "";
            int cardNum = 0;
            int count = gameState.getCount();
            boolean hasUnderCount = false;

            for (Card c : currentPlayer.getDeckList()) {
                if (c.getValue() + count <= 31) {
                    hasUnderCount = true;
                }
            }

            while (!gotInput) {
                System.out.println("Choose a card to play!");
                System.out.println("Count: " + count + " Discard-pile: " + gameState.getDiscardPile());
                for (int i = 1; i < currentPlayer.getDeckList().size() + 1; i++) {
                    System.out.print("(" + i + ")" + currentPlayer.getDeckList().get(i - 1) + "; ");
                }
                System.out.println("(" + (currentPlayer.getDeckList().size() + 1) + ") Go! ");

                //            System.out.println(currentPlayer.getDeckList());
                InputStream streamer = System.in;

                InputStreamReader sr = new InputStreamReader(streamer);
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                try {
                    inputString = input.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                boolean gotNum = false;
                try {
                    cardNum = Integer.parseInt(inputString) - 1;
                    gotNum = true;
                } catch (Exception e) {
                    System.out.println("Invalid input!");
                }

                if (gotNum) {

                    if (cardNum >= 0 && cardNum < currentPlayer.getDeckList().size()) {
                        if (currentPlayer.getDeckList().get(cardNum).getValue() + count > 31) {
                            System.out.println("Cannot play cards over 31!");
                        }
                        else {
                            gotInput = true;
                        }
                    } else {
                        if (cardNum == (currentPlayer.getDeckList().size())) {
                            if (hasUnderCount) {
                                System.out.println("You must play a card if you have one that can be played!");
                            } else {
                                System.out.println("Go!");
                                return new Card("X", "S");
                            }
                        }
                        else {
                            System.out.println("Invalid Input");
                        }
                    }


                }
            }

            Card c = currentPlayer.getDeckList().get(cardNum);

//            System.out.println(currentPlayer + " played " + c + ", the count is: " + (count + c.getValue()));

            return currentPlayer.playGivenCard(c);
        }

        else {
            boolean gotInput = false;
            String inputString = "";
            int cardNum = 0;
            int count = gameState.getCount();
            boolean hasUnderCount = false;

            for (Card c : currentPlayer.getDeckList()) {
                if (c.getValue() + count <= 31) {
                    hasUnderCount = true;
                }
            }

            while (!gotInput) {
                System.out.println("Choose a card to discard!");
                for (int i = 1; i < currentPlayer.getDeckList().size() + 1; i++) {
                    System.out.print("(" + i + ")" + currentPlayer.getDeckList().get(i - 1) + "; ");
                }
                System.out.println();

                //            System.out.println(currentPlayer.getDeckList());
                InputStream streamer = System.in;

                InputStreamReader sr = new InputStreamReader(streamer);
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                try {
                    inputString = input.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                boolean gotNum = false;
                try {
                    cardNum = Integer.parseInt(inputString) - 1;
                    gotNum = true;
                } catch (Exception e) {
                    System.out.println("Invalid input!");
                }

                if (gotNum) {

                    if (cardNum >= 0 && cardNum < currentPlayer.getDeckList().size()) {
                        if (currentPlayer.getDeckList().get(cardNum).getValue() + count > 31) {
                            System.out.println("Cannot play cards over 31!");
                        }
                        else{
                            gotInput = true;
                        }

                    } else {
                        System.out.println("Invalid Input");
                    }


                }

            }

            Card c = currentPlayer.getDeckList().get(cardNum);

//            System.out.println(currentPlayer + " discarded " + c);

            return currentPlayer.playGivenCard(c);
        }
    }
}