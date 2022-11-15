package main;

import fileio.CardInput;

import java.util.ArrayList;

public abstract class Cards {
    private int mana;
    private String description;
    private ArrayList<String> colors;
    private String name;

    public Cards(int mana, String description, ArrayList<String> colors, String name) {
        this.mana = mana;
        this.description = description;
        this.colors = colors;
        this.name = name;
    }

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
        this.health = cardInput.getHealth();
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

