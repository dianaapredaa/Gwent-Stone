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

    public static void getCardsInHand(ObjectMapper objectMapper, ArrayNode output,
                                      ActionsInput command, LinkedList<Cards> playerOneDeckInHand,
                                      LinkedList<Cards> playerTwoDeckInHand) {
        int playerIdx = command.getPlayerIdx();
        ObjectNode outputNode = objectMapper.createObjectNode();
        if (playerIdx == 1) {
            outputNode.put("command", "getCardsInHand");
            outputNode.put("playerIdx", command.getPlayerIdx());
            outputNode.putPOJO("output", new ArrayList<>(playerOneDeckInHand));
        } else if (playerIdx == 2) {
            outputNode.put("command", "getCardsInHand");
            outputNode.put("playerIdx", command.getPlayerIdx());
            outputNode.putPOJO("output", new ArrayList<>(playerTwoDeckInHand));
        }
        output.addPOJO(outputNode);
    }

    public static void getPlayerDeck(ObjectMapper objectMapper, ArrayNode output,
                                     ActionsInput command, LinkedList<Cards> playerOneDeck,
                                     LinkedList<Cards> playerTwoDeck) {
        int playerIdx = command.getPlayerIdx();
        ObjectNode outputNode = objectMapper.createObjectNode();
        if (playerIdx == 1) {
            outputNode.put("command", "getPlayerDeck");
            outputNode.put("playerIdx", command.getPlayerIdx());
            outputNode.putPOJO("output", new ArrayList<>(playerOneDeck));
        } else if (playerIdx == 2) {
            outputNode.put("command", "getPlayerDeck");
            outputNode.put("playerIdx", command.getPlayerIdx());
            outputNode.putPOJO("output", new ArrayList<>(playerTwoDeck));
        }
        output.addPOJO(outputNode);
    }
}
