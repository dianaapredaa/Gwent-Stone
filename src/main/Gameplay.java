// Copyright 2022-2023 Preda Diana 324CA
package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.StartGameInput;

import java.util.ArrayList;
import java.util.LinkedList;

public final class Gameplay {
    private static ObjectMapper objectMapper = new ObjectMapper();
    public static final int MAX_SIZE = 5;
    private Gameplay() {
    }

    /**
     *
     * @param output
     * @param command
     * @param turn
     * @param playingTable
     * @param playerOneDeckInHand
     * @param playerTwoDeckInHand
     * @param utils
     */
    // place card on table
    public static void placeCard(final ArrayNode output, final ActionsInput command, final int turn,
                                 final ArrayList<LinkedList<Minion>> playingTable,
                                 final LinkedList<Cards> playerOneDeckInHand,
                                 final LinkedList<Cards> playerTwoDeckInHand, final Utils utils) {
        int handIdx = command.getHandIdx();
        // check for player turn and invalid commends
        if (turn == 1) {
            Cards cardToPlace = playerOneDeckInHand.get(handIdx);
            String cardName = cardToPlace.getName();
            // if card is 'Environment' type, it can not be placed on the playing table
            if (cardName.equals("Firestorm") || cardName.equals("Winterfell")
                    || cardName.equals("Heart Hound")) {

                InvalidCase.placeCardEnvironmentCard(output, handIdx);

                // check if there is enough mana left to place this card
            } else if (playerOneDeckInHand.get(handIdx).getMana() > utils.getPlayerOneMana()) {

                InvalidCase.placeCardNotEnoughMana(output, handIdx);

            } else if (cardName.equals("The Ripper") || cardName.equals("Miraj")
                    || cardName.equals("Goliath") || cardName.equals("Warden")) {
                // not enough space on the playing table
                if (playingTable.get(2).size() == MAX_SIZE) {

                    InvalidCase.placeCardNotEnoughSpace(output, handIdx);

                } else {
                    // decrease mana and place card
                    utils.setPlayerOneMana(utils.getPlayerOneMana() - cardToPlace.getMana());
                    playingTable.get(2).addLast((Minion) playerOneDeckInHand.remove(handIdx));
                }
            } else if (cardName.equals("Sentinel") || cardName.equals("Berserker")
                    || cardName.equals("The Cursed One") || cardName.equals("Disciple")) {
                // not enough space on the playing table
                if (playingTable.get(3).size() == MAX_SIZE) {

                    InvalidCase.placeCardNotEnoughSpace(output, handIdx);

                } else {
                    // decrease mana and place card
                    utils.setPlayerOneMana(utils.getPlayerOneMana() - cardToPlace.getMana());
                    playingTable.get(3).addLast((Minion) playerOneDeckInHand.remove(handIdx));
                }
            }
        } else if (turn == 2) {
            Cards cardToPlace = playerTwoDeckInHand.get(handIdx);
            String cardName = cardToPlace.getName();

            // if card is 'Environment' type, it can not be placed on the playing table
            if (cardName.equals("Firestorm") || cardName.equals("Winterfell")
                    || cardName.equals("Heart Hound")) {

                InvalidCase.placeCardEnvironmentCard(output, handIdx);

                // check if there is enough mana left to place this card
            } else if (playerTwoDeckInHand.get(handIdx).getMana() > utils.getPlayerTwoMana()) {

                InvalidCase.placeCardNotEnoughMana(output, handIdx);

            } else if (cardName.equals("The Ripper") || cardName.equals("Miraj")
                    || cardName.equals("Goliath") || cardName.equals("Warden")) {
                // check if there is enough space on the playing table
                if (playingTable.get(1).size() == MAX_SIZE) {

                    InvalidCase.placeCardNotEnoughSpace(output, handIdx);

                } else {
                    // decrease mana and place card
                    utils.setPlayerTwoMana(utils.getPlayerTwoMana() - cardToPlace.getMana());
                    playingTable.get(1).addLast((Minion) playerTwoDeckInHand.remove(handIdx));
                }
            } else if (cardName.equals("Sentinel") || cardName.equals("Berserker")
                    || cardName.equals("The Cursed One") || cardName.equals("Disciple")) {
                // not enough space on the playing table
                if (playingTable.get(0).size() == MAX_SIZE) {

                    InvalidCase.placeCardNotEnoughSpace(output, handIdx);

                } else {
                    // decrease mana and place card
                    utils.setPlayerTwoMana(utils.getPlayerTwoMana() - cardToPlace.getMana());
                    playingTable.get(0).addLast((Minion) playerTwoDeckInHand.remove(handIdx));
                }
            }
        }
    }

