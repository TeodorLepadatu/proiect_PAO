import java.util.ArrayList;
import java.util.HashMap;

public class AIPlayer extends GenericPlayer {
    private static AIPlayer instance1 = null;
    private static AIPlayer instance2 = null;
    int difficulty; // 1 = easy, 2 = medium, 3 = hard
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

    //difficulty 1: random card choice
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

    public ArrayList<Integer> chooseWhatToPlay(GenericPlayer oppenent, int difficulty) {
        //v[0] = what card to play
        //v[1] = target card
        ArrayList<Integer> v = new ArrayList<>();
        if(difficulty == 1)
            v = chooseRandomCard(oppenent);
        return v;
    }
}