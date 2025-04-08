import java.util.ArrayList;

public class Scenario {

    private ArrayList<Card> germanyDeck;
    private ArrayList<Card> sovietDeck;
    private ArrayList<Card> italyDeck;
    private ArrayList<Card> ukDeck;
    private ArrayList<Card> franceDeck;

    private void CreateGermany() {
        germanyDeck = new ArrayList<>();
        germanyDeck.add(new AttackingCard("Tiger I", 100, 20));
        germanyDeck.add(new AttackingCard("Ju-87", 80, 20));
        germanyDeck.add(new AttackingCard("Bf-109", 70, 10));
        germanyDeck.add(new AttackingCard("Panther", 90, 18));
        germanyDeck.add(new AttackingCard("King Tiger", 120, 25));
        germanyDeck.add(new AttackingCard("Dora", 10, 50));
        germanyDeck.add(new AttackingCard("U-boat", 20, 40));
        germanyDeck.add(new AttackingCard("Bismark", 200, 30));
        germanyDeck.add(new HealingCard("Feldlazarette", 50, 15));
        germanyDeck.add(new HealingCard("Ingenieurbüro", 60, 10));
        germanyDeck.add(new HealingCard("Wartungsunternehmen", 40, 17));
    }

    private void CreateUSSR() {
        sovietDeck = new ArrayList<>();
        sovietDeck.add(new AttackingCard("T-34", 80, 15));
        sovietDeck.add(new AttackingCard("KV-1", 90, 18));
        sovietDeck.add(new AttackingCard("IS-2", 100, 20));
        sovietDeck.add(new AttackingCard("ASU-57", 70, 25));
        sovietDeck.add(new AttackingCard("Pe-8", 50, 40));
        sovietDeck.add(new AttackingCard("Yak-3", 60, 10));
        sovietDeck.add(new AttackingCard("IL-2 Sturmovik", 70, 18));  // extra card for balance
        sovietDeck.add(new HealingCard("Obsluzhivaniye Predpriyatiye", 50, 15));
        sovietDeck.add(new HealingCard("American volunteer medics", 100, 20));
    }

    private void CrateFrance() {
        franceDeck = new ArrayList<>();
        franceDeck.add(new AttackingCard("B1 bis", 200, 20));
        franceDeck.add(new AttackingCard("ARL-44", 200, 25));
        franceDeck.add(new AttackingCard("EBR", 80, 25));
        franceDeck.add(new AttackingCard("Infanterie", 70, 10));
        franceDeck.add(new AttackingCard("Cheval avec anti-tank", 80, 30));
        franceDeck.add(new AttackingCard("Chevalier", 70, 15));
        franceDeck.add(new HealingCard("Bran Carrier", 200, 10));
        franceDeck.add(new HealingCard("Cheval", 60, 5));
        franceDeck.add(new HealingCard("American volunteer medics", 100, 20));
        franceDeck.add(new HealingCard("Baguette", 1, 40));
    }

    private void CreateEngland() {
        ukDeck = new ArrayList<>();
        ukDeck.add(new AttackingCard("Churchill", 200, 20));
        ukDeck.add(new AttackingCard("Matilda", 200, 20));
        ukDeck.add(new AttackingCard("Cromwell", 100, 10));
        ukDeck.add(new AttackingCard("Spitfire", 80, 15));
        ukDeck.add(new AttackingCard("Lancaster", 100, 25));
        ukDeck.add(new AttackingCard("HMS-Hood", 200, 20));
        ukDeck.add(new HealingCard("Sea Gladiator", 80, 15));
        ukDeck.add(new HealingCard("British Red Cross", 50, 10));
        ukDeck.add(new HealingCard("American volunteer medics", 100, 20));
    }

    private void CreateItaly() {
        italyDeck = new ArrayList<>();
        italyDeck.add(new AttackingCard("L3", 40, 5));
        italyDeck.add(new AttackingCard("R3 T20 FA-HS", 70, 30));
        italyDeck.add(new AttackingCard("M13/40", 80, 15));
        italyDeck.add(new AttackingCard("AS-42", 40, 13));
        italyDeck.add(new AttackingCard("P40", 80, 20));
        italyDeck.add(new HealingCard("Pasta ristorante", 50, 20));
        italyDeck.add(new HealingCard("Erwin Rommel Feldlazarette", 100, 15));
        italyDeck.add(new AttackingCard("Bersaglieri", 60, 12));
        italyDeck.add(new HealingCard("Medico Militare", 70, 15));
    }

    public Scenario(int index) {
        switch (index) {
            case 1:
                CreateGermany();
                CreateUSSR();
                break;
            case 2:
                CreateGermany();
                CrateFrance();
                break;
            case 3:
                CreateItaly();
                CrateFrance();
                break;
            case 4:
                CreateItaly();
                CreateEngland();
                break;
            case 5:
                CreateGermany();
                CreateEngland();
                break;
            default:
                CreateGermany();
                CreateUSSR();
                break;
        }
    }

    public ArrayList<Card> getGermanyDeck() { return germanyDeck; }
    public ArrayList<Card> getSovietDeck() { return sovietDeck; }
    public ArrayList<Card> getFranceDeck() { return franceDeck; }
    public ArrayList<Card> getUKDeck() { return ukDeck; }
    public ArrayList<Card> getItalyDeck() { return italyDeck; }
}
