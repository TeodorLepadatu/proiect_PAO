import java.util.*;

public class GameEngine {
    private final ArrayList<GenericPlayer> players;
    private static GameEngine instance = null;

    private GameEngine() {
        players = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        // Choose scenario
        System.out.println("Choose a scenario:");
        System.out.println("1: Germany vs. USSR");
        System.out.println("2: Germany vs. France");
        System.out.println("3: Italy vs. France");
        System.out.println("4: Italy vs. England");
        System.out.println("5: Germany vs. England");
        int scenario = scanner.nextInt();
        scanner.nextLine();

        // Choose game mode
        System.out.println("Choose game mode:");
        System.out.println("1: Player vs Player");
        System.out.println("2: Player vs AI");
        System.out.println("3: AI vs AI");
        int gameMode = scanner.nextInt();
        scanner.nextLine();

        Scenario scen = new Scenario(scenario);

        ArrayList<Card> deck1;
        ArrayList<Card> deck2;
        String name1, name2;

        switch (scenario) {
            case 1 -> {
                deck1 = scen.getGermanyDeck();
                deck2 = scen.getSovietDeck();
                name1 = "Germany";
                name2 = "USSR";
            }
            case 2 -> {
                deck1 = scen.getGermanyDeck();
                deck2 = scen.getFranceDeck();
                name1 = "Germany";
                name2 = "France";
            }
            case 3 -> {
                deck1 = scen.getItalyDeck();
                deck2 = scen.getFranceDeck();
                name1 = "Italy";
                name2 = "France";
            }
            case 4 -> {
                deck1 = scen.getItalyDeck();
                deck2 = scen.getUKDeck();
                name1 = "Italy";
                name2 = "England";
            }
            case 5 -> {
                deck1 = scen.getGermanyDeck();
                deck2 = scen.getUKDeck();
                name1 = "Germany";
                name2 = "England";
            }
            default -> {
                System.out.println("Invalid scenario, defaulting to Germany vs USSR.");
                deck1 = scen.getGermanyDeck();
                deck2 = scen.getSovietDeck();
                name1 = "Germany";
                name2 = "USSR";
            }
        }

        NationConfig config1 = new NationConfig(deck1);
        NationConfig config2 = new NationConfig(deck2);

        double avgPower1 = config1.calculateAveragePower(deck1);
        double avgPower2 = config2.calculateAveragePower(deck2);

        int moves1 = avgPower1 >= avgPower2 ? config1.getMovesPerTurn() : config2.getMovesPerTurn();
        int moves2 = avgPower1 < avgPower2 ? config1.getMovesPerTurn() : config2.getMovesPerTurn();

        // Sort decks by total card power (descending)
        Comparator<Card> powerComparator = (a, b) -> {
            int powerA = a.hp + (a instanceof AttackingCard ? ((AttackingCard) a).getAttackPower() : (a instanceof HealingCard ? ((HealingCard) a).getHealPower() : 0));
            int powerB = b.hp + (b instanceof AttackingCard ? ((AttackingCard) b).getAttackPower() : (b instanceof HealingCard ? ((HealingCard) b).getHealPower() : 0));
            return Integer.compare(powerB, powerA);
        };
        deck1.sort(powerComparator);
        deck2.sort(powerComparator);

        GenericPlayer player1, player2;

        switch (gameMode) {
            case 1 -> {
                player1 = HumanPlayer.getInstance(name1 + " Player", new HashMap<>(), moves1);
                player2 = HumanPlayer.getInstance(name2 + " Player", new HashMap<>(), moves2);
            }
            case 2 -> {
                System.out.println("Choose AI difficulty (1: Easy, 2: Medium, 3: Hard):");
                int difficulty = scanner.nextInt();
                System.out.println("Choose the nation that you want to play:");
                System.out.println("1: " + name1);
                System.out.println("2: " + name2);
                int playerNation = scanner.nextInt();
                scanner.nextLine();
                if (playerNation == 1) {
                    player1 = HumanPlayer.getInstance(name1 + " Player", new HashMap<>(), moves1);
                    player2 = AIPlayer.getInstance(name2 + " AI", new HashMap<>(), moves2, difficulty);
                } else if (playerNation == 2) {
                    player1 = AIPlayer.getInstance(name1 + " AI", new HashMap<>(), moves1, difficulty);
                    player2 = HumanPlayer.getInstance(name2 + " Player", new HashMap<>(), moves2);
                } else {
                    System.out.println("Invalid choice. Defaulting to a random nation.");
                    Random random = new Random();
                    int randomNation = random.nextInt(2) + 1;
                    if (randomNation == 1) {
                        player1 = HumanPlayer.getInstance(name1 + " Player", new HashMap<>(), moves1);
                        player2 = AIPlayer.getInstance(name2 + " AI", new HashMap<>(), moves2, difficulty);
                    } else{
                        player1 = AIPlayer.getInstance(name1 + " AI", new HashMap<>(), moves1, difficulty);
                        player2 = HumanPlayer.getInstance(name2 + " Player", new HashMap<>(), moves2);
                    }
                }
            }
            case 3 -> {
                System.out.println("Choose AI1 difficulty (1: Easy, 2: Medium, 3: Hard):");
                int difficulty1 = scanner.nextInt();
                System.out.println("Choose AI2 difficulty (1: Easy, 2: Medium, 3: Hard):");
                int difficulty2 = scanner.nextInt();
                player1 = AIPlayer.getInstance(name1 + " AI1", new HashMap<>(), moves1, difficulty1);
                player2 = AIPlayer.getInstance(name2 + " AI2", new HashMap<>(), moves2, difficulty2);
            }
            default -> {
                System.out.println("Invalid game mode. Defaulting to PvP.");
                player1 = HumanPlayer.getInstance(name1 + " Player", new HashMap<>(), moves1);
                player2 = HumanPlayer.getInstance(name2 + " Player", new HashMap<>(), moves2);
            }
        }

        for (Card card : deck1) {
            player1.hand.put(card.name, card);
        }
        for (Card card : deck2) {
            player2.hand.put(card.name, card);
        }

        players.add(player1);
        players.add(player2);
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
            System.out.println(players.getFirst().name + " is victorious!");
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
        int movesAllowed = currentPlayer.movesPerTurn;
        System.out.println("\n" + currentPlayer.name + "'s turn. They are allowed " + movesAllowed + " move(s) this turn.");

        for (int move = 1; move <= movesAllowed; move++) {
            GraphicsInterface.displayHands(currentPlayer, opponent);
            if (currentPlayer.hand.isEmpty()) {
                System.out.println(currentPlayer.name + " has no more cards to play.");
                break;
            }
            System.out.println("Move " + move + " of " + movesAllowed + ":");

            if (currentPlayer instanceof HumanPlayer) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Choose a card to play (enter the card name):");
                for (String cardName : currentPlayer.hand.keySet()) {
                    System.out.println(cardName);
                }
                String cardName = scanner.nextLine();
                if (!currentPlayer.hand.containsKey(cardName)) {
                    System.out.println("Invalid card name. Skipping this move.");
                    continue;
                }
                Card currentCard = currentPlayer.hand.get(cardName);
                Card targetCard = null;
                if (currentCard instanceof AttackingCard) {
                    if (opponent.hand.isEmpty()) {
                        System.out.println("Opponent has no cards to target! Skipping this move.");
                        continue;
                    }
                    System.out.println("Choose a target card from the opponent's hand:");
                    for (String targetCardName : opponent.hand.keySet()) {
                        System.out.println(targetCardName);
                    }
                    String targetCardName = scanner.nextLine();
                    if (!opponent.hand.containsKey(targetCardName)) {
                        System.out.println("Invalid target card. Skipping this move.");
                        continue;
                    }
                    targetCard = opponent.hand.get(targetCardName);
                } else if (currentCard instanceof HealingCard) {
                    System.out.println("Choose a target card from your own hand to heal:");
                    for (String targetCardName : currentPlayer.hand.keySet()) {
                        System.out.println(targetCardName);
                    }
                    String targetCardName = scanner.nextLine();
                    if (!currentPlayer.hand.containsKey(targetCardName)) {
                        System.out.println("Invalid target card. Skipping this move.");
                        continue;
                    }
                    targetCard = currentPlayer.hand.get(targetCardName);
                }
                currentPlayer.playCard(cardName, targetCard, opponent);
            } else if (currentPlayer instanceof AIPlayer ai) {
                ArrayList<ArrayList<Integer>> moveChoices = ai.chooseWhatToPlay(opponent, ai.difficulty);

                if (moveChoices == null || moveChoices.isEmpty()) {
                    System.out.println(currentPlayer.name + " did not choose valid moves. Skipping turn.");
                    return;
                }

                for (ArrayList<Integer> moveChoice : moveChoices) {
                    if (moveChoice.size() < 2) continue;

                    String[] currentKeys = currentPlayer.hand.keySet().toArray(new String[0]);
                    String[] opponentKeys = opponent.hand.keySet().toArray(new String[0]);
                    int cardIndex = moveChoice.get(0);
                    int targetIndex = moveChoice.get(1);

                    if (cardIndex >= currentKeys.length) continue;
                    String cardName = currentKeys[cardIndex];
                    Card currentCard = currentPlayer.hand.get(cardName);
                    Card targetCard = null;

                    if (currentCard instanceof AttackingCard) {
                        if (targetIndex >= opponentKeys.length) continue;
                        String targetCardName = opponentKeys[targetIndex];
                        targetCard = opponent.hand.get(targetCardName);
                        System.out.println(currentPlayer.name + " played " + cardName + " to attack " + targetCardName + ".");
                    } else if (currentCard instanceof HealingCard) {
                        if (targetIndex >= currentKeys.length) continue;
                        String targetCardName = currentKeys[targetIndex];
                        targetCard = currentPlayer.hand.get(targetCardName);
                        System.out.println(currentPlayer.name + " played " + cardName + " to heal " + targetCardName + ".");
                    }

                    currentPlayer.playCard(cardName, targetCard, opponent);
                }
            }
        }
    }
}