// Copyright 2022-2023 Preda Diana 324CA
package main;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CardInput;
import fileio.DecksInput;

import java.util.ArrayList;
import java.util.LinkedList;

public abstract class Cards {
    public static final int INITIAL_HEALTH = 30;
    private int mana;
    private String description;
    private ArrayList<String> colors;
    private String name;
    @JsonIgnore
    private int isFrozen;
    @JsonIgnore
    private int attackUsed;

    public Cards(final int mana, final String description, final ArrayList<String> colors,
                 final String name) {
        this.mana = mana;
        this.description = description;
        this.colors = new ArrayList<>(colors);
        this.name = name;
        this.isFrozen = 0;
        this.attackUsed = 0;
    }

    /**
     *
     * @return
     */
    public int getAttackUsed() {
        return attackUsed;
    }

    /**
     *
     * @param attackUsed
     */
    public void setAttackUsed(final int attackUsed) {
        this.attackUsed = attackUsed;
    }

    /**
     *
     * @return
     */
    public int getMana() {
        return mana;
    }

    /**
     *
     * @return
     */
    public int getIsFrozen() {
        return isFrozen;
    }

    /**
     *
     * @param isFrozen
     */
    public void setIsFrozen(final int isFrozen) {
        this.isFrozen = isFrozen;
    }


    /**
     *
     * @param mana
     */
    public void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getColors() {
        return colors;
    }

    /**
     *
     * @param colors
     */
    public void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     *
     * @param decksInput
     * @return
     */

    // get player cards from input
    public static LinkedList<LinkedList<Cards>> setCardType(final DecksInput decksInput) {
        LinkedList<LinkedList<Cards>> deckOfDecks = new LinkedList<>();
        for (int i = 0; i < decksInput.getDecks().size(); i++) {
            ArrayList<CardInput> cardInputsDeck = decksInput.getDecks().get(i);
            LinkedList<Cards> deck = new LinkedList<>();
            for (int j = 0; j < cardInputsDeck.size(); j++) {
                String name = cardInputsDeck.get(j).getName();
                if (name.equals("The Ripper") || name.equals("Miraj")
                        || name.equals("The Cursed One")
                        || name.equals("Disciple") || name.equals("Sentinel")
                        || name.equals("Berserker") || name.equals("Goliath")
                        || name.equals("Warden")) {
                    Minion minion = new Minion(cardInputsDeck.get(j));
                    deck.addLast(minion);
                } else if (name.equals("Firestorm") || name.equals("Winterfell")
                        || name.equals("Heart Hound")) {
                    Environment environment = new Environment(cardInputsDeck.get(j));
                    deck.addLast(environment);
                }

            }
            deckOfDecks.addLast(deck);
        }
        return deckOfDecks;
    }
}

// extends for Hero cards
class Hero extends Cards {

    private int health;

    Hero(final Hero hero) {
        super(hero.getMana(), hero.getDescription(), hero.getColors(), hero.getName());
        this.setHealth(hero.health);
    }

    public void setHealth(final int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    Hero(final CardInput cardInput) {
        super(cardInput.getMana(), cardInput.getDescription(), cardInput.getColors(),
                cardInput.getName());
        // set initial health at 30
        this.health = INITIAL_HEALTH;
    }
}

// extends for Environment cards
class Environment extends Cards {

    Environment(final CardInput cardInput) {
        super(cardInput.getMana(), cardInput.getDescription(), cardInput.getColors(),
                cardInput.getName());
    }

    Environment(final Environment environment) {
        super(environment.getMana(), environment.getDescription(),
                environment.getColors(), environment.getName());
    }
}

// extends for Minion cards
class Minion extends Cards {
    Minion(final CardInput cardInput) {
        super(cardInput.getMana(), cardInput.getDescription(), cardInput.getColors(),
                cardInput.getName());
        this.health = cardInput.getHealth();
        this.attackDamage = cardInput.getAttackDamage();
    }
    public int getHealth() {
        return health;
    }

    public void setHealth(final int health) {
        this.health = health;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    private int health;
    private int attackDamage;

    Minion(final Minion minion) {
        super(minion.getMana(), minion.getDescription(), minion.getColors(), minion.getName());
        this.health = minion.getHealth();
        this.attackDamage = minion.getAttackDamage();
    }

}