    /**
     *
     * @param playerOneDeck
     * @param playerTwoDeck
     * @param playerOneDeckInHand
     * @param playerTwoDeckInHand
     * @param playingTable
     * @param playerOneHero
     * @param playerTwoHero
     * @param newGame
     * @param utils
     */
    // change turn
    public static void endPlayerTurn(final LinkedList<Cards> playerOneDeck,
                                     final LinkedList<Cards> playerTwoDeck,
                                     final LinkedList<Cards> playerOneDeckInHand,
                                     final LinkedList<Cards> playerTwoDeckInHand,
                                     final ArrayList<LinkedList<Minion>> playingTable,
                                     final Hero playerOneHero, final Hero playerTwoHero,
                                     final StartGameInput newGame, final Utils utils) {
        // check if both players played their turns
        if (utils.getTurn() != newGame.getStartingPlayer()) {
            // end turn & add mana
            utils.setNumberOfRounds(utils.getNumberOfRounds() + 1);
            utils.setPlayerOneMana(utils.getPlayerOneMana() + utils.getNumberOfRounds());
            utils.setPlayerTwoMana(utils.getPlayerTwoMana() + utils.getNumberOfRounds());
            if (!playerOneDeck.isEmpty()) {
                playerOneDeckInHand.addLast(playerOneDeck.removeFirst());
            }
            if (!playerTwoDeck.isEmpty()) {
                playerTwoDeckInHand.addLast(playerTwoDeck.removeFirst());
            }
        }
        // don't forget the frozen cards
        for (LinkedList<Minion> minions : playingTable) {
            for (Minion minion : minions) {
                if (utils.getTurn() == 2 && minion.getIsFrozen() == 1
                        && (minions == playingTable.get(0)
                        || minions == playingTable.get(1))) {
                    minion.setIsFrozen(0);
                }
                if (utils.getTurn() == 1 && minion.getIsFrozen() == 1
                        && (minions == playingTable.get(2)
                        || minions == playingTable.get(3))) {
                    minion.setIsFrozen(0);
                }
                // don't forget to let them use their power again
                if (minion.getAttackUsed() == 1) {
                    minion.setAttackUsed(0);
                }
            }
        }

        // also for the hero
        playerOneHero.setAttackUsed(0);
        playerTwoHero.setAttackUsed(0);

        // switch turns
        if (utils.getTurn() == 1) {
            utils.setTurn(2);
        } else {
            utils.setTurn(1);
        }
    }

