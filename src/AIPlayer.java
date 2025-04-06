import java.util.ArrayList;
import java.util.HashMap;

public class AIPlayer extends GenericPlayer {
    private static AIPlayer instance1 = null;
    private static AIPlayer instance2 = null;
    int difficulty; // 1 = easy, 2 = medium, 3 = hard, 4 = impossible

    private AIPlayer(String name, HashMap<String, Card> hand, int difficulty) {
        super(name, hand);
        this.difficulty = difficulty;
    }

    public static AIPlayer getInstance(String name, HashMap<String, Card> hand, int difficulty) {
        if (instance1 == null) {
            instance1 = new AIPlayer(name, hand, difficulty);
        } else if (instance2 == null) {
            instance2 = new AIPlayer(name, hand, difficulty);
        }
        return instance1 != null ? instance1 : instance2;
    }

    //--- AI search helper classes & methods ---//

    // Helper class to store search results (score and corresponding move)
    private class MoveResult {
        int score;
        ArrayList<Integer> move; // move: [cardIndex, targetIndex]

        MoveResult(int score, ArrayList<Integer> move) {
            this.score = score;
            this.move = move;
        }
    }

    // Evaluation function: positive if state favors 'myHand'
    private int evaluateState(HashMap<String, Card> myHand, HashMap<String, Card> oppHand) {
        int myTotalHP = 0;
        for (Card card : myHand.values()) {
            myTotalHP += card.HP;
        }
        int oppTotalHP = 0;
        for (Card card : oppHand.values()) {
            oppTotalHP += card.HP;
        }
        // Favor having more cards: each extra card adds a bonus.
        int bonus = 100 * (myHand.size() - oppHand.size());
        return (myTotalHP - oppTotalHP) + bonus;
    }

    // Clones a hand (creates a new HashMap with cloned card objects)
    private HashMap<String, Card> cloneHand(HashMap<String, Card> hand) {
        HashMap<String, Card> cloned = new HashMap<>();
        for (String key : hand.keySet()) {
            cloned.put(key, cloneCard(hand.get(key)));
        }
        return cloned;
    }

    // Clones a card based on its type.
    private Card cloneCard(Card card) {
        if (card instanceof AttackingCard) {
            AttackingCard ac = (AttackingCard) card;
            return new AttackingCard(ac.name, ac.HP, ac.getAttackPower());
        } else if (card instanceof HealingCard) {
            HealingCard hc = (HealingCard) card;
            return new HealingCard(hc.name, hc.HP, hc.getHealPower());
        } else {
            return new Card(card.name, card.HP) {
                @Override
                public void action(Card target) {
                    // Default: no action.
                }
            };
        }
    }

    // Simulate playing a move. Returns the new state as an array:
    // index 0: current player's hand after move, index 1: opponent's hand.
    private HashMap<String, Card>[] simulateMove(HashMap<String, Card> currentHand, HashMap<String, Card> oppHand,
                                                 int cardIndex, int targetIndex, boolean isAttacking) {
        // Clone hands to avoid modifying the original state.
        HashMap<String, Card> currClone = cloneHand(currentHand);
        HashMap<String, Card> oppClone = cloneHand(oppHand);

        // Identify the card to play.
        String[] currentKeys = currClone.keySet().toArray(new String[0]);
        String playCardKey = currentKeys[cardIndex];
        Card playCard = currClone.get(playCardKey);

        if (isAttacking) {
            // For an attacking card, choose a target from the opponent's hand.
            String[] oppKeys = oppClone.keySet().toArray(new String[0]);
            String targetKey = oppKeys[targetIndex];
            Card targetCard = oppClone.get(targetKey);
            playCard.action(targetCard);
            // Remove target card if its HP is 0 or less.
            if (targetCard.HP <= 0) {
                oppClone.remove(targetKey);
            }
        } else {
            // For a healing card, choose a target from the current player's hand.
            String[] currKeys = currClone.keySet().toArray(new String[0]);
            String targetKey = currKeys[targetIndex];
            Card targetCard = currClone.get(targetKey);
            playCard.action(targetCard);
        }
        return new HashMap[]{currClone, oppClone};
    }

