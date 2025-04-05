abstract public class Card {
    protected final String name;
    protected int HP;

    public Card(){
        this.name = "Unknown";
        this.HP = 1;
    }

    public Card(String name, int HP) {
        this.name = name;
        this.HP = HP;
    }

    abstract public void action(Card target);
}
