// Copyright 2022-2023 Preda Diana 324CA
package main;

import checker.Checker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import checker.CheckerConstants;
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

    public static final int INITIAL_CAPACITY = 4;
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param utilsrgs from command line
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

        Utils utils = new Utils();
        utils.setPlayerOneWins(0);
        utils.setPlayerTwoWins(0);

        for (int i = 0; i < inputData.getGames().size(); i++) {
            utils.setPlayerOneMana(1);
            utils.setPlayerTwoMana(1);

            // current game
            StartGameInput newGame = inputData.getGames().get(i).getStartGame();

            // current game command list
            ArrayList<ActionsInput> commandList = inputData.getGames().get(i).getActions();

            // players selected decks - must be deep copied to use only for this game
            LinkedList<Cards> playerOneDeck = new LinkedList<>();
            LinkedList<Cards> playerTwoDeck = new LinkedList<>();

            // make sure we do not modify the initial cards
            for (Cards card : playerOne.get(newGame.getPlayerOneDeckIdx())) {
                if (card.getName().equals("Winterfell") || card.getName().equals("Firestorm")
                        || card.getName().equals("Heart Hound")) {
                    // environment card
                    playerOneDeck.add(new Environment((Environment) card));
                } else {
                    // minion
                    playerOneDeck.add(new Minion((Minion) card));
                }
            }

            for (Cards card : playerTwo.get(newGame.getPlayerTwoDeckIdx())) {
                if (card.getName().equals("Winterfell") || card.getName().equals("Firestorm")
                        || card.getName().equals("Heart Hound")) {
                    // environment card
                    playerTwoDeck.add(new Environment((Environment) card));
                } else {
                    // minion
                    playerTwoDeck.add(new Minion((Minion) card));
                }
            }

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
            ArrayList<LinkedList<Minion>> playingTable = new ArrayList<>(INITIAL_CAPACITY);

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
            utils.setTurn(newGame.getStartingPlayer());
            utils.setNumberOfRounds(1);

            // iterate through the command list
            for (int j = 0; j < commandList.size(); j++) {
                ActionsInput command = commandList.get(j);
                switch (command.getCommand()) {
                    case ("getCardsInHand") -> Debug.getCardsInHand(output, command,
                            playerOneDeckInHand, playerTwoDeckInHand);
                    case ("getPlayerDeck") -> Debug.getPlayerDeck(output, command,
                            playerOneDeck, playerTwoDeck);
                    case ("getCardsOnTable") -> Debug.getCardsOnTable(output, playingTable);
                    case ("getPlayerTurn") -> Debug.getPlayerTurn(output, utils.getTurn());
                    case ("getPlayerHero") -> Debug.getPlayerHero(output, command, playerOneHero,
                            playerTwoHero);
                    case ("getCardAtPosition") -> Debug.getCardsAtPosition(output, command,
                            playingTable);
                    case ("getPlayerMana") ->
                            Debug.getPlayerMana(output, command, utils.getPlayerOneMana(),
                                    utils.getPlayerTwoMana());
                    case ("getEnvironmentCardsInHand") -> Debug.getEnvironmentCardsInHand(output,
                            command, playerOneDeckInHand, playerTwoDeckInHand);
                    case ("getFrozenCardsOnTable") -> Debug.getFrozenCardsOnTable(output,
                            playingTable);
                    case ("getTotalGamesPlayed") -> Statistics.getTotalGamesPlayed(output, i);
                    case ("getPlayerOneWins") -> Statistics.getPlayerOneWins(output,
                            utils.getPlayerOneWins());
                    case ("getPlayerTwoWins") -> Statistics.getPlayerTwoWins(output,
                            utils.getPlayerTwoWins());
                    case ("endPlayerTurn") -> Gameplay.endPlayerTurn(playerOneDeck, playerTwoDeck,
                            playerOneDeckInHand, playerTwoDeckInHand, playingTable, playerOneHero,
                            playerTwoHero, newGame, utils);
                    case ("placeCard") -> Gameplay.placeCard(output, command, utils.getTurn(),
                            playingTable, playerOneDeckInHand, playerTwoDeckInHand, utils);
                    case ("cardUsesAttack") -> Gameplay.cardUsesAttack(output, command,
                            playingTable, utils.getTurn());
                    case ("cardUsesAbility") -> Gameplay.cardUsesAbility(output, command,
                            playingTable, utils.getTurn());
                    case ("useAttackHero") ->
                            Gameplay.cardAttackHero(output, command, playingTable, utils.getTurn(),
                                    utils, playerOneHero, playerTwoHero);
                    case ("useHeroAbility") ->
                            Gameplay.useHeroAbility(output, command, playingTable, utils.getTurn(),
                                    utils, playerOneHero, playerTwoHero);
                    case ("useEnvironmentCard") ->
                            Gameplay.useEnvironmentCard(output, command, playerOneDeckInHand,
                                    playerTwoDeckInHand, playingTable, utils, utils.getTurn());
                    default -> System.out.println("Nothing to do here");
                }
            }


        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }
}
