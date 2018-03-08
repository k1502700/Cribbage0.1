package core;

public class Card {
    String face = "";
    int id = 0;
    int value = 0;
    String suit =""; //Spades, Hearts, Diamonds, Clubs (SHDC)

    public Card(String faceIn){
        face = faceIn;

        switch (faceIn){
            case "2":
                id = 2;
                value = 2;
                break;
            case "3":
                id = 3;
                value = 3;
                break;
            case "4":
                id = 4;
                value = 4;
                break;
            case "5":
                id = 5;
                value = 5;
                break;
            case "6":
                id = 6;
                value = 6;
                break;
            case "7":
                id = 7;
                value = 7;
                break;
            case "8":
                id = 8;
                value = 8;
                break;
            case "9":
                id = 9;
                value = 9;
                break;
            case "10":
                id = 10;
                value = 10;
                break;
            case "J":
                id = 11;
                value = 10;
                break;
            case "Q":
                id = 12;
                value = 10;
                break;
            case "K":
                id = 13;
                value = 10;
                break;
            case "A":
                id = 1;
                value = 1;
                break;
            default:
                System.out.println("Invalid input for Card creation");
                id = 0;
                value = 0;
                break;
        }
    }

    public Card(String inFace, String suitIn){
        face = inFace;
        suit = suitIn;

        switch (inFace){
            case "A":
                id = 1;
                value = 1;
                break;

            case "2":
                id = 2;
                value = 2;
                break;
            case "3":
                id = 3;
                value = 3;
                break;
            case "4":
                id = 4;
                value = 4;
                break;
            case "5":
                id = 5;
                value = 5;
                break;
            case "6":
                id = 6;
                value = 6;
                break;
            case "7":
                id = 7;
                value = 7;
                break;
            case "8":
                id = 8;
                value = 8;
                break;
            case "9":
                id = 9;
                value = 9;
                break;
            case "10":
                id = 10;
                value = 10;
                break;
            case "J":
                id = 11;
                value = 10;
                break;
            case "Q":
                id = 12;
                value = 10;
                break;
            case "K":
                id = 13;
                value = 10;
                break;
            default:
                System.out.println("Invalid input for Card creation");
                id = 0;
                value = 0;
                break;

        }
    }

    public int getValue() {
        return value;
    }

    public boolean isGreater(Card otherCP){
        if (this.id > otherCP.id){
            return true;
        }
        else return false;
    }

    public boolean isEqual(Card otherCP){
        if (this.id == otherCP.id){
            return true;
        }
        else return false;
    }

    public Card compare(Card otherCP){
        if (this.id > otherCP.id){
//            System.out.println("Winner: " + this);
            return this;
        }
        else if (this.id == otherCP.id){
//            System.out.println("They're equal");
            return null;
        }
        else {
//            System.out.println("Winner: " + otherCP);
            return otherCP;
        }
    }

    public int getId(){
        return id;
    }

    @Override
    public String toString(){
        return face + suit;
    }
}
