package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by robert on 2018. 03. 28..
 */
public class Launcher{
    public Launcher(){

        boolean gotInput = false;
        boolean ai = true;
        String inputString  = "";
        while (!gotInput) {
            System.out.println("Choose a Game Mode!");
            System.out.println("   [1] - Player vs AI");
            System.out.println("   [2] - AI vs AI");
            InputStream streamer = System.in;

            InputStreamReader sr = new InputStreamReader(streamer);
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            try {
                inputString = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (inputString.equals("1")) {

                gotInput = true;
            } else if (inputString.equals("2")) {
                ai = true;
                gotInput = true;
            } else {
                System.out.println("Invalid Input");
            }
        }

        gotInput = false;
        while (!gotInput) {
            System.out.println("How Many Games?");
            InputStream streamer = System.in;

            InputStreamReader sr = new InputStreamReader(streamer);
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            try {
                inputString = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            playGames(Integer.parseInt(inputString));
        }




//        originalStream = new PrintStream(new OutputStream() {
//            @Override
//            public void write(int b) throws IOException {
//                //Do nothing
//            }
//        });

    }

    public void playGames(int numberofRounds){
        ArrayList<String> winners = new ArrayList<>();
        int preacherWins = 0;
        int dennisWins = 0;
        for (int i = 0; i < numberofRounds; i++) {
            System.out.println("========== Game: " + (i + 1) + " ==========");
            Game game = new Game();
            winners.add(game.winner.name);
            if (game.winner.name == "Preacher") {
                preacherWins++;
            } else {
                dennisWins++;
            }
            System.out.println("Dennis: " + dennisWins + " --- Preacher: " + preacherWins);
        }
        System.out.println(winners);
        System.out.println("Dennis: " + dennisWins + " --- Preacher: " + preacherWins);
    }

}
