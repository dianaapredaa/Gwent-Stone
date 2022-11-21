package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class Statistics {
    private static ObjectMapper objectMapper = new ObjectMapper();

    private Statistics() {
    }

    /**
     *
     * @param output
     * @param i
     */
    public static void getTotalGamesPlayed(final ArrayNode output, final int i) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "getTotalGamesPlayed");
        outputNode.put("output", i + 1);
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param playerOneWins
     */
    public static void getPlayerOneWins(final ArrayNode output, final int playerOneWins) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "getPlayerOneWins");
        outputNode.put("output", playerOneWins);
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param playerTwoWins
     */
    public static void getPlayerTwoWins(final ArrayNode output, final int playerTwoWins) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "getPlayerTwoWins");
        outputNode.put("output", playerTwoWins);
        output.addPOJO(outputNode);
    }
}
