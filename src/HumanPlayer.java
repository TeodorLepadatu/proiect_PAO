import java.util.HashMap;

public class HumanPlayer extends GenericPlayer {
    private static HumanPlayer instance1 = null;
    private static HumanPlayer instance2 = null;

    private HumanPlayer(String name, HashMap<String, Card> hand) {
        super(name, hand);
    }

    public static HumanPlayer getInstance(String name, HashMap<String, Card> hand) {
        if (instance1 == null) {
            instance1 = new HumanPlayer(name, hand);
        } else if (instance2 == null) {
            instance2 = new HumanPlayer(name, hand);
        }
        return instance1 != null ? instance1 : instance2;
    }
}