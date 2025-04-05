import java.util.ArrayList;
import java.util.HashMap;

public class AIPlayer extends GenericPlayer{
    public AIPlayer(String name, HashMap<String, Card> hand) {
        super(name, hand);
    }
    public ArrayList<Integer> chooseWhatToPlay() {
        //v[0] = what card to play
        //v[1] = target card
        return new ArrayList<Integer>();
    }

}
