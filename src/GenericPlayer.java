import java.util.HashMap;

abstract public class GenericPlayer {
    protected final String name;
    protected HashMap<String, Card> hand;
    protected int movesPerTurn;

    public GenericPlayer(String name, HashMap<String, Card> hand, int movesPerTurn) {
        this.name = name;
        this.hand = hand;
        this.movesPerTurn = movesPerTurn;
    }

    protected void playCard(String cardName, Card target, GenericPlayer opponent) {
        if (!hand.containsKey(cardName)) {
            System.out.println("Invalid card name.");
            return;
        }
        Card card = hand.get(cardName);
        card.action(target);
        if (target.HP <= 0) {
            opponent.loseCard(target, opponent);
        }
    }

    public void loseCard(Card card, GenericPlayer player) {
        player.hand.remove(card.name);
    }

    public boolean hasLost() {
        return hand.isEmpty();
    }
}
