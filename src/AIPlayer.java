import java.util.ArrayList;
import java.util.HashMap;

public non-sealed class AIPlayer extends GenericPlayer {
    private static AIPlayer instance1 = null;
    private static AIPlayer instance2 = null;
    public int difficulty; // 1 = easy, 2 = medium, 3 = hard

    private AIPlayer(String name, HashMap<String, Card> hand, int movesPerTurn, int difficulty) {
        super(name, hand, movesPerTurn);
        this.difficulty = difficulty;
    }

    public static AIPlayer getInstance(String name, HashMap<String, Card> hand, int movesPerTurn, int difficulty) {
        if (instance1 == null) {
            instance1 = new AIPlayer(name, hand, movesPerTurn, difficulty);
            return instance1;
        } else if (instance2 == null) {
            instance2 = new AIPlayer(name, hand, movesPerTurn, difficulty);
            return instance2;
        }
        return instance1;
    }

    public ArrayList<ArrayList<Integer>> chooseRandomCard(GenericPlayer opponent) {
        ArrayList<ArrayList<Integer>> moves = new ArrayList<>();

        ArrayList<String> cardNames = new ArrayList<>(hand.keySet());
        ArrayList<String> opponentCardNames = new ArrayList<>(opponent.hand.keySet());

        for (int i = 0; i < movesPerTurn; i++) {
            if (cardNames.isEmpty()) break;

            int cardIndex = (int) (Math.random() * cardNames.size());
            String cardKey = cardNames.get(cardIndex);
            Card card = hand.get(cardKey);

            int targetIndex;
            ArrayList<Integer> move = new ArrayList<>();
            move.add(cardIndex);

            if (card instanceof HealingCard) {
                targetIndex = (int) (Math.random() * cardNames.size());
                move.add(targetIndex);
            } else if (card instanceof AttackingCard) {
                if (opponentCardNames.isEmpty()) continue;
                targetIndex = (int) (Math.random() * opponentCardNames.size());
                move.add(targetIndex);
            } else {
                continue; // Skip unknown card types
            }

            moves.add(move);
        }

        return moves;
    }
    public ArrayList<ArrayList<Integer>> chooseWhatToPlay(GenericPlayer opponent, int difficulty) {
        switch (difficulty)
        {
            case 1:
                return chooseRandomCard(opponent);
            case 2:
                // Alpha-beta pruning logic for low depth for medium difficulty
                break;
            case 3:
                // Some ML logic for hard difficulty
                break;
            default:
                System.out.println("Invalid difficulty level. Defaulting to easy.");
                break;
        }
        return chooseRandomCard(opponent);
    }
}
