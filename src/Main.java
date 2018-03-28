import core.Game;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<String> winners = new ArrayList<>();
        int preacherWins = 0;
        int dennisWins = 1;
        for (int i = 0; i < 9; i++) {
            Game game = new Game();
            winners.add(game.winner.name);
            if (game.winner.name == "Preacher"){
                preacherWins++;
            }
            else {
                dennisWins++;
            }
        }
        System.out.println(winners);
        System.out.println("Dennis: " + dennisWins + " --- Preacher: " + preacherWins);

    }
}