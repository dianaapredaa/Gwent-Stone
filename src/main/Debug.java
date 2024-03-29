// Copyright 2022-2023 Preda Diana 324CA
package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;

import java.util.ArrayList;
import java.util.LinkedList;

public final class Debug {
    private Debug() {
    }
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     *
     * @param output
     * @param command
     * @param playerOneDeckInHand
     * @param playerTwoDeckInHand
     */
    // get player's cards in hand
    public static void getCardsInHand(final ArrayNode output, final ActionsInput command,
                                      final LinkedList<Cards> playerOneDeckInHand,
                                      final LinkedList<Cards> playerTwoDeckInHand) {
        int playerIdx = command.getPlayerIdx();

        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "getCardsInHand");
        outputNode.put("playerIdx", command.getPlayerIdx());

        LinkedList<Cards> playerOneDeckInHandDeepCopy = new LinkedList<>();
        LinkedList<Cards> playerTwoDeckInHandDeepCopy = new LinkedList<>();

        // deep copy because changing are made during the game
        // and we want to display player's cards at the exact time of the function call
        for (Cards card : playerOneDeckInHand) {
            if (card.getName().equals("Winterfell") || card.getName().equals("Firestorm")
                    || card.getName().equals("Heart Hound")) {
                // environment card
                playerOneDeckInHandDeepCopy.add(new Environment((Environment) card));
            } else {
                // minion
                playerOneDeckInHandDeepCopy.add(new Minion((Minion) card));
            }
        }

        for (Cards card : playerTwoDeckInHand) {
            if (card.getName().equals("Winterfell") || card.getName().equals("Firestorm")
                    || card.getName().equals("Heart Hound")) {
                // environment card
                playerTwoDeckInHandDeepCopy.add(new Environment((Environment) card));
            } else {
                // minion
                playerTwoDeckInHandDeepCopy.add(new Minion((Minion) card));
            }
        }

        // display cards in hand
        if (playerIdx == 1) {
            outputNode.putPOJO("output", playerOneDeckInHandDeepCopy);
        } else if (playerIdx == 2) {
            outputNode.putPOJO("output", playerTwoDeckInHandDeepCopy);
        }
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param command
     * @param playerOneDeck
     * @param playerTwoDeck
     */
    // get player's deck
    public static void getPlayerDeck(final ArrayNode output, final ActionsInput command,
                                     final LinkedList<Cards> playerOneDeck,
                                     final LinkedList<Cards> playerTwoDeck) {
        int playerIdx = command.getPlayerIdx();

        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "getPlayerDeck");
        outputNode.put("playerIdx", command.getPlayerIdx());

        // deep copy because changing are made during the game
        // and we want to display player's deck at the exact time of the function call
        if (playerIdx == 1) {
            LinkedList<Cards> playerOneDeckDeepCopy = new LinkedList<>();
            for (Cards card : playerOneDeck) {
                if (card.getName().equals("Winterfell") || card.getName().equals("Firestorm")
                        || card.getName().equals("Heart Hound")) {
                    // environment card
                    playerOneDeckDeepCopy.add(new Environment((Environment) card));
                } else {
                    // minion
                    playerOneDeckDeepCopy.add(new Minion((Minion) card));
                }
            }
            outputNode.putPOJO("output", playerOneDeckDeepCopy);
        } else if (playerIdx == 2) {
            LinkedList<Cards> playerTwoDeckDeepCopy = new LinkedList<>();
            for (Cards card : playerTwoDeck) {
                if (card.getName().equals("Winterfell") || card.getName().equals("Firestorm")
                        || card.getName().equals("Heart Hound")) {
                    // environment card
                    playerTwoDeckDeepCopy.add(new Environment((Environment) card));
                } else {
                    // minion
                    playerTwoDeckDeepCopy.add(new Minion((Minion) card));
                }
            }
            outputNode.putPOJO("output", playerTwoDeckDeepCopy);
        }
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param playingTable
     */
    public static void getCardsOnTable(final ArrayNode output,
                                       final ArrayList<LinkedList<Minion>> playingTable) {
        // display playing table
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "getCardsOnTable");

        LinkedList<LinkedList<Minion>> tableDeepCopy = new LinkedList<>();

