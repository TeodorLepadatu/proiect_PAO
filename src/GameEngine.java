import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class GameEngine {
    private HumanPlayer player1;
    private HumanPlayer player2;
    private AIPlayer aiPlayer1;
    private AIPlayer aiPlayer2;
    private ArrayList<GenericPlayer> players;

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
            players.add(this.player1);
            players.add(this.player2);
        } else if (gameMode == 2) {
            System.out.println("Enter player name: ");
            Scanner scanner = new Scanner(System.in);
            String playerName = scanner.nextLine();
            HashMap<String, Card> playerHand = new HashMap<>();
            this.player1 = new HumanPlayer(playerName, playerHand);
            this.aiPlayer1 = new AIPlayer("AI Player", new HashMap<>());
            players.add(this.player1);
            players.add(this.aiPlayer1);
        } else {
            this.aiPlayer1 = new AIPlayer("AI Player 1", new HashMap<>());
            this.aiPlayer2 = new AIPlayer("AI Player 2", new HashMap<>());
            players.add(this.aiPlayer1);
            players.add(this.aiPlayer2);
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
        int turn = 0;
        while(!isGameOver()) {
            playRound(turn);
            turn = (turn + 1)%2;
        }
    }

    private void playRound(int turn) {
        GenericPlayer currentPlayer = players.get(turn);
        GenericPlayer opponent = players.get((turn + 1) % 2);
        System.out.println(currentPlayer.name + "'s turn.");

        if(currentPlayer instanceof HumanPlayer){
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
            }
            else if(currentCard instanceof HealingCard) {
                System.out.println("Choose a target card to heal:");
                for (String targetCardName : currentPlayer.hand.keySet()) {
                    System.out.println(targetCardName);
                }
                String targetCardName = scanner.nextLine();
                targetCard = currentPlayer.hand.get(targetCardName);
            }
            currentPlayer.playCard(cardName, targetCard, opponent);
        }
        else if(currentPlayer instanceof AIPlayer) {
            var cardToPlay = ((AIPlayer) currentPlayer).chooseWhatToPlay();
            int cardIndex = cardToPlay.get(0);
            int targetIndex = cardToPlay.get(1);
            String cardName = (String) currentPlayer.hand.keySet().toArray()[cardIndex];
            String targetCardName = (String) opponent.hand.keySet().toArray()[targetIndex];
            Card targetCard = opponent.hand.get(targetCardName);
            currentPlayer.playCard(cardName, targetCard, opponent);
        }
    }
}
