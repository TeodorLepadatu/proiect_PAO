import java.util.HashMap;

public class AIPlayer extends GenericPlayer{
    public AIPlayer(String name, HashMap<String, Card> hand) {
        super(name, hand);
    }
    private int chooseCardToPlay() {
        // AI logic to choose a card index to play
        // For simplicity, let's just return the first card
        return 0;
    }
}
