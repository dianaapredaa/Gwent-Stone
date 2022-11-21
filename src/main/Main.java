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
 * The entry point to this homework. It runs the checker that tests your implementation.
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

        System.out.println("-----------------------------------------");

        int nrOfGames = inputData.getGames().size();
        int playerOneWins = 0;
        int playerTwoWins = 0;
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

            // get Heroes
            Hero playerOneHero = new Hero(newGame.getPlayerOneHero());
            Hero playerTwoHero = new Hero(newGame.getPlayerTwoHero());

            // get first turn
            int turn = newGame.getStartingPlayer();
            int numberOfRounds = 1;

            // iterate through the command list
            for (int j = 0; j < commandList.size(); j++) {
                ActionsInput command = commandList.get(j);
                int cardAttackerX;
                int cardAttackerY;
                int cardAttackedX;
                int cardAttackedY;
                int affectedRow;
                int handIdx;
                ObjectNode outputNode;
                Cards cardToPlace =  null;
                String cardName;
                Minion cardAttacker;
                Minion cardAttacked;

                switch (command.getCommand()) {
                    case ("getCardsInHand"):
                        Debug.getCardsInHand(output, command, playerOneDeckInHand,
                                playerTwoDeckInHand);
                        break;

                    case ("getPlayerDeck"):
                        Debug.getPlayerDeck(output, command, playerOneDeck,
                                playerTwoDeck);
                        break;

                    case ("getCardsOnTable"):
                        Debug.getCardsOnTable(output, playingTable);
                        break;

                    case ("getPlayerTurn"):
                        Debug.getPlayerTurn(output, turn);
                        break;

                    case ("getPlayerHero"):
                        Debug.getPlayerHero(output, command, playerOneHero, playerTwoHero);
                        break;

                    case ("getCardAtPosition"):
                        Debug.getCardsAtPosition(output, command, playingTable);
                        break;

                    case ("getPlayerMana"):
                        Debug.getPlayerMana(output, command, playerOneMana, playerTwoMana);
                        break;

                    case ("getEnvironmentCardsInHand"):
                        Debug.getEnvironmentCardsInHand(output, command,
                                playerOneDeckInHand, playerTwoDeckInHand);
                        break;

                    case ("getFrozenCardsOnTable"):
                        Debug.getFrozenCardsOnTable(output, command, playingTable);
                        break;

                    case ("getTotalGamesPlayed"):
                        Statistics.getTotalGamesPlayed(output, i);
                        break;

                    case ("getPlayerOneWins"):
                        Statistics.getPlayerOneWins(output, playerOneWins);
                        break;

                    case ("getPlayerTwoWins"):
                        Statistics.getPlayerTwoWins(output, playerTwoWins);
                        break;

                    case ("endPlayerTurn"):
                        // check if both players played their turns
                        if (turn != newGame.getStartingPlayer()) {
                            // turn ends & add mana
                            numberOfRounds++;
                            playerOneMana += numberOfRounds;
                            playerTwoMana += numberOfRounds;
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
                                if (turn == 2 && minion.getIsFrozen() == 1 && (minions == playingTable.get(0) || minions == playingTable.get(1))) {
                                    minion.setIsFrozen(0);
                                } else if (turn == 1 && minion.getIsFrozen() == 1 && (minions == playingTable.get(2) || minions == playingTable.get(1))) {
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
                        if (turn == 1) {
                            turn = 2;
                        } else {
                            turn = 1;
                        }
                        break;

                    case ("placeCard"):
//                        Gameplay.placeCard(output, command, turn, playingTable,
//                                playerOneDeckInHand, playerTwoDeckInHand, playerOneMana,
//                                playerTwoMana);
                        handIdx = command.getHandIdx();

                        if (turn == 1) {
                            cardToPlace = playerOneDeckInHand.get(handIdx);
                            cardName = cardToPlace.getName();
                            if (cardName.equals("Firestorm") || cardName.equals("Winterfell")
                                    || cardName.equals("Heart Hound")) {
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
                            } else if (cardName.equals("The Ripper") || cardName.equals("Miraj")
                                    || cardName.equals("Goliath") || cardName.equals("Warden")) {
                                if (playingTable.get(2).size() == 5) {
                                    outputNode = objectMapper.createObjectNode();
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
                                    outputNode = objectMapper.createObjectNode();
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
                            cardToPlace = playerTwoDeckInHand.get(handIdx);
                            cardName = cardToPlace.getName();

                            if (cardName.equals("Firestorm") || cardName.equals("Winterfell")
                                    || cardName.equals("Heart Hound")) {
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
                            } else if (cardName.equals("The Ripper") || cardName.equals("Miraj")
                                    || cardName.equals("Goliath") || cardName.equals("Warden")) {
                                if (playingTable.get(1).size() == 5) {
                                    outputNode = objectMapper.createObjectNode();
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
                                    outputNode = objectMapper.createObjectNode();
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
                        break;

                    case ("cardUsesAttack"):
                        cardAttackerX = command.getCardAttacker().getX();
                        cardAttackerY = command.getCardAttacker().getY();
                        cardAttackedX = command.getCardAttacked().getX();
                        cardAttackedY = command.getCardAttacked().getY();

                        cardAttacked = null;
                        cardAttacker = null;

                        if (playingTable.get(cardAttackerX).size() > cardAttackerY && playingTable.get(cardAttackedX).size() > cardAttackedY) {
                            cardAttacker = playingTable.get(cardAttackerX).get(cardAttackerY);
                            cardAttacked = playingTable.get(cardAttackedX).get(cardAttackedY);
                        } else {
                            System.out.println("Hand Idx Wrong");
                            break;
                        }

                        if ((turn == 1 && (cardAttackedX == 2 || cardAttackedX == 3))
                                || (turn == 2 && (cardAttackedX == 0 || cardAttackedX == 1))) {
                            outputNode = objectMapper.createObjectNode();
                            outputNode.put("command", "cardUsesAttack");
                            outputNode.putPOJO("cardAttacker", command.getCardAttacker());
                            outputNode.putPOJO("cardAttacked", command.getCardAttacked());
                            outputNode.put("error", "Attacked card does not belong to the enemy.");
                            output.addPOJO(outputNode);
                            break;
                        } else if (cardAttacker.getAttackUsed() == 1) {
                            outputNode = objectMapper.createObjectNode();
                            outputNode.put("command", "cardUsesAttack");
                            outputNode.putPOJO("cardAttacker", command.getCardAttacker());
                            outputNode.putPOJO("cardAttacked", command.getCardAttacked());
                            outputNode.put("error", "Attacker card has already attacked this turn.");
                            output.addPOJO(outputNode);
                            break;
                        } else if (cardAttacker.getIsFrozen() == 1) {
                            outputNode = objectMapper.createObjectNode();
                            outputNode.put("command", "cardUsesAttack");
                            outputNode.putPOJO("cardAttacker", command.getCardAttacker());
                            outputNode.putPOJO("cardAttacked", command.getCardAttacked());
                            outputNode.put("error", "Attacker card is frozen.");
                            output.addPOJO(outputNode);
                            break;
                        } else {
                            int isTank = 0;

                            for (int k = 0; k < playingTable.get(turn).size(); k++) {
                                if (playingTable.get(turn).get(k).getName().equals("Goliath")
                                        || playingTable.get(turn).get(k).getName().equals("Warden")) {
                                    isTank = 1;
                                    // break tank search
                                    break;
                                }
                            }

                            if (isTank == 1 && !cardAttacked.getName().equals("Goliath")
                                    && !cardAttacked.getName().equals("Warden")) {
                                outputNode = objectMapper.createObjectNode();
                                outputNode.put("command", "cardUsesAttack");
                                outputNode.putPOJO("cardAttacker", command.getCardAttacker());
                                outputNode.putPOJO("cardAttacked", command.getCardAttacked());
                                outputNode.put("error", "Attacked card is not of type 'Tank'.");
                                output.addPOJO(outputNode);
                                break;
                            } else {
                                // just attack
                                cardAttacker.setAttackUsed(1);
                                if (cardAttacked.getHealth() <= cardAttacker.getAttackDamage()) {
                                    // card dies
                                    playingTable.get(cardAttackedX).remove(cardAttackedY);
                                    break;
                                } else {
                                    cardAttacked.setHealth(cardAttacked.getHealth() - cardAttacker.getAttackDamage());
                                    break;
                                }
                            }
                        }

                    case ("cardUsesAbility"):
                        cardAttackerX = command.getCardAttacker().getX();
                        cardAttackerY = command.getCardAttacker().getY();
                        cardAttackedX = command.getCardAttacked().getX();
                        cardAttackedY = command.getCardAttacked().getY();

                        if (playingTable.get(cardAttackerX).size() > cardAttackerY
                                && playingTable.get(cardAttackedX).size() > cardAttackedY) {
                            cardAttacker = playingTable.get(cardAttackerX).get(cardAttackerY);
                            cardAttacked = playingTable.get(cardAttackedX).get(cardAttackedY);
                        } else {
                            System.out.println("Hand Idx Wrong");
                            break;
                        }

                        if (cardAttacker.getIsFrozen() == 1) {
                            outputNode = objectMapper.createObjectNode();
                            outputNode.put("command", "cardUsesAbility");
                            outputNode.putPOJO("cardAttacker", command.getCardAttacker());
                            outputNode.putPOJO("cardAttacked", command.getCardAttacked());
                            outputNode.put("error", "Attacker card is frozen.");
                            output.addPOJO(outputNode);
                            break;
                        }  else if (cardAttacker.getAttackUsed() == 1) {
                            outputNode = objectMapper.createObjectNode();
                            outputNode.put("command", "cardUsesAbility");
                            outputNode.putPOJO("cardAttacker", command.getCardAttacker());
                            outputNode.putPOJO("cardAttacked", command.getCardAttacked());
                            outputNode.put("error", "Attacker card has already attacked this turn.");
                            output.addPOJO(outputNode);
                            break;
                        } else if (cardAttacker.getName().equals("Disciple")) {
                            if ((turn == 1 && (cardAttackedX == 2 || cardAttackedX == 3))
                                    || (turn == 2 && (cardAttackedX == 0 || cardAttackedX == 1))) {
                                // use ability
                                cardAttacker.setAttackUsed(1);
                                cardAttacked.setHealth(cardAttacked.getHealth() + 2);
                                break;
                            } else {
                                outputNode = objectMapper.createObjectNode();
                                outputNode.put("command", "cardUsesAbility");
                                outputNode.putPOJO("cardAttacker", command.getCardAttacker());
                                outputNode.putPOJO("cardAttacked", command.getCardAttacked());
                                outputNode.put("error", "Attacked card does not belong to the "
                                        + "current player.");
                                output.addPOJO(outputNode);
                                break;
                            }
                        } else if (cardAttacker.getName().equals("The Ripper")
                                || cardAttacker.getName().equals("Miraj")
                                || cardAttacker.getName().equals("The Cursed One")) {
                            if ((turn == 1 && (cardAttackedX == 2 || cardAttackedX == 3))
                                    || (turn == 2 && (cardAttackedX == 0 || cardAttackedX == 1))) {
                                    outputNode = objectMapper.createObjectNode();
                                    outputNode.put("command", "cardUsesAbility");
                                    outputNode.putPOJO("cardAttacker", command.getCardAttacker());
                                    outputNode.putPOJO("cardAttacked", command.getCardAttacked());
                                    outputNode.put("error", "Attacked card does not belong to the "
                                            + "enemy.");
                                    output.addPOJO(outputNode);
                                    break;
                            }

                            int isTank = 0;

                            for (int k = 0; k < playingTable.get(turn).size(); k++)
                                if (playingTable.get(turn).get(k).getName().equals("Goliath")
                                        || playingTable.get(turn).get(k).getName().equals("Warden")) {
                                    isTank = 1;
                                    // break tank search
                                    break;
                                }

                            if (isTank == 1 && !cardAttacked.getName().equals("Goliath")
                                    && !cardAttacked.getName().equals("Warden")) {
                                outputNode = objectMapper.createObjectNode();
                                outputNode.put("command", "cardUsesAbility");
                                outputNode.putPOJO("cardAttacker", command.getCardAttacker());
                                outputNode.putPOJO("cardAttacked", command.getCardAttacked());
                                outputNode.put("error", "Attacked card is not of type 'Tank'.");
                                output.addPOJO(outputNode);
                                break;
                            } else {
                                // just attack
                                cardAttacker.setAttackUsed(1);
                                if (cardAttacker.getName().equals("The Ripper")) {
                                    cardAttacked.setAttackDamage(cardAttacked.getAttackDamage() - 2);
                                    if (cardAttacked.getAttackDamage() < 0) {
                                        cardAttacked.setAttackDamage(0);
                                    }
                                    break;

                                } else if (cardAttacker.getName().equals("Miraj")) {
                                    int healthSwap = cardAttacked.getHealth();
                                    cardAttacked.setHealth(cardAttacker.getHealth());
                                    cardAttacker.setHealth(healthSwap);
                                    break;

                                } else if (cardAttacker.getName().equals("The Cursed One")) {
                                    int healthSwap = cardAttacked.getHealth();
                                    cardAttacked.setHealth(cardAttacked.getAttackDamage());
                                    cardAttacked.setAttackDamage(healthSwap);
                                    if (cardAttacked.getHealth() <= 0) {
                                        playingTable.get(cardAttackedX).remove(cardAttackedY);
                                    }
                                    break;
                                }
                            }
                        }

                        break;

                    case ("useAttackHero"):
                        cardAttackerX = command.getCardAttacker().getX();
                        cardAttackerY = command.getCardAttacker().getY();

                        if (playingTable.get(cardAttackerX).size() > cardAttackerY) {
                            cardAttacker = playingTable.get(cardAttackerX).get(cardAttackerY);
                        } else {
                            break;
                        }

                        if (cardAttacker.getIsFrozen() == 1) {
                            outputNode = objectMapper.createObjectNode();
                            outputNode.put("command", "useAttackHero");
                            outputNode.putPOJO("cardAttacker", command.getCardAttacker());
                            outputNode.put("error", "Attacker card is frozen.");
                            output.addPOJO(outputNode);
                            break;
                        } else if (cardAttacker.getAttackUsed() == 1) {
                            outputNode = objectMapper.createObjectNode();
                            outputNode.put("command", "useAttackHero");
                            outputNode.putPOJO("cardAttacker", command.getCardAttacker());
                            outputNode.put("error", "Attacker card has already attacked this turn.");
                            output.addPOJO(outputNode);
                            break;
                        } else {
                            int isTank = 0;
                            for (int k = 0; k < playingTable.get(turn).size(); k++) {
                                if (playingTable.get(turn).get(k).getName().equals("Goliath")
                                        || playingTable.get(turn).get(k).getName().equals("Warden")) {
                                    isTank = 1;
                                    // break tank search
                                    break;
                                }
                            }

                            if (isTank == 1) {
                                outputNode = objectMapper.createObjectNode();
                                outputNode.put("command", "useAttackHero");
                                outputNode.putPOJO("cardAttacker", command.getCardAttacker());
                                outputNode.put("error", "Attacked card is not of type 'Tank’.");
                                output.addPOJO(outputNode);
                                break;
                            }

                            if (turn == 1) {
                                cardAttacker.setAttackUsed(1);
                                if (playerTwoHero.getHealth() < cardAttacker.getHealth()) {
                                    // player One wins
                                    playerOneWins++;
                                    outputNode = objectMapper.createObjectNode();
                                    outputNode.put("command", "useAttackHero");
                                    outputNode.putPOJO("cardAttacker", command.getCardAttacker());
                                    outputNode.put("gameEnded", "Player one killed the enemy hero.");
                                    output.addPOJO(outputNode);
                                    break;
                                } else {
                                    playerTwoHero.setHealth(playerTwoHero.getHealth() - cardAttacker.getHealth());
                                    break;
                                }
                            } else if (turn == 2) {
                                cardAttacker.setAttackUsed(1);
                                if (playerOneHero.getHealth() < cardAttacker.getHealth()) {
                                    // player One wins
                                        playerTwoWins++;
                                    outputNode = objectMapper.createObjectNode();
                                    outputNode.put("command", "useAttackHero");
                                    outputNode.putPOJO("cardAttacker", command.getCardAttacker());
                                    outputNode.put("gameEnded", "Player two killed the enemy hero.");
                                    output.addPOJO(outputNode);
                                  break;
                                } else {
                                    playerOneHero.setHealth(playerTwoHero.getHealth() - cardAttacker.getHealth());
                                      break;
                                }
                            }
                        }
                        break;

//                    case ("useHeroAbility"):
//                        affectedRow = command.getAffectedRow();
//                        outputNode = objectMapper.createObjectNode();
//                        outputNode.put("command", "useHeroAbility");
//                        output.addPOJO(outputNode);
//
//                        if (turn == 1) {
//                            if (playerOneMana < playerOneHero.getMana()) {
//                                outputNode = objectMapper.createObjectNode();
//                                outputNode.put("command", "useHeroAbility");
//                                outputNode.put("command", affectedRow);
//                                outputNode.put("gameEnded", "Not enough mana to use hero's ability.");
//                                output.addPOJO(outputNode);
//                                break;
//                            }
//                            if (playerOneHero.getAttackUsed() == 1) {
//                                outputNode = objectMapper.createObjectNode();
//                                outputNode.put("command", "useHeroAbility");
//                                outputNode.put("command", affectedRow);
//                                outputNode.put("gameEnded", "Hero has already attacked this turn.");
//                                output.addPOJO(outputNode);
//                                break;
//                            }
//                            if (playerOneHero.getName().equals("Lord Royce")
//                            || playerOneHero.getName().equals("Empress Thorina")) {
//                                if (affectedRow == 2 || affectedRow == 3) {
//                                    outputNode = objectMapper.createObjectNode();
//                                    outputNode.put("command", "useHeroAbility");
//                                    outputNode.put("command", affectedRow);
//                                    outputNode.put("gameEnded", "Selected row does not belong to the enemy.");
//                                    output.addPOJO(outputNode);
//                                    break;
//                                } else {
//                                    // attack
//                                    if (playerOneHero.getName().equals("Lord Royce")) {
//                                        playerOneHero.setAttackUsed(1);
//                                        int maxAttack = 0;
//                                        int maxAttackIdx = 0;
//                                        for (int k = 0; k < playingTable.get(affectedRow).size(); k++) {
//                                            if (playingTable.get(affectedRow).get(k).getAttackDamage() > maxAttack)
//                                                maxAttackIdx = k;
//                                        }
//                                        playingTable.get(affectedRow).get(maxAttackIdx).setIsFrozen(1);
//                                        break;
//                                    }
//                                    if (playerOneHero.getName().equals("Empress Thorina")) {
//                                        playerOneHero.setAttackUsed(1);
//                                        int maxHealth = 0;
//                                        int maxHealthIdx = 0;
//                                        for (int k = 0; k < playingTable.get(affectedRow).size(); k++) {
//                                            if (playingTable.get(affectedRow).get(k).getHealth() > maxHealth) {
//                                                maxHealthIdx = k;
//                                              }
//                                        }
//                                        playingTable.get(affectedRow).remove(maxHealthIdx);
//                                        break;
//                                    }
//
//                                }
//                            }
//                            if (playerOneHero.getName().equals("General Kocioraw")
//                            || playerOneHero.getName().equals("King Mudface")) {
//                                if (affectedRow == 0 || affectedRow == 1) {
//                                    outputNode = objectMapper.createObjectNode();
//                                    outputNode.put("command", "useHeroAbility");
//                                    outputNode.put("command", affectedRow);
//                                    outputNode.put("gameEnded", "Selected row does not belong to the current player.”");
//                                    output.addPOJO(outputNode);
//                                    break;
//                                } else {
//                                    if (playerOneHero.getName().equals("General Kocioraw")) {
//                                        playerOneHero.setAttackUsed(1);
//                                            for (int k = 0; k < playingTable.get(affectedRow).size(); k++) {
//                                                playingTable.get(affectedRow).get(k).setAttackDamage(playingTable.get(affectedRow).get(k).getAttackDamage() + 1);
//                                            }
//                                            break;
//                                        }
//                                    if (playerOneHero.getName().equals("King Mudface")) {
//                                        playerOneHero.setAttackUsed(1);
//                                        for (int k = 0; k < playingTable.get(affectedRow).size(); k++) {
//                                                playingTable.get(affectedRow).get(k).setHealth(playingTable.get(affectedRow).get(k).getHealth() + 1);
//                                        }
//                                        break;
//                                    }
//
//                                }
//                            }
//                        } else if (turn == 2) {
//                            if (playerTwoMana < playerTwoHero.getMana()) {
//                                outputNode = objectMapper.createObjectNode();
//                                outputNode.put("command", "useHeroAbility");
//                                outputNode.put("command", affectedRow);
//                                outputNode.put("gameEnded", "Not enough mana to use hero's ability.");
//                                output.addPOJO(outputNode);
//                                break;
//                            }
//                            if (playerTwoHero.getAttackUsed() == 1) {
//                                outputNode = objectMapper.createObjectNode();
//                                outputNode.put("command", "useHeroAbility");
//                                outputNode.put("command", affectedRow);
//                                outputNode.put("gameEnded", "Hero has already attacked this turn.");
//                                output.addPOJO(outputNode);
//                                break;
//                            }
//                            if (playerOneHero.getName().equals("Lord Royce")
//                            || playerOneHero.getName().equals("Empress Thorina")) {
//                                if (affectedRow == 0 || affectedRow == 1) {
//                                    outputNode = objectMapper.createObjectNode();
//                                    outputNode.put("command", "useHeroAbility");
//                                    outputNode.put("command", affectedRow);
//                                    outputNode.put("gameEnded", "Selected row does not belong to the enemy.");
//                                    output.addPOJO(outputNode);
//                                    break;
//                                } else {
//                                    // attack
//                                    if (playerTwoHero.getName().equals("Lord Royce")) {
//                                        playerTwoHero.setAttackUsed(1);
//                                        int maxAttack = 0;
//                                        int maxAttackIdx = 0;
//                                        for (int k = 0; k < playingTable.get(affectedRow).size(); k++) {
//                                            if (playingTable.get(affectedRow).get(k).getAttackDamage() > maxAttack)
//                                                maxAttackIdx = k;
//                                        }
//                                        playingTable.get(affectedRow).get(maxAttackIdx).setIsFrozen(1);
//                                        break;
//                                    }
//                                    if (playerTwoHero.getName().equals("Empress Thorina")) {
//                                        playerTwoHero.setAttackUsed(1);
//                                        int maxHealth = 0;
//                                        int maxHealthIdx = 0;
//                                        for (int k = 0; k < playingTable.get(affectedRow).size(); k++) {
//                                            if (playingTable.get(affectedRow).get(k).getHealth() > maxHealth) {
//                                                maxHealthIdx = k;
//                                            }
//                                        }
//                                        playingTable.get(affectedRow).remove(maxHealthIdx);
//                                        break;
//                                    }
//
//                                }
//                            }
//                            if (playerTwoHero.getName().equals("General Kocioraw")
//                            || playerOneHero.getName().equals("King Mudface")) {
//                                if (affectedRow == 2 || affectedRow == 3) {
//                                    outputNode = objectMapper.createObjectNode();
//                                    outputNode.put("command", "useHeroAbility");
//                                    outputNode.put("command", affectedRow);
//                                    outputNode.put("gameEnded", "Selected row does not belong to the current player.”");
//                                    output.addPOJO(outputNode);
//                                    break;
//                                } else {
//                                    if (playerTwoHero.getName().equals("General Kocioraw")) {
//                                        playerTwoHero.setAttackUsed(1);
//                                        for (int k = 0; k < playingTable.get(affectedRow).size(); k++) {
//                                            playingTable.get(affectedRow).get(k).setAttackDamage(playingTable.get(affectedRow).get(k).getAttackDamage() + 1);
//                                        }
//                                        break;
//                                    }
//                                    if (playerTwoHero.getName().equals("King Mudface")) {
//                                        playerTwoHero.setAttackUsed(1);
//                                        for (int k = 0; k < playingTable.get(affectedRow).size(); k++) {
//                                            playingTable.get(affectedRow).get(k).setHealth(playingTable.get(affectedRow).get(k).getHealth() + 1);
//                                        }
//                                        break;
//                                    }
//
//                                }
//                            }
//                        }
//                        break;

                    case ("useEnvironmentCard"):
                        handIdx = command.getHandIdx();
                        affectedRow = command.getAffectedRow();

                        if (turn == 1) {
                            if (playerOneDeckInHand.size() > handIdx) {
                                cardToPlace = playerOneDeckInHand.get(handIdx);
                            } else {
                                System.out.println("ERROR");
                                break;
                            }
                        } else if (turn == 2) {
                            if (playerTwoDeckInHand.size() > handIdx) {
                                cardToPlace = playerTwoDeckInHand.get(handIdx);
                            } else {
                                System.out.println("ERROR");
                                break;
                            }
                        }

                        cardName = cardToPlace.getName();
                        if (!cardName.equals("Firestorm") && !cardName.equals("Winterfell")
                                && !cardName.equals("Heart Hound")) {
                            outputNode = objectMapper.createObjectNode();
                            outputNode.put("command", "useEnvironmentCard");
                            outputNode.put("error", "Chosen card is not of type environment.");
                            outputNode.put("handIdx", handIdx);
                            outputNode.put("affectedRow", affectedRow);
                            output.addPOJO(outputNode);
                            break;
                        } else if ((turn == 1 && playerOneDeckInHand.get(handIdx).getMana() > playerOneMana)
                                || (turn == 2 && playerTwoDeckInHand.get(handIdx).getMana() > playerTwoMana)) {
                            outputNode = objectMapper.createObjectNode();
                            outputNode.put("command", "useEnvironmentCard");
                            outputNode.put("error", "Not enough mana to use environment card.");
                            outputNode.put("handIdx", handIdx);
                            outputNode.put("affectedRow", affectedRow);
                            output.addPOJO(outputNode);
                            break;
                        } else if ((turn == 1 && (affectedRow == 2 || affectedRow == 3))
                                || (turn == 2 && (affectedRow == 0 || affectedRow == 1))) {
                            outputNode = objectMapper.createObjectNode();
                            outputNode.put("command", "useEnvironmentCard");
                            outputNode.put("error", "Chosen row does not belong to the enemy.");
                            outputNode.put("handIdx", handIdx);
                            outputNode.put("affectedRow", affectedRow);
                            output.addPOJO(outputNode);
                            break;
                        } else if (cardName.equals("Heart Hound")) {
                            if ((turn == 1 && affectedRow == 1 && playingTable.get(2).size() == 5)
                                    || (turn == 1 && affectedRow == 0 && playingTable.get(3).size() == 5)
                                    || (turn == 2 && affectedRow == 2 && playingTable.get(1).size() == 5)
                                    || (turn == 2 && affectedRow == 3 && playingTable.get(0).size() == 5)) {
                                outputNode = objectMapper.createObjectNode();
                                outputNode.put("command", "useEnvironmentCard");
                                outputNode.put("error", "Cannot steal enemy card since the player's row is full.");
                                outputNode.put("handIdx", handIdx);
                                outputNode.put("affectedRow", affectedRow);
                                output.addPOJO(outputNode);
                                break;
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
                                playerOneMana -= playerOneDeckInHand.get(handIdx).getMana();
                                playerOneDeckInHand.remove(handIdx);
                                break;
                            }
                            if (turn == 1 && affectedRow == 0) {
                                for (int k = 0; k < playingTable.get(0).size(); k++) {
                                    if (playingTable.get(0).get(k).getHealth() > maxHealth) {
                                        maxHealth = playingTable.get(0).get(k).getHealth();
                                        maxHealthIdx = k;
                                    }
                                }
                                playingTable.get(3).add(playingTable.get(0).remove(maxHealthIdx));
                                playerOneMana -= playerOneDeckInHand.get(handIdx).getMana();
                                playerOneDeckInHand.remove(handIdx);
                                break;
                            }
                            if (turn == 2 && affectedRow == 2) {
                                for (int k = 0; k < playingTable.get(2).size(); k++) {
                                    if (playingTable.get(2).get(k).getHealth() > maxHealth) {
                                        maxHealth = playingTable.get(2).get(k).getHealth();
                                        maxHealthIdx = k;
                                    }
                                }
                                playingTable.get(1).add(playingTable.get(2).remove(maxHealthIdx));
                                playerTwoMana -= playerTwoDeckInHand.get(handIdx).getMana();
                                playerTwoDeckInHand.remove(handIdx);
                                break;
                            }
                            if (turn == 2 && affectedRow == 3) {
                                for (int k = 0; k < playingTable.get(3).size(); k++) {
                                    if (playingTable.get(3).get(k).getHealth() > maxHealth) {
                                        maxHealth = playingTable.get(3).get(k).getHealth();
                                        maxHealthIdx = k;
                                    }
                                }
                                playingTable.get(0).add(playingTable.get(3).remove(maxHealthIdx));
                                playerTwoMana -= playerTwoDeckInHand.get(handIdx).getMana();
                                playerTwoDeckInHand.remove(handIdx);
                                break;
                            }
                        } else if (cardName.equals("Firestorm")) {
                            for (int k = 0; k < playingTable.get(affectedRow).size(); k++) {
                                playingTable.get(affectedRow).get(k).setHealth(playingTable.get(affectedRow).get(k).getHealth() - 1);
                                if (playingTable.get(affectedRow).get(k).getHealth() <= 0) {
                                    // card died
                                    playingTable.get(affectedRow).remove(k);
                                    k--;
                                }
                            }
                            if (turn == 1) {
                                playerOneMana -= playerOneDeckInHand.get(handIdx).getMana();
                                playerOneDeckInHand.remove(handIdx);
                            }
                            if (turn == 2) {
                                playerTwoMana -= playerTwoDeckInHand.get(handIdx).getMana();
                                playerTwoDeckInHand.remove(handIdx);
                            }
                            break;
                        } else if (cardName.equals("Winterfell")) {
                            for (int k = 0; k < playingTable.get(affectedRow).size(); k++) {
                                playingTable.get(affectedRow).get(k).setIsFrozen(1);
                                System.out.println("CARD GOT FROZEN");
                            }

                            if (turn == 1) {
                                playerOneMana -= playerOneDeckInHand.get(handIdx).getMana();
                                playerOneDeckInHand.remove(handIdx);
                            } else if (turn == 2) {
                                playerTwoMana -= playerTwoDeckInHand.get(handIdx).getMana();
                                playerTwoDeckInHand.remove(handIdx);
                            }
                            break;
                        }
                }
            }


        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }
}
