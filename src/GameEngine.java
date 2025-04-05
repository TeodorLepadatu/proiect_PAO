import java.util.HashMap;
import java.util.Scanner;

public class GameEngine {
    private HumanPlayer player1;
    private HumanPlayer player2;
    private AIPlayer aiPlayer1;
    private AIPlayer aiPlayer2;

    private GameEngine instance = null;

    private GameEngine() {
        int scenario = 99;  //ww2 map TO DO
        int gameMode = 99; //1 for 1v1, 2 for 1vAI, 3 for AIvAI
        while (gameMode < 1 || gameMode > 3) {
            System.out.println("Choose a scenario!");
            System.out.println("1. 1v1");
            System.out.println("2. 1vAI");
            System.out.println("3. AIvAI");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            Scanner scanner = new Scanner(System.in);
            scenario = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            gameMode = scanner.nextInt();
            scanner.nextLine();
            if (gameMode == 4) {
                System.out.println("Exiting the game.");
                return;
            }
            if (gameMode > 4 || gameMode < 1) {
                System.out.println("Invalid choice. Please try again.");
                return;
            }
        }
        if(gameMode == 1) {
            System.out.println("Enter player 1 name: ");
            Scanner scanner = new Scanner(System.in);
            String player1Name = scanner.nextLine();
            System.out.println("Enter player 2 name: ");
            String player2Name = scanner.nextLine();
            HashMap<String, Card> player1Hand = new HashMap<>();
            HashMap<String, Card> player2Hand = new HashMap<>();
            this.player1 = new HumanPlayer(player1Name, player1Hand);
            this.player2 = new HumanPlayer(player2Name, player2Hand);
        } else if (gameMode == 2) {
            System.out.println("Enter player name: ");
            Scanner scanner = new Scanner(System.in);
            String playerName = scanner.nextLine();
            HashMap<String, Card> playerHand = new HashMap<>();
            this.player1 = new HumanPlayer(playerName, playerHand);
            this.aiPlayer1 = new AIPlayer("AI Player", new HashMap<>());
        } else if (gameMode == 3) {
            this.aiPlayer1 = new AIPlayer("AI Player 1", new HashMap<>());
            this.aiPlayer2 = new AIPlayer("AI Player 2", new HashMap<>());
        }
    }
    GameEngine getInstance() {
        if (instance == null) {
            instance = new GameEngine();
        }
        return instance;
    }

    public boolean isGameOver() {
        if(player1.hasLost()) {
            System.out.println(player2.name + "is victorious!");
            return true;
        }
        if(player2.hasLost()) {
            System.out.println(player1.name + "is victorious!");
            return true;
        }
        return false;
    }

    public void Run() {
        while(!isGameOver()) {
            playRound();
        }
    }

    private void playRound() {
        // Implement the logic for a single round of the game
    }
}
