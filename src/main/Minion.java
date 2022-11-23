package main;

import fileio.CardInput;

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