    /**
     *
     * @param output
     * @param command
     * @param playingTable
     * @param turn
     */
    // make use of card attack
    public static void cardUsesAttack(final ArrayNode output, final ActionsInput command,
                                      final ArrayList<LinkedList<Minion>> playingTable,
                                      final int turn) {
        // get coordinates
        int cardAttackerX = command.getCardAttacker().getX();
        int cardAttackerY = command.getCardAttacker().getY();
        int cardAttackedX = command.getCardAttacked().getX();
        int cardAttackedY = command.getCardAttacked().getY();

        Minion cardAttacked = null;
        Minion cardAttacker = null;

        if (playingTable.get(cardAttackerX).size() > cardAttackerY
                && playingTable.get(cardAttackedX).size() > cardAttackedY) {
            cardAttacker = playingTable.get(cardAttackerX).get(cardAttackerY);
            cardAttacked = playingTable.get(cardAttackedX).get(cardAttackedY);
        } else {
            return;
        }

        // check for invalid cases
        // check if you attack you enemy card
        if ((turn == 1 && (cardAttackedX == 2 || cardAttackedX == 3))
                || (turn == 2 && (cardAttackedX == 0 || cardAttackedX == 1))) {

            InvalidCase.useAttackNotEnemyCard(output, command);

            // check if the card has already been used this turn
        } else if (cardAttacker.getAttackUsed() == 1) {

            InvalidCase.useAttackAlreadyAttacked(output, command);

            // check if the card is frozen
        } else if (cardAttacker.getIsFrozen() == 1) {

            InvalidCase.useAttackIsFrozen(output, command);

        } else {
            int isTank = Utils.isTank(playingTable, turn);
            // check for 'Tank'
            if (isTank == 1 && !cardAttacked.getName().equals("Goliath")
                    && !cardAttacked.getName().equals("Warden")) {

                InvalidCase.useAttackTank(output, command);

            } else {
                // just attack
                cardAttacker.setAttackUsed(1);
                if (cardAttacked.getHealth() <= cardAttacker.getAttackDamage()) {
                    // card dies
                    playingTable.get(cardAttackedX).remove(cardAttackedY);
                } else {
                    cardAttacked.setHealth(cardAttacked.getHealth()
                            - cardAttacker.getAttackDamage());
                }
            }
        }
    }

    /**
     *
     * @param output
     * @param command
     * @param playingTable
     * @param turn
     */
    public static void cardUsesAbility(final ArrayNode output, final ActionsInput command,
                                       final ArrayList<LinkedList<Minion>> playingTable,
                                       final int turn) {
        // get coordinates
        int cardAttackerX = command.getCardAttacker().getX();
        int cardAttackerY = command.getCardAttacker().getY();
        int cardAttackedX = command.getCardAttacked().getX();
        int cardAttackedY = command.getCardAttacked().getY();
        Minion cardAttacker;
        Minion cardAttacked;

        if (playingTable.get(cardAttackerX).size() > cardAttackerY
                && playingTable.get(cardAttackedX).size() > cardAttackedY) {
            cardAttacker = playingTable.get(cardAttackerX).get(cardAttackerY);
            cardAttacked = playingTable.get(cardAttackedX).get(cardAttackedY);
        } else {
            return;
        }

        // check for invalid cases
        // check if the card is frozen
        if (cardAttacker.getIsFrozen() == 1) {

            InvalidCase.useAbilityIsFrozen(output, command);

            // check if the card has already attacked this turn
        }  else if (cardAttacker.getAttackUsed() == 1) {

            InvalidCase.useAbilityAlreadyAttacked(output, command);

        } else {
            boolean b = (turn == 1 && (cardAttackedX == 2 || cardAttackedX == 3))
                    || (turn == 2 && (cardAttackedX == 0 || cardAttackedX == 1));
            if (cardAttacker.getName().equals("Disciple")) {
                if (b) {
                    // use ability
                    cardAttacker.setAttackUsed(1);
                    cardAttacked.setHealth(cardAttacked.getHealth() + 2);
                } else {
                    // can not use this ability on enemy's card
                    InvalidCase.useAbilityNotMyCard(output, command);

                }
            } else if (cardAttacker.getName().equals("The Ripper")
                    || cardAttacker.getName().equals("Miraj")
                    || cardAttacker.getName().equals("The Cursed One")) {
                if (b) {

                    // can not use this ability on your card
                    InvalidCase.useAbilityNotEnemyCard(output, command);
                    return;
                }
                int isTank = Utils.isTank(playingTable, turn);
                // check for 'Tank' cards
                if (isTank == 1 && !cardAttacked.getName().equals("Goliath")
                        && !cardAttacked.getName().equals("Warden")) {

                    InvalidCase.useAbilityTank(output, command);

                } else {
                    // use ability
                    cardAttacker.setAttackUsed(1);
                    if (cardAttacker.getName().equals("The Ripper")) {
                        cardAttacked.setAttackDamage(cardAttacked.getAttackDamage() - 2);
                        if (cardAttacked.getAttackDamage() < 0) {
                            cardAttacked.setAttackDamage(0);
                        }
                    } else if (cardAttacker.getName().equals("Miraj")) {
                        int healthSwap = cardAttacked.getHealth();
                        cardAttacked.setHealth(cardAttacker.getHealth());
                        cardAttacker.setHealth(healthSwap);
                    } else if (cardAttacker.getName().equals("The Cursed One")) {
                        int healthSwap = cardAttacked.getHealth();
                        cardAttacked.setHealth(cardAttacked.getAttackDamage());
                        cardAttacked.setAttackDamage(healthSwap);
                        if (cardAttacked.getHealth() <= 0) {
                            playingTable.get(cardAttackedX).remove(cardAttackedY);
                        }
                    }
                }
            }
        }

    }

