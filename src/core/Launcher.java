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
    boolean human = false;
    String ai1 = "Hybrid";
    String ai2 = "Hybrid";
    int gameAmount = 1;

    public Launcher(){

        boolean gotInput = false;
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
                human = true;
                gotInput = true;
            } else if (inputString.equals("2")) {
                human = false;
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
            gameAmount = (Integer.parseInt(inputString));
            gotInput = true;
        }

        gotInput = false;
        while (!gotInput) {
            System.out.println("Choose an AI difficulty");
            System.out.println("   [1] - Beginner");
            System.out.println("   [2] - Intermediate");
            System.out.println("   [3] - Advanced");
            System.out.println("   [3] - Legacy (warning - slow)");
            InputStream streamer = System.in;

            InputStreamReader sr = new InputStreamReader(streamer);
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            try {
                inputString = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (inputString.equals("1")) {
                ai1 = "Basic";
                gotInput = true;
            } else if (inputString.equals("2")) {
                ai1 = "Lite";
                gotInput = true;
            } else if (inputString.equals("3")) {
                ai1 = "Hybrid";
                gotInput = true;
            } else if (inputString.equals("4")) {
                ai1 = "Full";
                gotInput = true;
            }else {
                System.out.println("Invalid Input");
            }
        }

        if (!human) {
            gotInput = false;
            while (!gotInput) {
                System.out.println("Choose another AI difficulty");
                System.out.println("   [1] - Beginner");
                System.out.println("   [2] - Intermediate");
                System.out.println("   [3] - Advanced");
                System.out.println("   [3] - Legacy (warning - slow)");
                InputStream streamer = System.in;

                InputStreamReader sr = new InputStreamReader(streamer);
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                try {
                    inputString = input.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (inputString.equals("1")) {
                    ai2 = "Basic";
                    gotInput = true;
                } else if (inputString.equals("2")) {
                    ai2 = "Lite";
                    gotInput = true;
                } else if (inputString.equals("3")) {
                    ai2 = "Hybrid";
                    gotInput = true;
                } else if (inputString.equals("4")) {
                    ai2 = "Full";
                    gotInput = true;
                } else {
                    System.out.println("Invalid Input");
                }
            }
        }

        playGames(gameAmount);

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
            Game game = new Game(i+1, this);
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
