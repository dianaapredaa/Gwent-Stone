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
     * @param a
     */
    public static void placeCard(final ArrayNode output, final ActionsInput command, final int turn,
                                 final ArrayList<LinkedList<Minion>> playingTable,
                                 final LinkedList<Cards> playerOneDeckInHand,
                                 final LinkedList<Cards> playerTwoDeckInHand, final Utils a) {
        int handIdx = command.getHandIdx();
        if (turn == 1) {
            Cards cardToPlace = playerOneDeckInHand.get(handIdx);
            String cardName = cardToPlace.getName();
            if (cardName.equals("Firestorm") || cardName.equals("Winterfell")
                    || cardName.equals("Heart Hound")) {
                ObjectNode outputNode = objectMapper.createObjectNode();
                outputNode.put("command", "placeCard");
                outputNode.put("error", "Cannot place environment card on table.");
                outputNode.put("handIdx", handIdx);
                output.addPOJO(outputNode);
            } else if (playerOneDeckInHand.get(handIdx).getMana() > a.getPlayerOneMana()) {
                ObjectNode outputNode = objectMapper.createObjectNode();
                outputNode.put("command", "placeCard");
                outputNode.put("error", "Not enough mana to place card on table.");
                outputNode.put("handIdx", handIdx);
                output.addPOJO(outputNode);
            } else if (cardName.equals("The Ripper") || cardName.equals("Miraj")
                    || cardName.equals("Goliath") || cardName.equals("Warden")) {
                if (playingTable.get(2).size() == 5) {
                    ObjectNode outputNode = objectMapper.createObjectNode();
                    outputNode.put("command", "placeCard");
                    outputNode.put("error", "Cannot place card on table since row is full.");
                    outputNode.put("handIdx", handIdx);
                    output.addPOJO(outputNode);
                } else {
                    a.setPlayerOneMana(a.getPlayerOneMana() - cardToPlace.getMana());
                    playingTable.get(2).addLast((Minion) playerOneDeckInHand.remove(handIdx));
                }
            } else {
                if (playingTable.get(3).size() == 5) {
                    ObjectNode outputNode = objectMapper.createObjectNode();
                    outputNode.put("command", "placeCard");
                    outputNode.put("error", "Cannot place card on table since row is full.");
                    outputNode.put("handIdx", handIdx);
                    output.addPOJO(outputNode);
                } else {
                    a.setPlayerOneMana(a.getPlayerOneMana() - cardToPlace.getMana());
                    playingTable.get(3).addLast((Minion) playerOneDeckInHand.remove(handIdx));
                }
            }
        } else if (turn == 2) {
            Cards cardToPlace = playerTwoDeckInHand.get(handIdx);
            String cardName = cardToPlace.getName();

            if (cardName.equals("Firestorm") || cardName.equals("Winterfell")
                    || cardName.equals("Heart Hound")) {
                ObjectNode outputNode = objectMapper.createObjectNode();
                outputNode.put("command", "placeCard");
                outputNode.put("error", "Cannot place environment card on table.");
                outputNode.put("handIdx", handIdx);
                output.addPOJO(outputNode);
            } else if (playerTwoDeckInHand.get(handIdx).getMana() > a.getPlayerTwoMana()) {
                ObjectNode outputNode = objectMapper.createObjectNode();
                outputNode.put("command", "placeCard");
                outputNode.put("error", "Not enough mana to place card on table.");
                outputNode.put("handIdx", handIdx);
                output.addPOJO(outputNode);
            } else if (cardName.equals("The Ripper") || cardName.equals("Miraj")
                    || cardName.equals("Goliath") || cardName.equals("Warden")) {
                if (playingTable.get(1).size() == 5) {
                    ObjectNode outputNode = objectMapper.createObjectNode();
                    outputNode.put("command", "placeCard");
                    outputNode.put("error", "Cannot place card on table since row is full.");
                    outputNode.put("handIdx", handIdx);
                    output.addPOJO(outputNode);
                } else {
                    a.setPlayerTwoMana(a.getPlayerTwoMana() - cardToPlace.getMana());
                    playingTable.get(1).addLast((Minion) playerTwoDeckInHand.remove(handIdx));
                }
            } else {
                if (playingTable.get(0).size() == 5) {
                    ObjectNode outputNode = objectMapper.createObjectNode();
                    outputNode.put("command", "placeCard");
                    outputNode.put("error", "Cannot place card on table since row is full.");
                    outputNode.put("handIdx", handIdx);
                    output.addPOJO(outputNode);
                } else {
                    a.setPlayerTwoMana(a.getPlayerTwoMana() - cardToPlace.getMana());
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
     * @param a
     */
    public static void endPlayerTurn(final LinkedList<Cards> playerOneDeck,
                                     final LinkedList<Cards> playerTwoDeck,
                                     final LinkedList<Cards> playerOneDeckInHand,
                                     final LinkedList<Cards> playerTwoDeckInHand,
                                     final ArrayList<LinkedList<Minion>> playingTable,
                                     final Hero playerOneHero, final Hero playerTwoHero,
                                     final StartGameInput newGame, final Utils a) {
        // check if both players played their turns
        if (a.getTurn() != newGame.getStartingPlayer()) {
            // turn ends & add mana
            a.setNumberOfRounds(a.getNumberOfRounds() + 1);
            a.setPlayerOneMana(a.getPlayerOneMana() + a.getNumberOfRounds());
            a.setPlayerTwoMana(a.getPlayerTwoMana() + a.getNumberOfRounds());
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
                if (a.getTurn() == 2 && minion.getIsFrozen() == 1
                        && (minions == playingTable.get(0)
                        || minions == playingTable.get(1))) {
                    minion.setIsFrozen(0);
                }
                if (a.getTurn() == 1 && minion.getIsFrozen() == 1
                        && (minions == playingTable.get(2)
                        || minions == playingTable.get(3))) {
                    minion.setIsFrozen(0);
                }
                if (minion.getAttackUsed() == 1) {
                    minion.setAttackUsed(0);
                }
            }
        }
        playerOneHero.setAttackUsed(0);
        playerTwoHero.setAttackUsed(0);
        // switch turns
        if (a.getTurn() == 1) {
            a.setTurn(2);
        } else {
            a.setTurn(1);
        }
    }

    /**
     *
     * @param output
     * @param command
     * @param playingTable
     * @param turn
     */
    public static void cardUsesAttack(final ArrayNode output, final ActionsInput command,
                                      final ArrayList<LinkedList<Minion>> playingTable,
                                      final int turn) {
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

        if ((turn == 1 && (cardAttackedX == 2 || cardAttackedX == 3))
                || (turn == 2 && (cardAttackedX == 0 || cardAttackedX == 1))) {
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("command", "cardUsesAttack");
            outputNode.putPOJO("cardAttacker", command.getCardAttacker());
            outputNode.putPOJO("cardAttacked", command.getCardAttacked());
            outputNode.put("error", "Attacked card does not belong to the enemy.");
            output.addPOJO(outputNode);
        } else if (cardAttacker.getAttackUsed() == 1) {
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("command", "cardUsesAttack");
            outputNode.putPOJO("cardAttacker", command.getCardAttacker());
            outputNode.putPOJO("cardAttacked", command.getCardAttacked());
            outputNode.put("error", "Attacker card has already attacked this turn.");
            output.addPOJO(outputNode);
        } else if (cardAttacker.getIsFrozen() == 1) {
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("command", "cardUsesAttack");
            outputNode.putPOJO("cardAttacker", command.getCardAttacker());
            outputNode.putPOJO("cardAttacked", command.getCardAttacked());
            outputNode.put("error", "Attacker card is frozen.");
            output.addPOJO(outputNode);
        } else {
            int isTank = Utils.isTank(playingTable, turn);

            if (isTank == 1 && !cardAttacked.getName().equals("Goliath")
                    && !cardAttacked.getName().equals("Warden")) {
                ObjectNode outputNode = objectMapper.createObjectNode();
                outputNode.put("command", "cardUsesAttack");
                outputNode.putPOJO("cardAttacker", command.getCardAttacker());
                outputNode.putPOJO("cardAttacked", command.getCardAttacked());
                outputNode.put("error", "Attacked card is not of type 'Tank'.");
                output.addPOJO(outputNode);
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

        if (cardAttacker.getIsFrozen() == 1) {
            ObjectNode  outputNode = objectMapper.createObjectNode();
            outputNode.put("command", "cardUsesAbility");
            outputNode.putPOJO("cardAttacker", command.getCardAttacker());
            outputNode.putPOJO("cardAttacked", command.getCardAttacked());
            outputNode.put("error", "Attacker card is frozen.");
            output.addPOJO(outputNode);
        }  else if (cardAttacker.getAttackUsed() == 1) {
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("command", "cardUsesAbility");
            outputNode.putPOJO("cardAttacker", command.getCardAttacker());
            outputNode.putPOJO("cardAttacked", command.getCardAttacked());
            outputNode.put("error", "Attacker card has already attacked this turn.");
            output.addPOJO(outputNode);
        } else {
            boolean b = (turn == 1 && (cardAttackedX == 2 || cardAttackedX == 3))
                    || (turn == 2 && (cardAttackedX == 0 || cardAttackedX == 1));
            if (cardAttacker.getName().equals("Disciple")) {
                if (b) {
                    // use ability
                    cardAttacker.setAttackUsed(1);
                    cardAttacked.setHealth(cardAttacked.getHealth() + 2);
                } else {
                    ObjectNode outputNode = objectMapper.createObjectNode();
                    outputNode.put("command", "cardUsesAbility");
                    outputNode.putPOJO("cardAttacker", command.getCardAttacker());
                    outputNode.putPOJO("cardAttacked", command.getCardAttacked());
                    outputNode.put("error", "Attacked card does not belong to the "
                            + "current player.");
                    output.addPOJO(outputNode);
                }
            } else if (cardAttacker.getName().equals("The Ripper")
                    || cardAttacker.getName().equals("Miraj")
                    || cardAttacker.getName().equals("The Cursed One")) {
                if (b) {
                    ObjectNode outputNode = objectMapper.createObjectNode();
                    outputNode.put("command", "cardUsesAbility");
                    outputNode.putPOJO("cardAttacker", command.getCardAttacker());
                    outputNode.putPOJO("cardAttacked", command.getCardAttacked());
                    outputNode.put("error", "Attacked card does not belong to the "
                            + "enemy.");
                    output.addPOJO(outputNode);
                    return;
                }
                int isTank = Utils.isTank(playingTable, turn);
                if (isTank == 1 && !cardAttacked.getName().equals("Goliath")
                        && !cardAttacked.getName().equals("Warden")) {
                    ObjectNode outputNode = objectMapper.createObjectNode();
                    outputNode.put("command", "cardUsesAbility");
                    outputNode.putPOJO("cardAttacker", command.getCardAttacker());
                    outputNode.putPOJO("cardAttacked", command.getCardAttacked());
                    outputNode.put("error", "Attacked card is not of type 'Tank'.");
                    output.addPOJO(outputNode);
                } else {
                    // just attack
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
     * @param a
     * @param playerOneHero
     * @param playerTwoHero
     */
    public static void cardAttackHero(final ArrayNode output, final ActionsInput command,
                                      final ArrayList<LinkedList<Minion>> playingTable,
                                      final int turn, final Utils a, final Hero playerOneHero,
                                      final Hero playerTwoHero) {
        int cardAttackerX = command.getCardAttacker().getX();
        int cardAttackerY = command.getCardAttacker().getY();
        Minion cardAttacker;

        if (playingTable.get(cardAttackerX).size() > cardAttackerY) {
            cardAttacker = playingTable.get(cardAttackerX).get(cardAttackerY);
        } else {
            return;
        }

        if (cardAttacker.getIsFrozen() == 1) {
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("command", "useAttackHero");
            outputNode.putPOJO("cardAttacker", command.getCardAttacker());
            outputNode.put("error", "Attacker card is frozen.");
            output.addPOJO(outputNode);
        } else if (cardAttacker.getAttackUsed() == 1) {
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("command", "useAttackHero");
            outputNode.putPOJO("cardAttacker", command.getCardAttacker());
            outputNode.put("error", "Attacker card has already attacked this turn.");
            output.addPOJO(outputNode);
        } else {
            int isTank = Utils.isTank(playingTable, turn);

            if (isTank == 1) {
                ObjectNode outputNode = objectMapper.createObjectNode();
                outputNode.put("command", "useAttackHero");
                outputNode.putPOJO("cardAttacker", command.getCardAttacker());
                outputNode.put("error", "Attacked card is not of type 'Tank'.");
                output.addPOJO(outputNode);
                return;
            }

            if (turn == 1) {
                cardAttacker.setAttackUsed(1);
                if (playerTwoHero.getHealth() <= cardAttacker.getAttackDamage()) {
                    // player One wins
                    a.setPlayerOneWins(a.getPlayerOneWins() + 1);
                    ObjectNode outputNode = objectMapper.createObjectNode();
                    outputNode.put("gameEnded", "Player one killed the enemy hero.");
                    output.addPOJO(outputNode);
                    return;
                } else {
                    playerTwoHero.setHealth(playerTwoHero.getHealth()
                            - cardAttacker.getAttackDamage());
                }
            } else if (turn == 2) {
                cardAttacker.setAttackUsed(1);
                if (playerOneHero.getHealth() <= cardAttacker.getAttackDamage()) {
                    // player Two wins
                    a.setPlayerTwoWins(a.getPlayerTwoWins() +  1);
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
     * @param a
     * @param playerOneHero
     * @param playerTwoHero
     */
    public static void useHeroAbility(final ArrayNode output, final ActionsInput command,
                                      final ArrayList<LinkedList<Minion>> playingTable,
                                      final int turn, final Utils a, final Hero playerOneHero,
                                      final Hero playerTwoHero) {
        int affectedRow = command.getAffectedRow();

        if ((turn == 1 && a.getPlayerOneMana() < playerOneHero.getMana())
                || (turn == 2 && a.getPlayerTwoMana() < playerTwoHero.getMana())) {
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("command", "useHeroAbility");
            outputNode.put("affectedRow", affectedRow);
            outputNode.put("error", "Not enough mana to use hero's ability.");
            output.addPOJO(outputNode);
            return;
        }
        if ((turn == 1 && playerOneHero.getAttackUsed() == 1)
                || (turn == 2 && playerTwoHero.getAttackUsed() == 1)) {
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("command", "useHeroAbility");
            outputNode.put("affectedRow", affectedRow);
            outputNode.put("error", "Hero has already attacked this turn.");
            output.addPOJO(outputNode);
            return;
        }
        if ((turn == 1 && (playerOneHero.getName().equals("Lord Royce")
                || playerOneHero.getName().equals("Empress Thorina"))
                && (affectedRow == 2 || affectedRow == 3))
                || (turn == 2 && (playerTwoHero.getName().equals("Lord Royce")
                || playerTwoHero.getName().equals("Empress Thorina"))
                && (affectedRow == 0 || affectedRow == 1))) {
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("command", "useHeroAbility");
            outputNode.put("affectedRow", affectedRow);
            outputNode.put("error", "Selected row does not belong to the enemy.");
            output.addPOJO(outputNode);
            return;
        }
        if ((turn == 1 && (playerOneHero.getName().equals("General Kocioraw")
                || playerOneHero.getName().equals("King Mudface"))
                && (affectedRow == 0 || affectedRow == 1))
                || (turn == 2 && (playerTwoHero.getName().equals("General Kocioraw")
                || playerTwoHero.getName().equals("King Mudface"))
                && (affectedRow == 2 || affectedRow == 3))) {
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("command", "useHeroAbility");
            outputNode.put("affectedRow", affectedRow);
            outputNode.put("error", "Selected row does not belong to the current player.");
            output.addPOJO(outputNode);
            return;
        }
        // attack
        if (turn == 1) {
            a.setPlayerOneMana(a.getPlayerOneMana() - playerOneHero.getMana());
            playerOneHero.setAttackUsed(1);
        } else if (turn == 2) {
            a.setPlayerTwoMana(a.getPlayerTwoMana() - playerTwoHero.getMana());
            playerTwoHero.setAttackUsed(1);
        }

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
     * @param a
     * @param turn
     */
    public static void useEnvironmentCard(final ArrayNode output, final ActionsInput command,
                                          final LinkedList<Cards> playerOneDeckInHand,
                                          final LinkedList<Cards> playerTwoDeckInHand,
                                          final ArrayList<LinkedList<Minion>> playingTable,
                                          final Utils a, final int turn) {
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
        if (!cardName.equals("Firestorm") && !cardName.equals("Winterfell")
                && !cardName.equals("Heart Hound")) {
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("command", "useEnvironmentCard");
            outputNode.put("error", "Chosen card is not of type environment.");
            outputNode.put("handIdx", handIdx);
            outputNode.put("affectedRow", affectedRow);
            output.addPOJO(outputNode);
        } else if ((turn == 1 && playerOneDeckInHand.get(handIdx).getMana() > a.getPlayerOneMana())
                || (turn == 2 && playerTwoDeckInHand.get(handIdx).getMana()
                > a.getPlayerTwoMana())) {
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("command", "useEnvironmentCard");
            outputNode.put("error", "Not enough mana to use environment card.");
            outputNode.put("handIdx", handIdx);
            outputNode.put("affectedRow", affectedRow);
            output.addPOJO(outputNode);
        } else if ((turn == 1 && (affectedRow == 2 || affectedRow == 3))
                || (turn == 2 && (affectedRow == 0 || affectedRow == 1))) {
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("command", "useEnvironmentCard");
            outputNode.put("error", "Chosen row does not belong to the enemy.");
            outputNode.put("handIdx", handIdx);
            outputNode.put("affectedRow", affectedRow);
            output.addPOJO(outputNode);
        } else if (cardName.equals("Heart Hound")) {
            if ((turn == 1 && affectedRow == 1 && playingTable.get(2).size() == 5)
                    || (turn == 1 && affectedRow == 0 && playingTable.get(3).size() == 5)
                    || (turn == 2 && affectedRow == 2 && playingTable.get(1).size() == 5)
                    || (turn == 2 && affectedRow == 3 && playingTable.get(0).size() == 5)) {
                ObjectNode outputNode = objectMapper.createObjectNode();
                outputNode.put("command", "useEnvironmentCard");
                outputNode.put("error", "Cannot steal enemy card since the player's row is full.");
                outputNode.put("handIdx", handIdx);
                outputNode.put("affectedRow", affectedRow);
                output.addPOJO(outputNode);
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
                a.setPlayerOneMana(a.getPlayerOneMana()
                        - playerOneDeckInHand.get(handIdx).getMana());
                playerOneDeckInHand.remove(handIdx);
                return;
            }
            if (turn == 1 && affectedRow == 0) {
                for (int k = 0; k < playingTable.get(0).size(); k++) {
                    if (playingTable.get(0).get(k).getHealth() > maxHealth) {
                        maxHealth = playingTable.get(0).get(k).getHealth();
                        maxHealthIdx = k;
                    }
                }
                playingTable.get(3).add(playingTable.get(0).remove(maxHealthIdx));
                a.setPlayerOneMana(a.getPlayerOneMana()
                        - playerOneDeckInHand.get(handIdx).getMana());
                playerOneDeckInHand.remove(handIdx);
                return;
            }
            if (turn == 2 && affectedRow == 2) {
                for (int k = 0; k < playingTable.get(2).size(); k++) {
                    if (playingTable.get(2).get(k).getHealth() > maxHealth) {
                        maxHealth = playingTable.get(2).get(k).getHealth();
                        maxHealthIdx = k;
                    }
                }
                playingTable.get(1).add(playingTable.get(2).remove(maxHealthIdx));
                a.setPlayerOneMana(a.getPlayerOneMana()
                        - playerTwoDeckInHand.get(handIdx).getMana());
                playerTwoDeckInHand.remove(handIdx);
                return;
            }
            if (turn == 2 && affectedRow == 3) {
                for (int k = 0; k < playingTable.get(3).size(); k++) {
                    if (playingTable.get(3).get(k).getHealth() > maxHealth) {
                        maxHealth = playingTable.get(3).get(k).getHealth();
                        maxHealthIdx = k;
                    }
                }
                playingTable.get(0).add(playingTable.get(3).remove(maxHealthIdx));
                a.setPlayerOneMana(a.getPlayerOneMana()
                        - playerTwoDeckInHand.get(handIdx).getMana());
                playerTwoDeckInHand.remove(handIdx);
            }
        } else if (cardName.equals("Firestorm")) {
            for (int k = 0; k < playingTable.get(affectedRow).size(); k++) {
                Minion card = playingTable.get(affectedRow).get(k);
                card.setHealth(card.getHealth() - 1);
                if (playingTable.get(affectedRow).get(k).getHealth() <= 0) {
                    // card died
                    playingTable.get(affectedRow).remove(k);
                    k--;
                }
            }
            if (turn == 1) {
                a.setPlayerOneMana(a.getPlayerOneMana()
                        - playerOneDeckInHand.get(handIdx).getMana());
                playerOneDeckInHand.remove(handIdx);
            }
            if (turn == 2) {
                a.setPlayerTwoMana(a.getPlayerTwoMana()
                        - playerTwoDeckInHand.get(handIdx).getMana());
                playerTwoDeckInHand.remove(handIdx);
            }
        } else if (cardName.equals("Winterfell")) {
            for (int k = 0; k < playingTable.get(affectedRow).size(); k++) {
                playingTable.get(affectedRow).get(k).setIsFrozen(1);
            }

            if (turn == 1) {
                a.setPlayerOneMana(a.getPlayerOneMana()
                        - playerOneDeckInHand.get(handIdx).getMana());
                playerOneDeckInHand.remove(handIdx);
            } else if (turn == 2) {
                a.setPlayerTwoMana(a.getPlayerTwoMana()
                        - playerTwoDeckInHand.get(handIdx).getMana());
                playerTwoDeckInHand.remove(handIdx);
            }
        }
    }
}
