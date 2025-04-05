public class DefendingCard extends Card{
    private final int healPower;

    public DefendingCard(int healPower) {
        super();
        this.healPower = healPower;
    }

    public DefendingCard(String name, int HP, int healPower) {
        super(name, HP);
        this.healPower = healPower;
    }

    public void action(Card target) {
        target.HP += healPower;
    }
}
