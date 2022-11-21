package main;

import checker.CheckerConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.Input;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class Debug {
    public Debug() {
    }
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void getCardsInHand(ArrayNode output, ActionsInput command,
                                      LinkedList<Cards> playerOneDeckInHand,
                                      LinkedList<Cards> playerTwoDeckInHand) {
        int playerIdx = command.getPlayerIdx();

        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "getCardsInHand");
        outputNode.put("playerIdx", command.getPlayerIdx());

        LinkedList<Cards> playerOneDeckInHandDeepCopy = new LinkedList<>();
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

        LinkedList<Cards> playerTwoDeckInHandDeepCopy = new LinkedList<>();
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

    public static void getPlayerDeck(ArrayNode output,
                                     ActionsInput command, LinkedList<Cards> playerOneDeck,
                                     LinkedList<Cards> playerTwoDeck) {
        int playerIdx = command.getPlayerIdx();

        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "getPlayerDeck");
        outputNode.put("playerIdx", command.getPlayerIdx());

        // display player deck
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
            outputNode.putPOJO("output", playerTwoDeckDeepCopy);        }
        output.addPOJO(outputNode);
    }

    public static void getCardsOnTable(ArrayNode output,
                                       ArrayList<LinkedList<Minion>> playingTable) {
        // display playing table
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "getCardsOnTable");

        LinkedList<LinkedList<Minion>> tableDeepCopy = new LinkedList<>();

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

    public static void getPlayerTurn(ArrayNode output, int turn) {
        // display player turn
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "getPlayerTurn");
        outputNode.put("output", turn);
        output.addPOJO(outputNode);
    }

    public static void getPlayerHero(ArrayNode output,
                                     ActionsInput command, Hero playerOneHero,
                                     Hero playerTwoHero) {
        int playerIdx = command.getPlayerIdx();
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "getPlayerHero");
        outputNode.put("playerIdx", playerIdx);

        // display player hero
        if (playerIdx == 1) {
            outputNode.putPOJO("output", playerOneHero);
        } else {
            outputNode.putPOJO("output", playerTwoHero);
        }
        output.addPOJO(outputNode);
    }

    public static void getCardsAtPosition(ArrayNode output, ActionsInput command,
                                          ArrayList<LinkedList<Minion>> playingTable) {
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

    public static void getPlayerMana(ArrayNode output,
                                     ActionsInput command, int playerOneMana, int playerTwoMana) {
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

    public static void getEnvironmentCardsInHand(ArrayNode output,
                                                 ActionsInput command,
                                                 LinkedList<Cards> playerOneDeckInHand,
                                                 LinkedList<Cards> playerTwoDeckInHand ) {
        int playerIdx = command.getPlayerIdx();
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "getEnvironmentCardsInHand");
        outputNode.put("playerIdx", playerIdx);
        ArrayList<Cards> environmentCards = new ArrayList<>();

        // check for environment cards
        if (playerIdx == 1) {
            for (Cards cards : playerOneDeckInHand) {
                if (cards.getName().equals("Firestorm")
                        || cards.getName().equals("HeartHound")
                        || cards.getName().equals("Winterfell")) {
                    environmentCards.add(cards);
                }
            }
        } else if (playerIdx == 2) {
            for (Cards cards : playerTwoDeckInHand) {
                if (cards.getName().equals("Firestorm")
                        || cards.getName().equals("HeartHound")
                        || cards.getName().equals("Winterfell")) {
                    environmentCards.add(cards);
                }
            }
        }
        outputNode.putPOJO("output", environmentCards);
        output.addPOJO(outputNode);
    }

    public static void getFrozenCardsOnTable(ArrayNode output,
                                             ActionsInput command,
                                             ArrayList<LinkedList<Minion>> playingTable) {
        LinkedList<Minion> frozenCardsOnTable = new LinkedList<>();

        // check if card is frozen
        for (LinkedList<Minion> minions : playingTable)
            for (Minion minion : minions)
                if (minion.getIsFrozen() == 1)
                    frozenCardsOnTable.add(new Minion(minion));

        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "getFrozenCardsOnTable");
        outputNode.putPOJO("output", frozenCardsOnTable);
        output.addPOJO(outputNode);
    }

}
