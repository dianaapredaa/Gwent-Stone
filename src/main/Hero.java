package main;
import fileio.CardInput;

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
