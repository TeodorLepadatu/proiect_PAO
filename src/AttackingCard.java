public class AttackingCard extends Card{
    private final int attackPower;

    public AttackingCard(String name, int hp, int attackPower) {
        super(name, hp);
        this.attackPower = attackPower;
    }

    @Override
    public void action(Card target) {
        target.hp -= attackPower;
    }

    public int getAttackPower() {
        return attackPower;
    }
}
