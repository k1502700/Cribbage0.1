package DecisionMaking;

import core.Card;
import core.Game;
import core.Hand;

public class DecisionMaker {

    public DecisionMaker(){

    }

    public Card makeMove(Game gameState, String moveType, Hand currentPlayer){
//        ArrayList<Card> handList = currentPlayer.getDeckList();
//        Collections.shuffle(handList);
//        if (currentPlayer.discardCard(handList.get(0))){
//            return handList.get(0);
//        }
//        else{
            System.out.println("Something went wrong");
//        }
        return new Card("X");

    }

}
