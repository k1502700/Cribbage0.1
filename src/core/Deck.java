package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Deck {
    ArrayList<Card> deckList = new ArrayList<>();
    ArrayList<String> suits = new ArrayList<>();

    public Deck(){


        suits.add("S");
        suits.add("H");
        suits.add("D");
        suits.add("C");

        for (String s: suits){
            deckList.add(new Card("2", s));
            deckList.add(new Card("3", s));
            deckList.add(new Card("4", s));
            deckList.add(new Card("5", s));
            deckList.add(new Card("6", s));
            deckList.add(new Card("7", s));
            deckList.add(new Card("8", s));
            deckList.add(new Card("9", s));
            deckList.add(new Card("10", s));
            deckList.add(new Card("J", s));
            deckList.add(new Card("Q", s));
            deckList.add(new Card("K", s));
            deckList.add(new Card("A", s));
        }
        shuffle();
    }

    public Deck(Boolean fullDeck){
        if (fullDeck){
            suits.add("S");
            suits.add("H");
            suits.add("D");
            suits.add("C");

            for (String s: suits){
                deckList.add(new Card("2", s));
                deckList.add(new Card("3", s));
                deckList.add(new Card("4", s));
                deckList.add(new Card("5", s));
                deckList.add(new Card("6", s));
                deckList.add(new Card("7", s));
                deckList.add(new Card("8", s));
                deckList.add(new Card("9", s));
                deckList.add(new Card("10", s));
                deckList.add(new Card("J", s));
                deckList.add(new Card("Q", s));
                deckList.add(new Card("K", s));
                deckList.add(new Card("A", s));
            }
            shuffle();
        }
    }


    public void shuffle(){
        Collections.shuffle(deckList);
    }

    public ArrayList<Card> getDeckList(){
        return deckList;
    }

    public void sort(){
        deckList.sort(new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                return ((Integer)o1.id).compareTo((Integer)o2.id);
            }//TODO: is this a problem? (no suit comparison)
        });
    }

    public int getValueSum(){
        int returnValue = 0;
        for (Card dL: deckList){
            returnValue += dL.getValue();
        }
        return returnValue;
    }

    public ArrayList<String> getSuitList(){
        return suits;
    }

    public ArrayList<Card> shuffleDeck(){//TODO: only shuffles
        shuffle();
        return getDeckList();
    }

    public Card dealCard(){
        return deckList.remove(0);
    }

    public Card discardCard(Card card){
        for (Card c: deckList){
            if (card.id == c.id && card.suit == c.suit){
                deckList.remove(c);
                return c;
            }
        }
        System.out.println("Error - given card not found");
        return card;
    }

    public void addCard(Card c){
        deckList.add(c);
    }

    public void discardAll(){
        while (deckList.size() > 0){
            deckList.remove(0);
        }
    }

    @Override
    public String toString() {
        return deckList.toString();
    }
}
