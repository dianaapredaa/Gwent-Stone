package main;

import fileio.CardInput;

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
