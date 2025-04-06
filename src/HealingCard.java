public class HealingCard extends Card{
    private final int healPower;

    public HealingCard(int healPower) {
        super();
        this.healPower = healPower;
    }

    public HealingCard(String name, int HP, int healPower) {
        super(name, HP);
        this.healPower = healPower;
    }

    public int getHealPower() {
        return healPower;
    }

    public void action(Card target) {
        target.HP += healPower;
    }
}
