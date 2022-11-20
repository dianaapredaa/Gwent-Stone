package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Statistics {
    public static void getTotalGamesPlayed(ObjectMapper objectMapper, ArrayNode output, int i) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "getTotalGamesPlayed");
        outputNode.put("output", i + 1);
        output.addPOJO(outputNode);
    }

    public static void getPlayerOneWins(ObjectMapper objectMapper, ArrayNode output, int playerOneWins) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "getPlayerOneWins");
        outputNode.put("command", playerOneWins);
        output.addPOJO(outputNode);
    }

    public static void getPlayerTwoWins(ObjectMapper objectMapper, ArrayNode output, int playerTwoWins) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "getPlayerOneWins");
        outputNode.put("command", playerTwoWins);
        output.addPOJO(outputNode);
    }
}