    /**
     *
     * @param output
     * @param command
     * @param playingTable
     * @param turn
     * @param utils
     * @param playerOneHero
     * @param playerTwoHero
     */
    // attack enemy's Hero
    public static void cardAttackHero(final ArrayNode output, final ActionsInput command,
                                      final ArrayList<LinkedList<Minion>> playingTable,
                                      final int turn, final Utils utils, final Hero playerOneHero,
                                      final Hero playerTwoHero) {
        // get coordinates
        int cardAttackerX = command.getCardAttacker().getX();
        int cardAttackerY = command.getCardAttacker().getY();
        Minion cardAttacker;

        if (playingTable.get(cardAttackerX).size() > cardAttackerY) {
            cardAttacker = playingTable.get(cardAttackerX).get(cardAttackerY);
        } else {
            return;
        }

        // check for invalid cases
        // check if card is frozen
        if (cardAttacker.getIsFrozen() == 1) {

            InvalidCase.attackHeroIsFrozen(output, command);

        // check if card has already been used this turn
        } else if (cardAttacker.getAttackUsed() == 1) {

            InvalidCase.attackHeroAlreadyAttacked(output, command);

        } else {
            int isTank = Utils.isTank(playingTable, turn);
            // check for 'Tank' cards
            if (isTank == 1) {

                InvalidCase.attackHeroTank(output, command);
                return;
            }
            // just attack
            if (turn == 1) {
                cardAttacker.setAttackUsed(1);
                if (playerTwoHero.getHealth() <= cardAttacker.getAttackDamage()) {
                    // player One wins
                    utils.setPlayerOneWins(utils.getPlayerOneWins() + 1);
                    ObjectNode outputNode = objectMapper.createObjectNode();
                    outputNode.put("gameEnded", "Player one killed the enemy hero.");
                    output.addPOJO(outputNode);
                } else {
                    playerTwoHero.setHealth(playerTwoHero.getHealth()
                            - cardAttacker.getAttackDamage());
                }
            } else if (turn == 2) {
                cardAttacker.setAttackUsed(1);
                if (playerOneHero.getHealth() <= cardAttacker.getAttackDamage()) {
                    // player Two wins
                    utils.setPlayerTwoWins(utils.getPlayerTwoWins() +  1);
                    ObjectNode outputNode = objectMapper.createObjectNode();
                    outputNode.put("gameEnded", "Player two killed the enemy hero.");
                    output.addPOJO(outputNode);
                } else {
                    playerOneHero.setHealth(playerOneHero.getHealth()
                            - cardAttacker.getAttackDamage());
                }
            }
        }
    }

