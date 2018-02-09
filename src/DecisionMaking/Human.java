package DecisionMaking;

import core.Card;
import core.Game;
import core.Hand;

public class Human extends DecisionMaker {
    public Human(){

    }

    @Override
    public Card makeMove(Game gameState, String moveType, Hand currentPlayer) {
        if (moveType == "Play"){
            int count = gameState.getCount();
            for (Card c: currentPlayer.getDeckList()){
                if (c.getValue() + count <= 31){
                    System.out.println(c.getValue());
                    System.out.println(currentPlayer + " played " + c + ", the count is: " + (count+c.getValue()));
                    return currentPlayer.playFirstCard();
                }
            }
            System.out.println("Problem");
            return new Card("2");
        }
        return currentPlayer.playFirstCard();
    }
}
