package DecisionMaking;

import core.Card;
import core.Game;
import core.Hand;

public class AI extends DecisionMaker{
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
            System.out.println("Problem");
            return new Card("2");
        }
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
