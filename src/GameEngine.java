import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class GameEngine {
    private ArrayList<GenericPlayer> players;
    private final int scenario;
    private int gameMode;
    private static GameEngine instance = null;

    private GameEngine() {
        players = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose a scenario!");
        scenario = scanner.nextInt();
        while (gameMode < 1 || gameMode > 3) {
            System.out.println("Choose a gamemode!");
            System.out.println("1. 1v1");
            System.out.println("2. 1vAI");
            System.out.println("3. AIvAI");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            gameMode = scanner.nextInt();
            scanner.nextLine();
            if (gameMode == 4) {
                System.out.println("Exiting the game.");
                System.exit(0);
                return;
            }
            if (gameMode > 4 || gameMode < 1) {
                System.out.println("Invalid choice. Please try again.");
                return;
            }
        }
        if (gameMode == 1) {
            System.out.println("Enter player 1 name: ");
            String player1Name = scanner.nextLine();
            System.out.println("Enter player 2 name: ");
            String player2Name = scanner.nextLine();
            HashMap<String, Card> player1Hand = new HashMap<>();
            HashMap<String, Card> player2Hand = new HashMap<>();
            players.add(HumanPlayer.getInstance(player1Name, player1Hand));
            players.add(HumanPlayer.getInstance(player2Name, player2Hand));
        } else if (gameMode == 2) {
            System.out.println("Enter player name: ");
            String playerName = scanner.nextLine();
            HashMap<String, Card> playerHand = new HashMap<>();
            players.add(HumanPlayer.getInstance(playerName, playerHand));
            System.out.println("Choose AI difficulty:");
            System.out.println("1. Easy");
            System.out.println("2. Medium");
            System.out.println("3. Hard");
            int difficulty = scanner.nextInt();
            players.add(AIPlayer.getInstance("AI Player", new HashMap<>(), difficulty));
        } else {
            System.out.println("Choose AI 1 difficulty:");
            System.out.println("1. Easy");
            System.out.println("2. Medium");
            System.out.println("3. Hard");
            int difficulty = scanner.nextInt();
            players.add(AIPlayer.getInstance("AI Player 1", new HashMap<>(), difficulty));
            System.out.println("Choose AI 2 difficulty:");
            System.out.println("1. Easy");
            System.out.println("2. Medium");
            System.out.println("3. Hard");
            difficulty = scanner.nextInt();
            players.add(AIPlayer.getInstance("AI Player 2", new HashMap<>(), difficulty));
        }
    }

    public static GameEngine getInstance() {
        if (instance == null) {
            instance = new GameEngine();
        }
        return instance;
    }

    public boolean isGameOver() {
        if (players.get(0).hasLost()) {
            System.out.println(players.get(1).name + " is victorious!");
            return true;
        }
        if (players.get(1).hasLost()) {
            System.out.println(players.get(0).name + " is victorious!");
            return true;
        }
        return false;
    }

    public void run() {
        int turn = 0;
        while (!isGameOver()) {
            playRound(turn);
            turn = (turn + 1) % 2;
        }
    }

    private void playRound(int turn) {
        GenericPlayer currentPlayer = players.get(turn);
        GenericPlayer opponent = players.get((turn + 1) % 2);
        System.out.println(currentPlayer.name + "'s turn.");

        if (currentPlayer instanceof HumanPlayer) {
            System.out.println("Choose a card to play:");
            for (String cardName : currentPlayer.hand.keySet()) {
                System.out.println(cardName);
            }
            Scanner scanner = new Scanner(System.in);
            String cardName = scanner.nextLine();
            Card targetCard = null;
            Card currentCard = currentPlayer.hand.get(cardName);
            if (currentCard instanceof AttackingCard) {
                System.out.println("Choose a target card:");
                for (String targetCardName : opponent.hand.keySet()) {
                    System.out.println(targetCardName);
                }
                String targetCardName = scanner.nextLine();
                targetCard = opponent.hand.get(targetCardName);
            } else if (currentCard instanceof HealingCard) {
                System.out.println("Choose a target card to heal:");
                for (String targetCardName : currentPlayer.hand.keySet()) {
                    System.out.println(targetCardName);
                }
                String targetCardName = scanner.nextLine();
                targetCard = currentPlayer.hand.get(targetCardName);
            }
            currentPlayer.playCard(cardName, targetCard, opponent);
        } else if (currentPlayer instanceof AIPlayer) {
            var cardToPlay = ((AIPlayer) currentPlayer).chooseWhatToPlay(opponent, ((AIPlayer) currentPlayer).difficulty);
            var cardIndex = cardToPlay.get(0);
            var targetIndex = cardToPlay.get(1);
            String cardName = (String) currentPlayer.hand.keySet().toArray()[cardIndex];
            String targetCardName = (String) opponent.hand.keySet().toArray()[targetIndex];
            Card targetCard = opponent.hand.get(targetCardName);
            currentPlayer.playCard(cardName, targetCard, opponent);
        }
    }
}