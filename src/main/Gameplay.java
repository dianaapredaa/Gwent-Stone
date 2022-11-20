package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;

import java.util.ArrayList;
import java.util.LinkedList;

public class Gameplay {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static void placeCard(ArrayNode output,
                                 ActionsInput command, int turn,
                                 ArrayList<LinkedList<Minion>> playingTable,
                                 LinkedList<Cards> playerOneDeckInHand,
                                 LinkedList<Cards> playerTwoDeckInHand,
                                 int playerOneMana, int playerTwoMana) {
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
            } else if (playerOneDeckInHand.get(handIdx).getMana() > playerOneMana) {
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
                    playerOneMana -= cardToPlace.getMana();
                    playingTable.get(2).add((Minion) playerOneDeckInHand.remove(handIdx));
                }
            } else {
                if (playingTable.get(3).size() == 5) {
                    ObjectNode outputNode = objectMapper.createObjectNode();
                    outputNode.put("command", "placeCard");
                    outputNode.put("error", "Cannot place card on table since row is full.");
                    outputNode.put("handIdx", handIdx);
                    output.addPOJO(outputNode);
                } else {
                    playerOneMana -= cardToPlace.getMana();
                    playingTable.get(3).add((Minion) playerOneDeckInHand.remove(handIdx));
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
            } else if (playerTwoDeckInHand.get(handIdx).getMana() > playerTwoMana) {
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
                    playerTwoMana -= cardToPlace.getMana();
                    playingTable.get(1).add((Minion) playerTwoDeckInHand.remove(handIdx));
                }
            } else {
                if (playingTable.get(0).size() == 5) {
                    ObjectNode outputNode = objectMapper.createObjectNode();
                    outputNode.put("command", "placeCard");
                    outputNode.put("error", "Cannot place card on table since row is full.");
                    outputNode.put("handIdx", handIdx);
                    output.addPOJO(outputNode);
                } else {
                    playerTwoMana -= cardToPlace.getMana();
                    playingTable.get(0).add((Minion) playerTwoDeckInHand.remove(handIdx));
                }
            }
        }
    }
}
