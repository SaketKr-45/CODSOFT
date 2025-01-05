package number_game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {
    public static void main(String[] args) {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            Random random = new Random();

            System.out.println("Welcome to NUMBER GUESSING GAME!!\n");

            HashMap<String , Integer> singlePlayerLeaderboard = new HashMap<>();
            boolean continueGame = true;
            int bestScore = Integer.MAX_VALUE;

            while(continueGame) {
                System.out.println("Choose mode");
                System.out.println("1. Single player");
                System.out.println("2. Dual (challenge your friend)");
                int mode;

                while(true){
                    try {
                        System.out.print("Choose: ");
                        mode = Integer.parseInt(br.readLine());

                        if ( mode < 0 || mode > 2 ){
                            System.out.println("Invalid Input!! Please choose correct options.");
                            continue;
                        }
                        break;
                    }catch(NumberFormatException e){
                        System.out.println("Invalid Entry!! Please choose a number");
                    }
                }

                if (mode == 1){
                    System.out.print("Enter your name: ");
                    String name = br.readLine().toUpperCase().trim().replaceAll("\\s+", " ");

                    System.out.println("\nChoose difficulty:");
                    System.out.println("1. easy (Range: 0 - 50, Attempts: 20");
                    System.out.println("2. medium (Range: 0 - 100, Attempts: 15");
                    System.out.println("3. hard (Range: 0 - 200, Attempts: 10");
                    int difficulty;
                    while (true) {
                        try {
                            System.out.print("Enter your choice: ");
                            difficulty = Integer.parseInt(br.readLine());

                            if (difficulty < 0 || difficulty > 3){
                                System.out.println("Invalid Input!! Choose correct options.");
                                continue;
                            }
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid Entry!! Please choose a number");
                        }
                    }

                    int maxAttempts = difficulty == 1 ? 20 : difficulty == 2 ? 15 : 10;
                    int maxRange = difficulty == 1 ? 50 : difficulty == 2 ? 100 : 200;

                    int score = getSingleplayerScore(br , random , maxAttempts , maxRange);

                    singlePlayerLeaderboard.put(name , score);  // saving  score

                    bestScore = Integer.min(bestScore , score);
                    System.out.printf("Best score: %d\n\n", bestScore);

                    while (true){
                        System.out.print("View Leaderboard (y/n) : ");
                        String viewLeaderboard = br.readLine().trim().toLowerCase();
                        if("y".equals(viewLeaderboard)) {
                            for(Map.Entry<String , Integer> h : singlePlayerLeaderboard.entrySet()){
                                System.out.println(h.getKey() + ": " + h.getValue());
                            }
                            break;
                        }
                        else if("n".equals(viewLeaderboard)){
                            break;
                        }
                        else{
                            System.out.println("Invalid Input!! Choose correct option.");
                        }
                    }

                }

                else if(mode == 2){
                    System.out.println("Enter player 1 name: ");
                    String p1 = br.readLine().toUpperCase().trim().replaceAll("\\s+", " ");
                    System.out.println("Enter player 2 name: ");
                    String p2 = br.readLine().toUpperCase().trim().replaceAll("\\s+", " ");

                    System.out.printf("It's %s turn to choose a number for %s\n" , p1 , p2);
                    int secretNumber2 = getSecretNumber(br);
                    int score2 = getMultiplayerScore(br , secretNumber2 , 15);

                    System.out.printf("It's %s turn to choose a number for %s\n" , p2 , p1);
                    int secretNumber1 = getSecretNumber(br);
                    int score1 = getMultiplayerScore(br , secretNumber1 , 15);

                    System.out.println(score1 == score2 ? "It's a draw!!" : score1 > score2 ? (p2 + " won!!") : (p1 + " won!!"));
                }


                while (true){
                    System.out.print("\nWould you like to play again (y/n) : ");
                    String playAgain = br.readLine().trim().toLowerCase();
                    if("y".equals(playAgain)) break;
                    else if("n".equals(playAgain)){
                        continueGame = false;
                        break;
                    }
                    else{
                        System.out.println("Invalid Input!! Choose correct option.");
                    }
                }
            }
        }catch (IOException e){
            System.out.println("An error occurred while reading input: " + e.getMessage());
        }
    }

    private static int getSingleplayerScore(BufferedReader br, Random random, int maxAttempts, int maxRange) throws IOException {
        int computer = random.nextInt(maxRange + 1);

        int score = 0;

        for (int attempt = maxAttempts ; attempt > 0 ; attempt--){
            int user;
            try {
                System.out.println("\nAttempts left: " + attempt);
                System.out.print("Enter number: ");
                user = Integer.parseInt(br.readLine());
                score++;

                if(user == computer){
                    System.out.println("\nCongratulations!! You won !!");
                    System.out.printf("Your score: %d \n", score);
                    return score;
                }

                System.out.println(computer > user ? "Go Higher." : "Go Lower.");
            }catch (NumberFormatException e){
                System.out.println("Invalid Entry!! Please choose a number");
            }
        }

        System.out.println("You have exhausted all your attempts!! Better luck next time.");
        System.out.printf("Correct number is: %d \n", computer);

        return maxAttempts + 5;  // penalty
    }

    private static int getSecretNumber(BufferedReader br){
        System.out.print("Chosoe number between 0-100: ");
        while(true){
            try{
                return Integer.parseInt(br.readLine());
            } catch (NumberFormatException | IOException e) {
                System.out.println("Invalid Entry!! Please choose a number");
            }
        }
    }

    private static int getMultiplayerScore(BufferedReader br , int secretNumber , int attempts){
        int score = 0;

        for (int attemptsLeft = attempts ; attemptsLeft > 0 ; attemptsLeft--){
            try {
                System.out.printf("Attempts left: %d\n",attemptsLeft);
                System.out.print("your guess: ");
                int guess = Integer.parseInt(br.readLine());
                score++;
                if (guess == secretNumber){
                    System.out.println("Congratulations!! Your guess is correct.");
                    return score;
                }
                System.out.println(secretNumber > guess ? "Go Higher!!" : "Go Lower!!");
            }catch (NumberFormatException | IOException e){
                System.out.println("Invalid Entry!! Please choose a number");
            }
        }

        System.out.println("You have exhausted all your attempts!! Better luck next time.");
        System.out.printf("Correct number is: %d \n", secretNumber);

        return attempts + 5;  // penalty
    }
}
