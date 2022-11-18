package main;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CardInput;
import fileio.DecksInput;

import java.util.ArrayList;
import java.util.LinkedList;

public abstract class Cards {
    private int mana;
    private String description;
    private ArrayList<String> colors;
    private String name;
    @JsonIgnore
    private int isFrozen = 0;
    @JsonIgnore
    private int attackUsed = 0;

    public int getAttackUsed() {
        return attackUsed;
    }

    public Cards(int mana, String description, ArrayList<String> colors, String name) {
        this.mana = mana;
        this.description = description;
        this.colors = new ArrayList<>(colors);
        this.name = name;
    }

    public void setAttackUsed(int attackUsed) { this.attackUsed = attackUsed; }
    public int getIsFrozen() { return isFrozen; }

    public void setIsFrozen(int isFrozen) { this.isFrozen = isFrozen; }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static LinkedList<LinkedList<Cards>> setCardType(DecksInput decksInput) {
        LinkedList<LinkedList<Cards>> deckOfDecks = new LinkedList<>();
        for (int i = 0; i < decksInput.getDecks().size(); i++) {
            ArrayList<CardInput> cardInputsDeck = decksInput.getDecks().get(i);
            LinkedList<Cards> deck = new LinkedList<>();
            for (int j = 0; j < cardInputsDeck.size(); j++) {
                String name = cardInputsDeck.get(j).getName();
                if (name.equals("The Ripper") || name.equals("Miraj") || name.equals("The Cursed One") ||
                        name.equals("Disciple") || name.equals("Sentinel") || name.equals("Berserker") ||
                        name.equals("Goliath") || name.equals("Warden")) {
                    Minion minion = new Minion(cardInputsDeck.get(j));
                    deck.addLast(minion);
                } else if (name.equals("Firestorm") || name.equals("Winterfell") || name.equals("Heart Hound")) {
                    Environment environment = new Environment(cardInputsDeck.get(j));
                    deck.addLast(environment);
                }

            }
            deckOfDecks.addLast(deck);
        }
        return deckOfDecks;
    }
}

class Hero extends Cards {

    private int health = 30;

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Hero(CardInput cardInput) {
        super(cardInput.getMana(), cardInput.getDescription(), cardInput.getColors(), cardInput.getName());
        this.health = 30;
    }
}

class Environment extends Cards {

    public Environment(CardInput cardInput) {
        super(cardInput.getMana(), cardInput.getDescription(), cardInput.getColors(), cardInput.getName());
    }
}

class Minion extends Cards {
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    private int health;
    private int attackDamage;

    public Minion(CardInput cardInput) {
        super(cardInput.getMana(), cardInput.getDescription(), cardInput.getColors(), cardInput.getName());
        this.health = cardInput.getHealth();
        this.attackDamage = cardInput.getAttackDamage();
    }


}

class TheRipper extends Minion {
    // Weak Knees: -2 atac pentru un minion din tabăra adversă.
    // atac diferit de 0
    public TheRipper(CardInput cardInput) {
        super(cardInput);
    }

}
class Miraj extends Minion {
    // Skyjack: face swap între viața lui și viața unui minion din tabăra adversă.
    // atac diferit de 0
    public Miraj(CardInput cardInput) {
        super(cardInput);
    }
}

class TheCursedOne extends Minion {
    // Shapeshift: face swap între viața unui minion din tabăra adversă și atacul aceluiași minion
    // atac 0
    public TheCursedOne(CardInput cardInput) {
        super(cardInput);
    }
}

class Disciple extends Minion {
    // God's Plan: +2 viață pentru un minion din tabăra lui.
    // atac 0
    public Disciple(CardInput cardInput) {
        super(cardInput);
    }
}

class Sentinel extends Minion {
    public Sentinel(CardInput cardInput) {
        super(cardInput);
    }
}

class Berserker extends Minion {
    public Berserker(CardInput cardInput) {
        super(cardInput);
    }
}

class Goliath extends Minion {
    public Goliath(CardInput cardInput) {
        super(cardInput);
    }
}

class Warden extends Minion {
    public Warden(CardInput cardInput) {
        super(cardInput);
    }
}

class Firestorm extends Environment {
    // Scade cu 1 viața tuturor minionilor de pe rând.
    public Firestorm(CardInput cardInput) {
        super(cardInput);
    }
}

class Winterfell extends Environment {
    // Toate cărțile de pe rând stau o tură.
    public Winterfell(CardInput cardInput) {
        super(cardInput);
    }
}

class HeartHound extends Environment {
    // Se fură minionul adversarului cu cea mai mare viață de pe rând și se pune pe rândul “oglindit” aferent jucătorului.
    public HeartHound(CardInput cardInput) {
        super(cardInput);
    }
}

class LordRoyce extends Hero {
    // Sub-Zero: îngheață cartea cu cel mai mare atac de pe rând.

    public LordRoyce(CardInput cardInput) {
        super(cardInput);
    }
}

class EmpressThorina extends Hero {
    // Low Blow: distruge cartea cu cea mai mare viață de pe rând.
    public EmpressThorina(CardInput cardInput) {
        super(cardInput);
    }
}

class KingMudface extends Hero {
    // Earth Born: +1 viață pentru toate cărțile de pe rând.
    public KingMudface(CardInput cardInput) {
        super(cardInput);
    }
}

class GeneralKocioraw extends  Hero {
    // Blood Thirst: +1 atac pentru toate cărțile de pe rând.
    public GeneralKocioraw(CardInput cardInput) {
        super(cardInput);
    }
}