    /**
     *
     * @param output
     * @param command
     * @param playingTable
     * @param turn
     * @param utils
     * @param playerOneHero
     * @param playerTwoHero
     */
    // Hero uses ability
    public static void useHeroAbility(final ArrayNode output, final ActionsInput command,
                                      final ArrayList<LinkedList<Minion>> playingTable,
                                      final int turn, final Utils utils, final Hero playerOneHero,
                                      final Hero playerTwoHero) {
        int affectedRow = command.getAffectedRow();

        // check for invalid cases
        // check if player has enough mana to use this card
        if ((turn == 1 && utils.getPlayerOneMana() < playerOneHero.getMana())
                || (turn == 2 && utils.getPlayerTwoMana() < playerTwoHero.getMana())) {

            InvalidCase.heroAbilityNotEnoughMana(output, affectedRow);
            return;

            // check if card has already been used this turn
        } else if ((turn == 1 && playerOneHero.getAttackUsed() == 1)
                || (turn == 2 && playerTwoHero.getAttackUsed() == 1)) {

            InvalidCase.heroAbilityAlreadyAttacked(output, affectedRow);
            return;

            // you can not use this ability on you row
        } else if ((turn == 1 && (playerOneHero.getName().equals("Lord Royce")
                || playerOneHero.getName().equals("Empress Thorina"))
                && (affectedRow == 2 || affectedRow == 3))
                || (turn == 2 && (playerTwoHero.getName().equals("Lord Royce")
                || playerTwoHero.getName().equals("Empress Thorina"))
                && (affectedRow == 0 || affectedRow == 1))) {

            InvalidCase.heroAbilityNotEnemyRow(output, affectedRow);
            return;

            // you can not use this ability on enemy row
        } else if ((turn == 1 && (playerOneHero.getName().equals("General Kocioraw")
                || playerOneHero.getName().equals("King Mudface"))
                && (affectedRow == 0 || affectedRow == 1))
                || (turn == 2 && (playerTwoHero.getName().equals("General Kocioraw")
                || playerTwoHero.getName().equals("King Mudface"))
                && (affectedRow == 2 || affectedRow == 3))) {

            InvalidCase.heroAbilityNotMyRow(output, affectedRow);
            return;
        }
        // check for player turn
        if (turn == 1) {
            utils.setPlayerOneMana(utils.getPlayerOneMana() - playerOneHero.getMana());
            playerOneHero.setAttackUsed(1);
        } else if (turn == 2) {
            utils.setPlayerTwoMana(utils.getPlayerTwoMana() - playerTwoHero.getMana());
            playerTwoHero.setAttackUsed(1);
        }

        // just attack
        if ((turn == 1 && playerOneHero.getName().equals("Lord Royce"))
                || (turn == 2 && playerTwoHero.getName().equals("Lord Royce"))) {
            int maxAttack = -1;
            int maxAttackIdx = -1;
            for (int k = 0; k < playingTable.get(affectedRow).size(); k++) {
                if (playingTable.get(affectedRow).get(k).getAttackDamage() >= maxAttack) {
                    maxAttack = playingTable.get(affectedRow).get(k).getAttackDamage();
                    maxAttackIdx = k;
                }
            }
            if (maxAttackIdx != -1) {
                playingTable.get(affectedRow).get(maxAttackIdx).setIsFrozen(1);
            } else {
                return;
            }
        }

        if ((turn == 1 && playerOneHero.getName().equals("Empress Thorina"))
                || (turn == 2 && playerTwoHero.getName().equals("Empress Thorina"))) {
            int maxHealth = -1;
            int maxHealthIdx = -1;
            for (int k = 0; k < playingTable.get(affectedRow).size(); k++) {
                if (playingTable.get(affectedRow).get(k).getHealth() >= maxHealth) {
                    maxHealth = playingTable.get(affectedRow).get(k).getHealth();
                    maxHealthIdx = k;
                }
            }
            if (maxHealthIdx != -1) {
                playingTable.get(affectedRow).remove(maxHealthIdx);
            } else {
                return;
            }
        }

        if ((turn == 1 && playerOneHero.getName().equals("General Kocioraw"))
                || (turn == 2 && playerTwoHero.getName().equals("General Kocioraw"))) {
            for (int k = 0; k < playingTable.get(affectedRow).size(); k++) {
                Minion card = playingTable.get(affectedRow).get(k);
                card.setAttackDamage(card.getAttackDamage() + 1);
            }
            return;
        }

        if ((turn == 1 && playerOneHero.getName().equals("King Mudface"))
                || (turn == 2 && playerTwoHero.getName().equals("King Mudface"))) {
            for (int k = 0; k < playingTable.get(affectedRow).size(); k++) {
                Minion card = playingTable.get(affectedRow).get(k);
                card.setHealth(card.getHealth() + 1);
            }
        }
    }