    //--- MINIMAX (Full-Tree) function ---//
    // This version explores the entire game tree until a terminal state is reached.
    private MoveResult minimaxFull(HashMap<String, Card> myHand, HashMap<String, Card> oppHand, boolean maximizingPlayer) {
        // Terminal condition: one player's hand is empty.
        if (myHand.isEmpty() || oppHand.isEmpty()) {
            int eval = evaluateState(myHand, oppHand);
            return new MoveResult(eval, null);
        }
        MoveResult bestResult = null;

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            ArrayList<Integer> bestMove = null;
            // For each move in my hand.
            String[] myKeys = myHand.keySet().toArray(new String[0]);
            for (int i = 0; i < myKeys.length; i++) {
                Card card = myHand.get(myKeys[i]);
                if (card instanceof HealingCard) {
                    // Healing: target a card from my own hand.
                    String[] targetKeys = myHand.keySet().toArray(new String[0]);
                    for (int j = 0; j < targetKeys.length; j++) {
                        HashMap<String, Card>[] newState = simulateMove(myHand, oppHand, i, j, false);
                        // Roles swap for next move.
                        MoveResult result = minimaxFull(newState[1], newState[0], false);
                        if (result.score > maxEval) {
                            maxEval = result.score;
                            bestMove = new ArrayList<>();
                            bestMove.add(i);
                            bestMove.add(j);
                        }
                    }
                } else if (card instanceof AttackingCard) {
                    // Attacking: target a card from opponent's hand.
                    String[] oppKeys = oppHand.keySet().toArray(new String[0]);
                    for (int j = 0; j < oppKeys.length; j++) {
                        HashMap<String, Card>[] newState = simulateMove(myHand, oppHand, i, j, true);
                        MoveResult result = minimaxFull(newState[1], newState[0], false);
                        if (result.score > maxEval) {
                            maxEval = result.score;
                            bestMove = new ArrayList<>();
                            bestMove.add(i);
                            bestMove.add(j);
                        }
                    }
                }
            }
            bestResult = new MoveResult(maxEval, bestMove);
        } else {
            // Minimizing branch: the opponent's turn.
            int minEval = Integer.MAX_VALUE;
            ArrayList<Integer> bestMove = null;
            String[] oppKeys = oppHand.keySet().toArray(new String[0]);
            for (int i = 0; i < oppKeys.length; i++) {
                Card card = oppHand.get(oppKeys[i]);
                if (card instanceof HealingCard) {
                    String[] targetKeys = oppHand.keySet().toArray(new String[0]);
                    for (int j = 0; j < targetKeys.length; j++) {
                        HashMap<String, Card>[] newState = simulateMove(oppHand, myHand, i, j, false);
                        MoveResult result = minimaxFull(newState[1], newState[0], true);
                        if (result.score < minEval) {
                            minEval = result.score;
                            bestMove = new ArrayList<>();
                            bestMove.add(i);
                            bestMove.add(j);
                        }
                    }
                } else if (card instanceof AttackingCard) {
                    String[] targetKeys = myHand.keySet().toArray(new String[0]);
                    for (int j = 0; j < targetKeys.length; j++) {
                        HashMap<String, Card>[] newState = simulateMove(oppHand, myHand, i, j, true);
                        MoveResult result = minimaxFull(newState[1], newState[0], true);
                        if (result.score < minEval) {
                            minEval = result.score;
                            bestMove = new ArrayList<>();
                            bestMove.add(i);
                            bestMove.add(j);
                        }
                    }
                }
            }
            bestResult = new MoveResult(minEval, bestMove);
        }
        return bestResult;
    }

    //--- ALPHA-BETA PRUNING function with fixed depth ---//
    private MoveResult alphaBeta(HashMap<String, Card> myHand, HashMap<String, Card> oppHand, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || myHand.isEmpty() || oppHand.isEmpty()) {
            int eval = evaluateState(myHand, oppHand);
            return new MoveResult(eval, null);
        }
        ArrayList<Integer> bestMove = null;

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            String[] myKeys = myHand.keySet().toArray(new String[0]);
            for (int i = 0; i < myKeys.length; i++) {
                Card card = myHand.get(myKeys[i]);
                if (card instanceof HealingCard) {
                    String[] targetKeys = myHand.keySet().toArray(new String[0]);
                    for (int j = 0; j < targetKeys.length; j++) {
                        HashMap<String, Card>[] newState = simulateMove(myHand, oppHand, i, j, false);
                        MoveResult result = alphaBeta(newState[1], newState[0], depth - 1, alpha, beta, false);
                        if (result.score > maxEval) {
                            maxEval = result.score;
                            bestMove = new ArrayList<>();
                            bestMove.add(i);
                            bestMove.add(j);
                        }
                        alpha = Math.max(alpha, result.score);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                } else if (card instanceof AttackingCard) {
                    String[] oppKeys = oppHand.keySet().toArray(new String[0]);
                    for (int j = 0; j < oppKeys.length; j++) {
                        HashMap<String, Card>[] newState = simulateMove(myHand, oppHand, i, j, true);
                        MoveResult result = alphaBeta(newState[1], newState[0], depth - 1, alpha, beta, false);
                        if (result.score > maxEval) {
                            maxEval = result.score;
                            bestMove = new ArrayList<>();
                            bestMove.add(i);
                            bestMove.add(j);
                        }
                        alpha = Math.max(alpha, result.score);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return new MoveResult(maxEval, bestMove);
        } else {
            int minEval = Integer.MAX_VALUE;
            String[] oppKeys = oppHand.keySet().toArray(new String[0]);
            for (int i = 0; i < oppKeys.length; i++) {
                Card card = oppHand.get(oppKeys[i]);
                if (card instanceof HealingCard) {
                    String[] targetKeys = oppHand.keySet().toArray(new String[0]);
                    for (int j = 0; j < targetKeys.length; j++) {
                        HashMap<String, Card>[] newState = simulateMove(oppHand, myHand, i, j, false);
                        MoveResult result = alphaBeta(newState[1], newState[0], depth - 1, alpha, beta, true);
                        if (result.score < minEval) {
                            minEval = result.score;
                            bestMove = new ArrayList<>();
                            bestMove.add(i);
                            bestMove.add(j);
                        }
                        beta = Math.min(beta, result.score);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                } else if (card instanceof AttackingCard) {
                    String[] targetKeys = myHand.keySet().toArray(new String[0]);
                    for (int j = 0; j < targetKeys.length; j++) {
                        HashMap<String, Card>[] newState = simulateMove(oppHand, myHand, i, j, true);
                        MoveResult result = alphaBeta(newState[1], newState[0], depth - 1, alpha, beta, true);
                        if (result.score < minEval) {
                            minEval = result.score;
                            bestMove = new ArrayList<>();
                            bestMove.add(i);
                            bestMove.add(j);
                        }
                        beta = Math.min(beta, result.score);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return new MoveResult(minEval, bestMove);
        }
    }

    // Public interface for choosing moves based on difficulty:
    // Difficulty 1: random card choice.
    public ArrayList<Integer> chooseRandomCard(GenericPlayer opponent) {
        ArrayList<Integer> v = new ArrayList<>();
        int cardIndex = (int) (Math.random() * hand.size());
        String cardKey = (String) hand.keySet().toArray()[cardIndex];
        Card card = hand.get(cardKey);

        if (card instanceof HealingCard) {
            int targetIndex = (int) (Math.random() * hand.size());
            v.add(cardIndex);
            v.add(targetIndex);
        } else if (card instanceof AttackingCard) {
            int targetIndex = (int) (Math.random() * opponent.hand.size());
            v.add(cardIndex);
            v.add(targetIndex);
        }
        return v;
    }

    // Public interface for alpha-beta pruning (fixed depth).
    public ArrayList<Integer> AlphaBetaPruning(GenericPlayer opponent, int depth) {
        MoveResult result = alphaBeta(cloneHand(this.hand), cloneHand(opponent.hand), depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        return result.move;
    }

    // Public interface for exhaustive minimax (full tree).
    public ArrayList<Integer> MinMax(GenericPlayer opponent) {
        MoveResult result = minimaxFull(cloneHand(this.hand), cloneHand(opponent.hand), true);
        return result.move;
    }

    // Choose move based on difficulty.
    public ArrayList<Integer> chooseWhatToPlay(GenericPlayer opponent, int difficulty) {
        ArrayList<Integer> v = new ArrayList<>();
        if (difficulty == 1)
            v = chooseRandomCard(opponent);
        else if (difficulty == 2)
            v = AlphaBetaPruning(opponent, 5); // Fixed depth for alpha-beta.
        else if (difficulty == 3){
            // Implement more advanced ML-based decision here if desired.
        }
        else if (difficulty == 4)
            v = MinMax(opponent); // Exhaustive minimax.
        return v;
    }
}
