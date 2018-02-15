package core;

import DecisionMaking.AI;
import DecisionMaking.DecisionMaker;
import DecisionMaking.Human;

import java.util.ArrayList;

public class Hand extends Deck{
    public String name;
    public DecisionMaker dm = new AI();
    public int points = 0; //used for gathered points during the game
    public int score = 0; //used for individual hand scores
    public Hand(){
        name = "";
        deckList = new ArrayList<>();
        dm = new AI();
    }

    public Hand (String name){
        this.name = name;
        deckList = new ArrayList<>();
        dm = new AI();
    }
    public Hand (String name, Boolean isHuman){
        this.name = name;
        deckList = new ArrayList<>();
        if (isHuman){
            dm = new Human();
        }
        else{
            dm = new AI();
        }
    }

    public Card makeMove(Game gameState, String moveType){
        Card play = dm.makeMove(gameState, moveType, this);
        //System.out.println(play);
        //deckList.remove(play);//TODO:Fix this shit
        return play;
    }

    public void draw(Card c){
        deckList.add(c);
    }

    public Card playFirstCard(){
        //TODO: implement chack if there are cards to discard
        System.out.println(name+ " plays: " + deckList.get(deckList.size()-1));
        return deckList.remove(deckList.size()-1);
    }

    public Card playGivenCard(Card card){
        for (Card c: deckList){
            if (card == c){
                deckList.remove(c);
                return c;
            }
        }
        System.out.println("Error - given card not found");
        return card;
    }

    public void win(){
        score++;
    }

    public void addPoints(int points){
        this.points += points;
    }

    public void win(int score){
        this.score+=score;
    }

    @Override
    public String toString() {
        return name + "'s Deck: " + deckList.toString() + " (" + score + "Pts)";
    }
}