    /**
     *
     * @param output
     * @param command
     * @param playerOneDeckInHand
     * @param playerTwoDeckInHand
     * @param playingTable
     * @param utils
     * @param turn
     */
    // attack using Environment card
    public static void useEnvironmentCard(final ArrayNode output, final ActionsInput command,
                                          final LinkedList<Cards> playerOneDeckInHand,
                                          final LinkedList<Cards> playerTwoDeckInHand,
                                          final ArrayList<LinkedList<Minion>> playingTable,
                                          final Utils utils, final int turn) {
        int handIdx = command.getHandIdx();
        int affectedRow = command.getAffectedRow();
        Cards cardToPlace = null;
        if (turn == 1) {
            if (playerOneDeckInHand.size() > handIdx) {
                cardToPlace = playerOneDeckInHand.get(handIdx);
            } else {
                return;
            }
        } else if (turn == 2) {
            if (playerTwoDeckInHand.size() > handIdx) {
                cardToPlace = playerTwoDeckInHand.get(handIdx);
            } else {
                return;
            }
        }
        String cardName = cardToPlace.getName();
        // check for invalid cases, then attack if necessary
        // check if card is 'Environment' type
        if (!cardName.equals("Firestorm") && !cardName.equals("Winterfell")
                && !cardName.equals("Heart Hound")) {

            InvalidCase.notEnvironmentType(output, affectedRow, handIdx);

            // check if player has enough mana to use this card
        } else if ((turn == 1 && playerOneDeckInHand.get(handIdx).getMana()
                > utils.getPlayerOneMana())
                || (turn == 2 && playerTwoDeckInHand.get(handIdx).getMana()
                > utils.getPlayerTwoMana())) {

            InvalidCase.environmentCardNotEnoughMana(output, affectedRow, handIdx);

            // you cannot use this cards on you row
        } else if ((turn == 1 && (affectedRow == 2 || affectedRow == 3))
                || (turn == 2 && (affectedRow == 0 || affectedRow == 1))) {

                InvalidCase.environmentCardNotEnemyRow(output, affectedRow, handIdx);

            // check if there is any space left on the affected row of the playing table
        } else if (cardName.equals("Heart Hound")) {
            if ((turn == 1 && affectedRow == 1 && playingTable.get(2).size() == MAX_SIZE)
                    || (turn == 1 && affectedRow == 0 && playingTable.get(3).size() == MAX_SIZE)
                    || (turn == 2 && affectedRow == 2 && playingTable.get(1).size() == MAX_SIZE)
                    || (turn == 2 && affectedRow == 3 && playingTable.get(0).size() == MAX_SIZE)) {

                InvalidCase.environmentCardNotEnoughSpace(output, affectedRow, handIdx);
                return;
            }
            int maxHealth = 0;
            int maxHealthIdx = 0;
            if (turn == 1 && affectedRow == 1) {
                for (int k = 0; k < playingTable.get(1).size(); k++) {
                    if (playingTable.get(1).get(k).getHealth() > maxHealth) {
                        maxHealth = playingTable.get(1).get(k).getHealth();
                        maxHealthIdx = k;
                    }
                }
                playingTable.get(2).add(playingTable.get(1).remove(maxHealthIdx));
                utils.setPlayerOneMana(utils.getPlayerOneMana()
                        - playerOneDeckInHand.get(handIdx).getMana());
                playerOneDeckInHand.remove(handIdx);
            } else if (turn == 1 && affectedRow == 0) {
                for (int k = 0; k < playingTable.get(0).size(); k++) {
                    int cardHealth = playingTable.get(0).get(k).getHealth();
                    if (cardHealth > maxHealth) {
                        maxHealth = playingTable.get(0).get(k).getHealth();
                        maxHealthIdx = k;
                    }
                }
                playingTable.get(3).add(playingTable.get(0).remove(maxHealthIdx));
                utils.setPlayerOneMana(utils.getPlayerOneMana()
                        - playerOneDeckInHand.get(handIdx).getMana());
                playerOneDeckInHand.remove(handIdx);
            } else if (turn == 2 && affectedRow == 2) {
                for (int k = 0; k < playingTable.get(2).size(); k++) {
                    int cardHealth = playingTable.get(2).get(k).getHealth();
                    if (cardHealth > maxHealth) {
                        maxHealth = playingTable.get(2).get(k).getHealth();
                        maxHealthIdx = k;
                    }
                }
                playingTable.get(1).add(playingTable.get(2).remove(maxHealthIdx));
                utils.setPlayerOneMana(utils.getPlayerOneMana()
                        - playerTwoDeckInHand.get(handIdx).getMana());
                playerTwoDeckInHand.remove(handIdx);
            } else if (turn == 2 && affectedRow == 3) {
                for (int k = 0; k < playingTable.get(3).size(); k++) {
                    int cardHealth = playingTable.get(3).get(k).getHealth();
                    if (cardHealth > maxHealth) {
                        maxHealth = playingTable.get(3).get(k).getHealth();
                        maxHealthIdx = k;
                    }
                }
                playingTable.get(0).add(playingTable.get(3).remove(maxHealthIdx));
                utils.setPlayerOneMana(utils.getPlayerOneMana()
                        - playerTwoDeckInHand.get(handIdx).getMana());
                playerTwoDeckInHand.remove(handIdx);
            }
        } else if (cardName.equals("Firestorm")) {
            for (int k = 0; k < playingTable.get(affectedRow).size(); k++) {
                Minion card = playingTable.get(affectedRow).get(k);
                card.setHealth(card.getHealth() - 1);
                int cardHealth = playingTable.get(affectedRow).get(k).getHealth();
                if (cardHealth <= 0) {
                    // card died
                    playingTable.get(affectedRow).remove(k);
                    k--;
                }
            }
            if (turn == 1) {
                utils.setPlayerOneMana(utils.getPlayerOneMana()
                        - playerOneDeckInHand.get(handIdx).getMana());
                playerOneDeckInHand.remove(handIdx);
            }
            if (turn == 2) {
                utils.setPlayerTwoMana(utils.getPlayerTwoMana()
                        - playerTwoDeckInHand.get(handIdx).getMana());
                playerTwoDeckInHand.remove(handIdx);
            }
        } else if (cardName.equals("Winterfell")) {
            for (int k = 0; k < playingTable.get(affectedRow).size(); k++) {
                playingTable.get(affectedRow).get(k).setIsFrozen(1);
            }

            if (turn == 1) {
                utils.setPlayerOneMana(utils.getPlayerOneMana()
                        - playerOneDeckInHand.get(handIdx).getMana());
                playerOneDeckInHand.remove(handIdx);
            } else if (turn == 2) {
                utils.setPlayerTwoMana(utils.getPlayerTwoMana()
                        - playerTwoDeckInHand.get(handIdx).getMana());
                playerTwoDeckInHand.remove(handIdx);
            }
        }
    }
}
