public class AttackingCard extends Card{
    private final int attackPower;

    public AttackingCard(String name, int HP, int attackPower) {
        super(name, HP);
        this.attackPower = attackPower;
    }

    public void action(Card target) {
        target.HP -= attackPower;
    }

    public int getAttackPower() {
        return attackPower;
    }
}