        // deep copy because changing are made during the game
        // and we want to display cards on the playing table at the exact time of the function call
        for (LinkedList<Minion> row : playingTable) {
            LinkedList<Minion> rowDeepCopy = new LinkedList<>();
            for (Minion minion : row) {
                rowDeepCopy.add(new Minion(minion));
            }
            tableDeepCopy.add(rowDeepCopy);
        }

        outputNode.putPOJO("output", tableDeepCopy);
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param turn
     */
    public static void getPlayerTurn(final ArrayNode output, final int turn) {
        // display player turn
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "getPlayerTurn");
        outputNode.put("output", turn);
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param command
     * @param playerOneHero
     * @param playerTwoHero
     */
    public static void getPlayerHero(final ArrayNode output, final ActionsInput command,
                                     final Hero playerOneHero, final Hero playerTwoHero) {
        int playerIdx = command.getPlayerIdx();
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "getPlayerHero");
        outputNode.put("playerIdx", playerIdx);

        // display player hero
        if (playerIdx == 1) {
            outputNode.putPOJO("output", new Hero(playerOneHero));
        } else {
            outputNode.putPOJO("output", new Hero(playerTwoHero));
        }
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param command
     * @param playingTable
     */
    public static void getCardsAtPosition(final ArrayNode output, final ActionsInput command,
                                          final ArrayList<LinkedList<Minion>> playingTable) {
        int x = command.getX();
        int y = command.getY();
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "getCardAtPosition");
        outputNode.put("x", x);
        outputNode.put("y", y);

        // display indicated card from table
        if (y < playingTable.get(x).size()) {
            outputNode.putPOJO("output", new Minion(playingTable.get(x).get(y)));
        } else {
            outputNode.put("output", "No card available at that position.");
        }
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param command
     * @param playerOneMana
     * @param playerTwoMana
     */
    public static void getPlayerMana(final ArrayNode output, final ActionsInput command,
                                     final int playerOneMana, final int playerTwoMana) {
        int playerIdx = command.getPlayerIdx();
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "getPlayerMana");
        outputNode.put("playerIdx", playerIdx);

        // display player mana
        if (playerIdx == 1) {
            outputNode.put("output", playerOneMana);
        } else {
            outputNode.put("output", playerTwoMana);
        }
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param command
     * @param playerOneDeckInHand
     * @param playerTwoDeckInHand
     */
    public static void getEnvironmentCardsInHand(final ArrayNode output, final ActionsInput command,
                                                 final LinkedList<Cards> playerOneDeckInHand,
                                                 final LinkedList<Cards> playerTwoDeckInHand) {
        int playerIdx = command.getPlayerIdx();
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "getEnvironmentCardsInHand");
        outputNode.put("playerIdx", playerIdx);
        ArrayList<Cards> environmentCards = new ArrayList<>();

        // check for environment cards
        if (playerIdx == 1) {
            for (Cards card : playerOneDeckInHand) {
                if (card.getName().equals("Firestorm")
                        || card.getName().equals("Heart Hound")
                        || card.getName().equals("Winterfell")) {
                    environmentCards.add(new Environment((Environment) card));
                }
            }
        } else if (playerIdx == 2) {
            for (Cards card : playerTwoDeckInHand) {
                if (card.getName().equals("Firestorm")
                        || card.getName().equals("Heart Hound")
                        || card.getName().equals("Winterfell")) {
                    environmentCards.add(new Environment((Environment) card));
                }
            }
        }
        outputNode.putPOJO("output", environmentCards);
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param playingTable
     */
    public static void getFrozenCardsOnTable(final ArrayNode output,
                                             final ArrayList<LinkedList<Minion>> playingTable) {
        LinkedList<Minion> frozenCardsOnTable = new LinkedList<>();

        // check if card is frozen
        for (LinkedList<Minion> minions : playingTable) {
            for (Minion minion : minions) {
                if (minion.getIsFrozen() == 1) {
                    frozenCardsOnTable.add(new Minion(minion));
                }
            }
        }
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "getFrozenCardsOnTable");
        outputNode.putPOJO("output", frozenCardsOnTable);
        output.addPOJO(outputNode);
    }

}
