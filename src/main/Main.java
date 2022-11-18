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
            ArrayList<LinkedList<Minion>> playingTable = new ArrayList<>(4);

            LinkedList<Minion> row0 = new LinkedList<>();
            LinkedList<Minion> row1 = new LinkedList<>();
            LinkedList<Minion> row2 = new LinkedList<>();
            LinkedList<Minion> row3 = new LinkedList<>();

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
            int numberOfRounds = 1;

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
                Cards cardToPlace;
                String cardName;

                switch (command.getCommand()) {
                    case ("getCardsInHand"):
                        playerIdx = command.getPlayerIdx();
                        outputNode = objectMapper.createObjectNode();
                        if (playerIdx == 1) {
                            outputNode.put("command", "getCardsInHand");
                            outputNode.put("playerIdx", command.getPlayerIdx());
                            outputNode.putPOJO("output", new ArrayList<>(playerOneDeckInHand));
                        } else {
                            outputNode.put("command", "getCardsInHand");
                            outputNode.put("playerIdx", command.getPlayerIdx());
                            outputNode.putPOJO("output", new ArrayList<>(playerTwoDeckInHand));
                        }
                        output.addPOJO(outputNode);
                        break;

                    case ("getPlayerDeck"):
                        playerIdx = command.getPlayerIdx();
                        outputNode = objectMapper.createObjectNode();
                        if (playerIdx == 1) {
                            outputNode.put("command", "getPlayerDeck");
                            outputNode.put("playerIdx", command.getPlayerIdx());
                            outputNode.putPOJO("output", new ArrayList<>(playerOneDeck));
                        } else {
                            outputNode.put("command", "getPlayerDeck");
                            outputNode.put("playerIdx", command.getPlayerIdx());
                            outputNode.putPOJO("output", new ArrayList<>(playerTwoDeck));
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
                        if (turn != newGame.getStartingPlayer()) {
                            // turn ends & add mana
                            numberOfRounds++;
                            playerOneMana += numberOfRounds;
                            playerTwoMana += numberOfRounds;
                            if (playerOneDeck.isEmpty() == false) {
                                playerOneDeckInHand.addLast(playerOneDeck.removeFirst());
                            }
                            if (playerTwoDeck.isEmpty() == false) {
                                playerTwoDeckInHand.addLast(playerTwoDeck.removeFirst());
                            }
                            // don't forget the frozen cards
                            for (int k = 0; k < playingTable.size(); k++)
                                for (int h = 0; h < playingTable.get(k).size(); h++) {
                                    if (playingTable.get(k).get(h).getIsFrozen() == 1)
                                        playingTable.get(k).get(h).setIsFrozen(0);
                                    if (playingTable.get(k).get(h).getAttackUsed() == 1)
                                        playingTable.get(k).get(h).setAttackUsed(0);
                                    if (playingTable.get(k).get(h).getAbilityUsed() == 1)
                                        playingTable.get(k).get(h).setAbilityUsed(0);
                                }
                        }
                        // switch turns
                        if (turn == 1) {
                            turn = 2;
                        } else {
                            turn = 1;
                        }
                        break;

                    case ("placeCard"):
                        handIdx = command.getHandIdx();

                        if (turn == 1) {
                            cardToPlace = playerOneDeckInHand.get(handIdx);
                            cardName = cardToPlace.getName();
                            if (cardName.equals("Firestorm") || cardName.equals("Winterfell") || cardName.equals("Heart Hound")) {
                                outputNode = objectMapper.createObjectNode();
                                outputNode.put("command", "placeCard");
                                outputNode.put("error", "Cannot place environment card on table.");
                                outputNode.put("handIdx", handIdx);
                                output.addPOJO(outputNode);
                            } else if (playerOneDeckInHand.get(handIdx).getMana() > playerOneMana) {
                                outputNode = objectMapper.createObjectNode();
                                outputNode.put("command", "placeCard");
                                outputNode.put("error", "Not enough mana to place card on table.");
                                outputNode.put("handIdx", handIdx);
                                output.addPOJO(outputNode);
                            } else if (cardName.equals("The Ripper") || cardName.equals("Miraj") ||
                                        cardName.equals("Goliath") || cardName.equals("Warden")) {
                                if (playingTable.get(2).size() == 5) {
                                    outputNode = objectMapper.createObjectNode();
                                    outputNode.put("command", "placeCard");
                                    outputNode.put("error", "Cannot place card on table since row is full.");
                                    outputNode.put("handIdx", handIdx);
                                    output.addPOJO(outputNode);
                                } else {
                                    playerOneMana -= cardToPlace.getMana();
                                    playingTable.get(2).add((Minion)playerOneDeckInHand.remove(handIdx));
                                }
                            } else {
                                if (playingTable.get(3).size() == 5) {
                                    outputNode = objectMapper.createObjectNode();
                                    outputNode.put("command", "placeCard");
                                    outputNode.put("error", "Cannot place card on table since row is full.");
                                    outputNode.put("handIdx", handIdx);
                                    output.addPOJO(outputNode);
                                } else {
                                    playerOneMana -= cardToPlace.getMana();
                                    playingTable.get(3).add((Minion)playerOneDeckInHand.remove(handIdx));
                                }
                            }
                        } else if (turn == 2) {
                            cardToPlace = playerTwoDeckInHand.get(handIdx);
                            cardName = cardToPlace.getName();

                            if (cardName.equals("Firestorm") || cardName.equals("Winterfell") || cardName.equals("Heart Hound")) {
                                outputNode = objectMapper.createObjectNode();
                                outputNode.put("command", "placeCard");
                                outputNode.put("error", "Cannot place environment card on table.");
                                outputNode.put("handIdx", handIdx);
                                output.addPOJO(outputNode);
                            } else if (playerTwoDeckInHand.get(handIdx).getMana() > playerTwoMana) {
                                outputNode = objectMapper.createObjectNode();
                                outputNode.put("command", "placeCard");
                                outputNode.put("error", "Not enough mana to place card on table.");
                                outputNode.put("handIdx", handIdx);
                                output.addPOJO(outputNode);
                            } else if (cardName.equals("The Ripper") || cardName.equals("Miraj") ||
                                        cardName.equals("Goliath") || cardName.equals("Warden")) {
                                if (playingTable.get(1).size() == 5) {
                                    outputNode = objectMapper.createObjectNode();
                                    outputNode.put("command", "placeCard");
                                    outputNode.put("error", "Cannot place card on table since row is full.");
                                    outputNode.put("handIdx", handIdx);
                                    output.addPOJO(outputNode);
                                } else {
                                    playerTwoMana -= cardToPlace.getMana();
                                    playingTable.get(1).add((Minion)playerTwoDeckInHand.remove(handIdx));
                                }
                            } else {
                                if (playingTable.get(0).size() == 5) {
                                    outputNode = objectMapper.createObjectNode();
                                    outputNode.put("command", "placeCard");
                                    outputNode.put("error", "Cannot place card on table since row is full.");
                                    outputNode.put("handIdx", handIdx);
                                    output.addPOJO(outputNode);
                                } else {
                                    playerTwoMana -= cardToPlace.getMana();
                                    playingTable.get(0).add((Minion)playerTwoDeckInHand.remove(handIdx));
                                }
                            }
                        }
                        break;

                    case ("cardUsesAttack"):
                        cardAttackerX = command.getX();
                        cardAttackerY = command.getY();
                        cardAttackedX = command.getX();
                        cardAttackedY = command.getY();
                        Minion cardAttacker = playingTable.get(cardAttackerX).get(cardAttackerY);
                        Minion cardAttacked = playingTable.get(cardAttackedX).get(cardAttackedY);

                        if (turn == 1) {
                            if (cardAttackedX == 2 || cardAttackedX == 3) {
                                outputNode = objectMapper.createObjectNode();
                                outputNode.put("command", "cardUsesAttack");
                                outputNode.put("error", "Attacked card does not belong to the enemy.");
                                output.addPOJO(outputNode);
                                break;
                            }
                        } else if (turn == 2) {
                            if (cardAttackedX == 0 || cardAttackedX == 1) {
                                outputNode = objectMapper.createObjectNode();
                                outputNode.put("command", "cardUsesAttack");
                                outputNode.put("error", "Attacked card does not belong to the enemy.");
                                output.addPOJO(outputNode);
                                break;
                            }
                        }
                        if (cardAttacker.getAttackUsed() == 1 || cardAttacker.getAbilityUsed() == 1) {
                            outputNode = objectMapper.createObjectNode();
                            outputNode.put("command", "cardUsesAttack");
                            outputNode.put("error", "Attacker card has already attacked this turn.");
                            output.addPOJO(outputNode);
                            break;
                        }  else if (playingTable.get(cardAttackerX).get(cardAttackerY).getIsFrozen() == 1) {
                            outputNode = objectMapper.createObjectNode();
                            outputNode.put("command", "cardUsesAttack");
                            outputNode.put("error", "Attacker card is frozen.");
                            output.addPOJO(outputNode);
                            break;
                        } else {
                            // Goliath and Warden are 'Tank' cards
                            if (cardAttacker.getName().equals("Disciple")) {
                                // just attack
                                cardAttacker.setAttackUsed(1);
                                if (cardAttacked.getHealth() < cardAttacker.getHealth()) {
                                    // card dies
                                    playingTable.get(cardAttackedX).remove(cardAttackedY);
                                } else {
                                    cardAttacked.setHealth(cardAttacked.getHealth() - cardAttacker.getHealth());
                                }
                            } else {
                                if (cardAttacked.getName().equals("Goliath") || cardAttacked.getName().equals("Warden")) {
                                    // just attack
                                    cardAttacker.setAttackUsed(1);
                                    if (cardAttacked.getHealth() < cardAttacker.getHealth()) {
                                        // card dies
                                        playingTable.get(cardAttackedX).remove(cardAttackedY);
                                    } else {
                                        cardAttacked.setHealth(cardAttacked.getHealth() - cardAttacker.getHealth());
                                    }
                                } else {
                                    outputNode = objectMapper.createObjectNode();
                                    outputNode.put("command", "cardUsesAttack");
                                    outputNode.put("error", "Attacked card is not of type 'Tank’.");
                                    output.addPOJO(outputNode);
                                    break;
                                }
                            }
                        }
                        break;

                    case ("cardUsesAbility"):
                        cardAttackerX = command.getX();
                        cardAttackerY = command.getY();
                        cardAttackedX = command.getX();
                        cardAttackedY = command.getY();

                        Minion cardAttacker = playingTable.get(cardAttackerX).get(cardAttackerY);
                        Minion cardAttacked = playingTable.get(cardAttackedX).get(cardAttackedY);

                        if (cardAttacker.getIsFrozen() == 1) {
                            outputNode = objectMapper.createObjectNode();
                            outputNode.put("command", "cardUsesAttack");
                            outputNode.put("error", "Attacker card is frozen.");
                            output.addPOJO(outputNode);
                            break;
                        }  else if (cardAttacker.getAttackUsed() == 1 || cardAttacker.getAbilityUsed() == 1) {
                            outputNode = objectMapper.createObjectNode();
                            outputNode.put("command", "cardUsesAttack");
                            outputNode.put("error", "Attacker card has already attacked this turn.");
                            output.addPOJO(outputNode);
                            break;
                        } else if (cardAttacker.getName().equals("Disciple")) {
                            if (turn == 1) {
                                if (cardAttackedX == 2 || cardAttackedX == 3) {
                                    // use ability
                                    cardAttacker.setAbilityUsed(1);
                                    cardAttacked.setHealth(cardAttacked.getHealth() + 2);
                                    break;

                                } else  {
                                    outputNode = objectMapper.createObjectNode();
                                    outputNode.put("command", "cardUsesAttack");
                                    outputNode.put("error", "Attacked card does not belong to the current player.");
                                    output.addPOJO(outputNode);
                                    break;
                                }
                            } else if (turn == 2) {
                                if (cardAttackedX == 0 || cardAttackedX == 1) {
                                    // use ability
                                    cardAttacker.setAbilityUsed(1);
                                    cardAttacked.setHealth(cardAttacked.getHealth() + 2);
                                    break;
                                } else  {
                                    outputNode = objectMapper.createObjectNode();
                                    outputNode.put("command", "cardUsesAttack");
                                    outputNode.put("error", "Attacked card does not belong to the current player.");
                                    output.addPOJO(outputNode);
                                    break;
                                }
                            }
                        } else if (cardAttacker.getName().equals("The Ripper") || cardAttacker.getName().equals("Miraj")
                                || cardAttacker.getName().equals("The Cursed One")) {
                            if (turn == 1) {
                                if (cardAttackedX == 2 || cardAttackedX == 3) {
                                    outputNode = objectMapper.createObjectNode();
                                    outputNode.put("command", "cardUsesAttack");
                                    outputNode.put("error", "Attacked card does not belong to the enemy.");
                                    output.addPOJO(outputNode);
                                    break;
                                }
                            } else if (turn == 2) {
                                if (cardAttackedX == 0 || cardAttackedX == 1) {
                                    outputNode = objectMapper.createObjectNode();
                                    outputNode.put("command", "cardUsesAttack");
                                    outputNode.put("error", "Attacked card does not belong to the enemy.");
                                    output.addPOJO(outputNode);
                                    break;
                                }
                            }
                            if (cardAttacked.getName().equals("Goliath") || cardAttacked.getName().equals("Warden")) {
                                // just attack
                                cardAttacker.setAbilityUsed(1);
                                if (cardAttacker.getName().equals("The Ripper")) {
                                    cardAttacked.setHealth(cardAttacked.getHealth() - 2);
                                    if (cardAttacked.getHealth() < 0) {
                                        playingTable.get(cardAttackedX).remove(cardAttackedY);
                                    }
                                    break;

                                } else if (cardAttacker.getName().equals("Miraj")) {
                                    int healtSwap = cardAttacked.getHealth();
                                    cardAttacked.setHealth(cardAttacker.getHealth());
                                    cardAttacker.setHealth(healtSwap);
                                    break;

                                } else if (cardAttacker.getName().equals("The Cursed One")) {
                                    int healthSwap = cardAttacked.getHealth();
                                    cardAttacked.setHealth(cardAttacked.getAttackDamage());
                                    cardAttacked.setAttackDamage(healthSwap);
                                    break;
                                }

                            } else {
                                outputNode = objectMapper.createObjectNode();
                                outputNode.put("command", "cardUsesAttack");
                                outputNode.put("error", "Attacked card is not of type 'Tank’.");
                                output.addPOJO(outputNode);
                                break;
                            }
                        }

                        break;

                    case ("useAttackHero"):
                        cardAttackerX = command.getX();
                        cardAttackerY = command.getY();

                        outputNode = objectMapper.createObjectNode();
                        outputNode.put("command", "useAttackHero");
                        outputNode.put("error", "Attacked card is not of type 'Tank’.");
                        output.addPOJO(outputNode);

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

                        if (turn == 1) {
                            cardToPlace = playerOneDeckInHand.get(handIdx);
                            cardName = cardToPlace.getName();
                            if (!cardName.equals("Firestorm") || !cardName.equals("Winterfell") || !cardName.equals("Heart Hound")) {
                                outputNode = objectMapper.createObjectNode();
                                outputNode.put("command", "useEnvironmentCard");
                                outputNode.put("error", "Chosen card is not of type environment.");
                                outputNode.put("handIdx", handIdx);
                                outputNode.put("affectedRow", affectedRow);
                                output.addPOJO(outputNode);
                            } else if (playerOneDeckInHand.get(handIdx).getMana() > playerOneMana) {
                                outputNode = objectMapper.createObjectNode();
                                outputNode.put("command", "useEnvironmentCard");
                                outputNode.put("error", "Not enough mana to place card on table.");
                                outputNode.put("handIdx", handIdx);
                                outputNode.put("affectedRow", affectedRow);
                                output.addPOJO(outputNode);
                            } else if (affectedRow == 2 || affectedRow == 3) {
                                outputNode = objectMapper.createObjectNode();
                                outputNode.put("command", "useEnvironmentCard");
                                outputNode.put("error", "Chosen row does not belong to the enemy.");
                                outputNode.put("handIdx", handIdx);
                                outputNode.put("affectedRow", affectedRow);
                                output.addPOJO(outputNode);
                            } else if (cardName.equals("Heart Hound")) {
                                if (affectedRow == 1) {
                                    if (playingTable.get(2).size() == 5) {
                                        outputNode = objectMapper.createObjectNode();
                                        outputNode.put("command", "useEnvironmentCard");
                                        outputNode.put("error", "Cannot steal enemy card since the player's row is full.");
                                        outputNode.put("handIdx", handIdx);
                                        outputNode.put("affectedRow", affectedRow);
                                        output.addPOJO(outputNode);
                                    } else {
                                        int maxHealth = 0;
                                        int maxHealthIdx = 0;
                                        for (int k = 0; k < playingTable.get(1).size(); k++) {
                                            if (playingTable.get(1).get(k).getHealth() > maxHealth) {
                                                maxHealth = playingTable.get(1).get(k).getHealth();
                                                maxHealthIdx = k;
                                            }
                                        }
                                        playingTable.get(2).add(playingTable.get(1).remove(maxHealthIdx));
                                    }
                                }
                                if (affectedRow == 0) {
                                    if (playingTable.get(3).size() == 5) {
                                        outputNode = objectMapper.createObjectNode();
                                        outputNode.put("command", "useEnvironmentCard");
                                        outputNode.put("error", "Cannot steal enemy card since the player's row is full.");
                                        outputNode.put("handIdx", handIdx);
                                        outputNode.put("affectedRow", affectedRow);
                                        output.addPOJO(outputNode);
                                    } else {
                                        int maxHealth = 0;
                                        int maxHealthIdx = 0;
                                        for (int k = 0; k < playingTable.get(0).size(); k++) {
                                            if (playingTable.get(0).get(k).getHealth() > maxHealth) {
                                                maxHealth = playingTable.get(0).get(k).getHealth();
                                                maxHealthIdx = k;
                                            }
                                        }
                                        playingTable.get(3).add(playingTable.get(0).remove(maxHealthIdx));
                                    }

                                }

                            } else if (cardName.equals("Firestorm")) {
                                for (int k = 0; k < playingTable.get(affectedRow).size(); k++) {
                                    playingTable.get(affectedRow).get(affectedRow).setHealth(playingTable.get(2).get(k).getHealth() - 1);
                                }
                            } else if (cardName.equals("Winterfell")) {
                                for (int k = 0; k < playingTable.get(affectedRow).size(); k++) {
                                    playingTable.get(affectedRow).get(k).setIsFrozen(1);
                                }
                            }
                        } else if (turn == 2) {
                            cardToPlace = playerTwoDeckInHand.get(handIdx);
                            cardName = cardToPlace.getName();
                            if (!cardName.equals("Firestorm") || !cardName.equals("Winterfell") || !cardName.equals("Heart Hound")) {
                                outputNode = objectMapper.createObjectNode();
                                outputNode.put("command", "useEnvironmentCard");
                                outputNode.put("error", "Chosen card is not of type environment.");
                                outputNode.put("handIdx", handIdx);
                                outputNode.put("affectedRow", affectedRow);
                                output.addPOJO(outputNode);
                            } else if (playerTwoDeckInHand.get(handIdx).getMana() > playerTwoMana) {
                                outputNode = objectMapper.createObjectNode();
                                outputNode.put("command", "useEnvironmentCard");
                                outputNode.put("error", "Not enough mana to place card on table.");
                                outputNode.put("handIdx", handIdx);
                                outputNode.put("affectedRow", affectedRow);
                                output.addPOJO(outputNode);
                            } else if (affectedRow == 0 || affectedRow == 1) {
                                outputNode = objectMapper.createObjectNode();
                                outputNode.put("command", "useEnvironmentCard");
                                outputNode.put("error", "Chosen row does not belong to the enemy.");
                                outputNode.put("handIdx", handIdx);
                                outputNode.put("affectedRow", affectedRow);
                                output.addPOJO(outputNode);
                            } else if (cardName.equals("Heart Hound")) {
                                if (affectedRow == 2) {
                                    if (playingTable.get(1).size() == 5) {
                                        outputNode = objectMapper.createObjectNode();
                                        outputNode.put("command", "useEnvironmentCard");
                                        outputNode.put("error", "Cannot steal enemy card since the player's row is full.");
                                        outputNode.put("handIdx", handIdx);
                                        outputNode.put("affectedRow", affectedRow);
                                        output.addPOJO(outputNode);
                                    } else {
                                        int maxHealth = 0;
                                        int maxHealthIdx = 0;
                                        for (int k = 0; k < playingTable.get(2).size(); k++) {
                                            if (playingTable.get(2).get(k).getHealth() > maxHealth) {
                                                maxHealth = playingTable.get(2).get(k).getHealth();
                                                maxHealthIdx = k;
                                            }
                                        }
                                        playingTable.get(1).add(playingTable.get(2).remove(maxHealthIdx));
                                    }
                                }
                                if (affectedRow == 3) {
                                    if (playingTable.get(0).size() == 5) {
                                        outputNode = objectMapper.createObjectNode();
                                        outputNode.put("command", "useEnvironmentCard");
                                        outputNode.put("error", "Cannot steal enemy card since the player's row is full.");
                                        outputNode.put("handIdx", handIdx);
                                        outputNode.put("affectedRow", affectedRow);
                                        output.addPOJO(outputNode);
                                    } else {
                                        int maxHealth = 0;
                                        int maxHealthIdx = 0;
                                        for (int k = 0; k < playingTable.get(3).size(); k++) {
                                            if (playingTable.get(3).get(k).getHealth() > maxHealth) {
                                                maxHealth = playingTable.get(3).get(k).getHealth();
                                                maxHealthIdx = k;
                                            }
                                        }
                                        playingTable.get(0).add(playingTable.get(3).remove(maxHealthIdx));
                                    }

                                }

                            } else if (cardName.equals("Firestorm")) {
                                for (int k = 0; k < playingTable.get(affectedRow).size(); k++) {
                                    playingTable.get(affectedRow).get(k).setHealth(playingTable.get(affectedRow).get(k).getHealth() - 1);
                                }
                            } else if (cardName.equals("Winterfell")) {
                                for (int k = 0; k < playingTable.get(affectedRow).size(); k++) {
                                    playingTable.get(affectedRow).get(k).setIsFrozen(1);
                                }
                            }
                        }

                }
            }


        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }
}
