import java.util.HashMap;

abstract public class GenericPlayer {
    protected final String name;
    protected HashMap<String, Card> hand;

    public GenericPlayer() {
        this.name = "Unknown";
        this.hand = new HashMap<>();
    }

    public GenericPlayer(String name, HashMap<String, Card> hand) {
        this.name = name;
        this.hand = hand;
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

    public void addCard(Card card, GenericPlayer player) {
        player.hand.put(card.name, card);
    }

    public boolean hasLost() {
        return hand.isEmpty();
    }
}