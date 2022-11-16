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
import java.util.*;

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
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Input inputData = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + filePath1),
                Input.class);

        ArrayNode output = objectMapper.createArrayNode();

        //TODO add here the entry point to your implementation

        LinkedList<LinkedList<Cards>> playerOne = Cards.setCardType(inputData.getPlayerOneDecks());
        LinkedList<LinkedList<Cards>> playerTwo = Cards.setCardType(inputData.getPlayerTwoDecks());

        int nrOfGames = inputData.getGames().size();
        for (int i = 0; i < nrOfGames; i++) {
            int playerOneMana = 1;
            int playerTwoMana = 1;

            StartGameInput newGame = inputData.getGames().get(i).getStartGame();

            // current game command list
            ArrayList<ActionsInput> commandList = inputData.getGames().get(i).getActions();

            // players selected decks
            LinkedList<Cards> playerOneDeck = playerOne.get(newGame.getPlayerOneDeckIdx());
            LinkedList<Cards> playerTwoDeck = playerTwo.get(newGame.getPlayerTwoDeckIdx());

            // players cards in hand (we begin with hand empty, one card is added per round)
            LinkedList<Cards> playerOneDeckInHand = new LinkedList<>();
            LinkedList<Cards> playerTwoDeckInHand = new LinkedList<>();

            // shuffle decks before starting
            Collections.shuffle(playerOneDeck, new Random(newGame.getShuffleSeed()));
            Collections.shuffle(playerTwoDeck, new Random(newGame.getShuffleSeed()));

            // get first card
            playerOneDeckInHand.addLast(playerOneDeck.removeFirst());
            playerTwoDeckInHand.addLast(playerTwoDeck.removeFirst());

            // initializing playing Table (4 x 5)
            ArrayList<ArrayList<Cards>> playingTable = new ArrayList<>(4);

            ArrayList<Cards> row0 = new ArrayList<>(5);
            ArrayList<Cards> row1 = new ArrayList<>(5);
            ArrayList<Cards> row2 = new ArrayList<>(5);
            ArrayList<Cards> row3 = new ArrayList<>(5);

            playingTable.add(0, row0);
            playingTable.add(1, row1);
            playingTable.add(2, row2);
            playingTable.add(3, row3);

            // get Heros
            Hero playerOneHero = new Hero(newGame.getPlayerOneHero());
            Hero playerTwoHero = new Hero(newGame.getPlayerTwoHero());

            // set initial health at 30
            playerOneHero.setHealth(30);
            playerTwoHero.setHealth(30);

            // get first turn
            int turn = newGame.getStartingPlayer();
            int numberOfTurns = 1;

            // iterate through the command list
            for (int j = 0; j < commandList.size(); j++) {
                ActionsInput command = commandList.get(j);
                int playerIdx;
                int cardAttackerX;
                int cardAttackerY;
                int cardAttackedX;
                int cardAttackedY;
                int affectedRow;
                int handIdx;
                ObjectNode outputNode;

                switch (command.getCommand()) {
                    case ("getCardsInHand"):
                        playerIdx = command.getPlayerIdx();
                        if (playerIdx == 1) {
                            outputNode = objectMapper.createObjectNode();
                            outputNode.put("command", "getCardsInHand");
                            outputNode.put("playerIdx", command.getPlayerIdx());
                            outputNode.putPOJO("output", playerOneDeckInHand);
                        } else {
                            outputNode = objectMapper.createObjectNode();
                            outputNode.put("command", "getCardsInHand");
                            outputNode.put("playerIdx", command.getPlayerIdx());
                            outputNode.putPOJO("output", playerTwoDeckInHand);
                        }
                        output.addPOJO(outputNode);
                        break;

                    case ("getPlayerDeck"):
                        playerIdx = command.getPlayerIdx();
                        if (playerIdx == 1) {
                            outputNode = objectMapper.createObjectNode();
                            outputNode.put("command", "getPlayerDeck");
                            outputNode.put("playerIdx", command.getPlayerIdx());
                            outputNode.putPOJO("output", playerOneDeck);
                        } else {
                            outputNode = objectMapper.createObjectNode();
                            outputNode.put("command", "getPlayerDeck");
                            outputNode.put("playerIdx", command.getPlayerIdx());
                            outputNode.putPOJO("output", playerTwoDeck);
                        }
                        output.addPOJO(outputNode);
                        break;

                    case ("getCardsOnTable"):
                        outputNode = objectMapper.createObjectNode();
                        outputNode.put("command", "getCardsOnTable");
                        outputNode.putPOJO("output", playingTable);
                        output.addPOJO(outputNode);
                        break;

                    case ("getPlayerTurn"):
                        outputNode = objectMapper.createObjectNode();
                        outputNode.put("command", "getPlayerTurn");
                        outputNode.put("output", turn);
                        output.addPOJO(outputNode);
                        break;

                    case ("getPlayerHero"):
                        playerIdx = command.getPlayerIdx();
                        outputNode = objectMapper.createObjectNode();
                        outputNode.put("command", "getPlayerHero");
                        outputNode.put("playerIdx", playerIdx);

                        if (playerIdx == 1) {
                            outputNode.putPOJO("output", playerOneHero);
                        } else {
                            outputNode.putPOJO("output", playerTwoHero);
                        }
                        output.addPOJO(outputNode);
                        break;

                    case ("getCardAtPosition"):
                        int x = command.getX();
                        int y = command.getY();
                        outputNode = objectMapper.createObjectNode();
                        outputNode.put("command", "getCardAtPosition");
                        outputNode.put("x", x);
                        outputNode.put("y", y);
                        if (y < playingTable.get(x).size()) {
                            outputNode.putPOJO("output", playingTable.get(x).get(y));
                        } else {
                            outputNode.put("output", "No card at that position.");
                        }
                        output.addPOJO(outputNode);
                        break;

                    case ("getPlayerMana"):
                        playerIdx = command.getPlayerIdx();
                        outputNode = objectMapper.createObjectNode();
                        outputNode.put("command", "getPlayerMana");
                        outputNode.put("playerIdx", playerIdx);

                        if (playerIdx == 1) {
                            outputNode.put("output", playerOneMana);
                        } else {
                            outputNode.put("output", playerTwoMana);
                        }
                        output.addPOJO(outputNode);
                        break;

                    case ("getEnvironmentCardsInHand"):
                        playerIdx = command.getPlayerIdx();
                        outputNode = objectMapper.createObjectNode();
                        outputNode.put("command", "getEnvironmentCardsInHand");
                        outputNode.put("playerIdx", playerIdx);

                        if (playerIdx == 1) {

                        } else {
                        }
                        break;

                    case ("getFrozenCardsOnTable"):
                        outputNode = objectMapper.createObjectNode();
                        outputNode.put("command", "getFrozenCardsOnTable");
                        break;

                    case ("getTotalGamesPlayed"):
                        outputNode = objectMapper.createObjectNode();
                        outputNode.put("command", "getTotalGamesPlayed");
                        break;

                    case ("getPlayerOneWins"):
                        outputNode = objectMapper.createObjectNode();
                        outputNode.put("command", "getPlayerOneWins");
                        break;

                    case ("endPlayerTurn"):
                        // check if both players played their turns
                        // check if both players played their turns
                        if (turn != newGame.getStartingPlayer()) {
                            // turn ends & add mana
                            playerOneMana += numberOfTurns;
                            playerTwoMana += numberOfTurns;
                            if (playerOneDeck.isEmpty() != false & playerTwoDeck.isEmpty() != false) {
                                playerOneDeckInHand.addLast(playerOneDeck.removeFirst());
                                playerTwoDeckInHand.addLast(playerTwoDeck.removeFirst());
                            }
                            // don't forget the frozen cards
                        }
                        // switch turns
                        if (turn == 1) {
                            turn = 2;
                        } else {
                            turn = 1;
                        }
                        numberOfTurns++;
                        break;

                    case ("placeCard"):
                        handIdx = command.getHandIdx();
                        Cards cardToPlace;
                        String cardName;

                        if (turn == 1 && handIdx < playerOneDeckInHand.size()) {
                            cardToPlace = playerOneDeckInHand.get(handIdx);
                            cardName = cardToPlace.getName();
                        } else if (turn == 2 && handIdx < playerTwoDeckInHand.size()){
                            cardToPlace = playerTwoDeckInHand.get(handIdx);
                            cardName = cardToPlace.getName();
                        } else {
                            break;
                        }
                        if (cardName.equals("Firestorm") || cardName.equals("Winterfell") || cardName.equals("Heart Hound")) {
                            outputNode = objectMapper.createObjectNode();
                            outputNode.put("command", "placeCard");
                            outputNode.put("handIdx", handIdx);
                            outputNode.put("output", "Cannot place environment card on table.");
                            output.addPOJO(outputNode);
                        } else if (playerOneDeckInHand.get(handIdx).getMana() > playerOneMana) {
                            outputNode = objectMapper.createObjectNode();
                            outputNode.put("command", "placeCard");
                            outputNode.put("handIdx", handIdx);
                            outputNode.put("output", "Not enough mana to place card on table.");
                            output.addPOJO(outputNode);
                        } else {
                            if (cardName.equals("The Ripper") || cardName.equals("Miraj") ||
                                    cardName.equals("Goliath") || cardName.equals("Warden")) {
                                if (turn == 1) {
                                    playerOneMana -= cardToPlace.getMana();
                                    playingTable.get(1).add(cardToPlace);
                                } else {
                                    playerTwoMana -= cardToPlace.getMana();
                                    playingTable.get(2).add(cardToPlace);
                                }
                            } else {
                                if (turn == 1) {
                                    playerOneMana -= cardToPlace.getMana();
                                    playingTable.get(0).add(cardToPlace);
                                } else {
                                    playingTable.get(3).add(cardToPlace);
                                    playerTwoMana -= cardToPlace.getMana();
                                }
                            }
                        }

                        break;

                    case ("cardUsesAttack"):
                        cardAttackerX = command.getX();
                        cardAttackerY = command.getY();
                        cardAttackedX = command.getX();
                        cardAttackedY = command.getY();
                        outputNode = objectMapper.createObjectNode();
                        outputNode.put("command", "cardUsesAttack");
                        break;

                    case ("cardUsesAbility"):
                        cardAttackerX = command.getX();
                        cardAttackerY = command.getY();
                        cardAttackedX = command.getX();
                        cardAttackedY = command.getY();
                        outputNode = objectMapper.createObjectNode();
                        outputNode.put("command", "cardUsesAbility");
                        break;

                    case ("useAttackHero"):
                        cardAttackerX = command.getX();
                        cardAttackerY = command.getY();
                        outputNode = objectMapper.createObjectNode();
                        outputNode.put("command", "useAttackHero");
                        break;

                    case ("useHeroAbility"):
                        affectedRow = command.getAffectedRow();
                        outputNode = objectMapper.createObjectNode();
                        outputNode.put("command", "useHeroAbility");
                        break;

                    case ("useEnvironmentCard"):
                        handIdx = command.getHandIdx();
                        affectedRow = command.getAffectedRow();
                        outputNode = objectMapper.createObjectNode();
                        outputNode.put("command", "useEnvironmentCard");
                }
            }


        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }
}
