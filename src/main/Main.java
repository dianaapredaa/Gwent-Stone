package main;

import checker.Checker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import checker.CheckerConstants;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */

//    public ArrayList<ArrayList<Cards>> setCardType(DecksInput decksInput) {
//
//    }
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Input inputData = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + filePath1),
                Input.class);

        ArrayNode output = objectMapper.createArrayNode();

        //TODO add here the entry point to your implementation

        DecksInput playerOne = inputData.getPlayerOneDecks();
        DecksInput playerTwo = inputData.getPlayerTwoDecks();

        int nrOfGames = inputData.getGames().size();
        for (int i = 0; i < nrOfGames; i++) {
            StartGameInput newGame = inputData.getGames().get(i).getStartGame();

            // current game command list
            ArrayList<ActionsInput> commandList = inputData.getGames().get(i).getActions();

            // players selected decks
            ArrayList<CardInput> playerOneDeck = playerOne.getDecks().get(newGame.getPlayerOneDeckIdx());
            ArrayList<CardInput> playerTwoDeck = playerOne.getDecks().get(newGame.getPlayerTwoDeckIdx());

            // players cards in hand (we begin with hand empty, one card is added per round)
            ArrayList<CardInput> playerOneDeckInHand = new ArrayList<>();
            ArrayList<CardInput> playerTwoDeckInHand = new ArrayList<>();

            Collections.shuffle(playerOneDeck, new Random(newGame.getShuffleSeed()));
            Collections.shuffle(playerTwoDeck, new Random(newGame.getShuffleSeed()));

            playerOneDeckInHand.add(playerOneDeck.remove(0));
            playerTwoDeckInHand.add(playerTwoDeck.remove(0));

            ArrayList<ArrayList<CardInput>> playingTable = new ArrayList<>(4);

            ArrayList<CardInput> row0 = new ArrayList<>(5);
            ArrayList<CardInput> row1 = new ArrayList<>(5);
            ArrayList<CardInput> row2 = new ArrayList<>(5);
            ArrayList<CardInput> row3 = new ArrayList<>(5);

            playingTable.add(row0);
            playingTable.add(row1);
            playingTable.add(row2);
            playingTable.add(row3);

            CardInput playerOneHero = newGame.getPlayerOneHero();
            CardInput playerTwoHero = newGame.getPlayerTwoHero();

            playerOneHero.setHealth(30);
            playerTwoHero.setHealth(30);

            // we iterate through the command list
            for (int j = 0; j < commandList.size(); j++) {
                ActionsInput command = commandList.get(j);
                int playerIdx;
                int cardAttackerX;
                int cardAttackerY;
                int cardAttackedX;
                int cardAttackedY;
                int affectedRow;
                int handIdx;

                switch (command.getCommand()) {
                    case ("getCardsInHand"):

                        playerIdx = command.getPlayerIdx();
                        if (playerIdx == 1) {
                            ObjectNode outputNode = objectMapper.createObjectNode();
                            outputNode.put("command", "getCardsInHand");
                            outputNode.put("playerIdx", command.getPlayerIdx());
                            outputNode.putPOJO("output", playerOneDeckInHand);
                            output.addPOJO(outputNode);

                            //                          output.addPOJO(playerOneDeckInHand);
                        } else {
                            ObjectNode outputNode = objectMapper.createObjectNode();
                            outputNode.put("command", "getCardsInHand");
                            outputNode.put("playerIdx", command.getPlayerIdx());
                            outputNode.putPOJO("output", playerTwoDeckInHand);
                            output.addPOJO(outputNode);
                        }
                        break;

                    case ("getPlayerDeck"):
                        playerIdx = command.getPlayerIdx();
                        if (playerIdx == 1) {
                            ObjectNode outputNode = objectMapper.createObjectNode();
                            outputNode.put("command", "getPlayerDeck");
                            outputNode.put("playerIdx", command.getPlayerIdx());
                            outputNode.putPOJO("output", playerOneDeck);
                            output.addPOJO(outputNode);
                        } else {
                            ObjectNode outputNode = objectMapper.createObjectNode();
                            outputNode.put("command", "getPlayerDeck");
                            outputNode.put("playerIdx", command.getPlayerIdx());
                            outputNode.putPOJO("output", playerTwoDeck);
                            output.addPOJO(outputNode);
                        }
                        break;

                    case ("getCardsOnTable"):
                        output.addPOJO(playingTable);
                        break;

                    case ("getPlayerTurn"):
                        break;

                    case ("getPlayerHero"):
                        playerIdx = command.getPlayerIdx();
                        ObjectNode outputNode = objectMapper.createObjectNode();
                        outputNode.put("command", "getPlayerHero");
                        outputNode.put("playerIdx", playerIdx);

                        if (playerIdx == 1) {
                            outputNode.putPOJO("output", newGame.getPlayerOneHero());
                        } else {
                            outputNode.putPOJO("output", newGame.getPlayerTwoHero());
                        }
                        output.addPOJO(outputNode);
                        break;

                    case ("getCardAtPosition"):
                        int x = command.getX();
                        int y = command.getY();
//                        output.addPOJO(playingTable.get(x).get(y));
                        break;

                    case ("getPlayerMana"):
                        playerIdx = command.getPlayerIdx();
                        if (playerIdx == 1) {

                        } else {

                        }
                        break;

                    case ("getEnvironmentCardsInHand"):
                        playerIdx = command.getPlayerIdx();
                        if (playerIdx == 1) {

                        } else {
                        }
                        break;

                    case ("getFrozenCardsOnTable"):
                        break;

                    case ("getTotalGamesPlayed"):
                        break;

                    case ("getPlayerOneWins"):
                        break;

                    case ("endPlayerTurn"):
                        break;

                    case ("placeCard"):
                        handIdx = command.getHandIdx();
                        break;

                    case ("cardUsesAttack"):
                        cardAttackerX = command.getX();
                        cardAttackerY = command.getY();
                        cardAttackedX = command.getX();
                        cardAttackedY = command.getY();
                        break;

                    case ("cardUsesAbility"):
                        cardAttackerX = command.getX();
                        cardAttackerY = command.getY();
                        cardAttackedX = command.getX();
                        cardAttackedY = command.getY();
                        break;

                    case ("useAttackHero"):
                        cardAttackerX = command.getX();
                        cardAttackerY = command.getY();
                        break;

                    case ("useHeroAbility"):
                        affectedRow = command.getAffectedRow();;
                        break;

                    case ("useEnvironmentCard"):
                        handIdx = command.getHandIdx();
                        affectedRow = command.getAffectedRow();
                }
            }


        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }
}
