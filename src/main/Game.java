//package main;
//
//import fileio.ActionsInput;
//import fileio.CardInput;
//import fileio.GameInput;
//import fileio.StartGameInput;
//
//import java.util.ArrayList;
//
//public class Game {
//    public int getPlayerOneDeckIdx() {
//        return playerOneDeckIdx;
//    }
//
//    public void setPlayerOneDeckIdx(int playerOneDeckIdx) {
//        this.playerOneDeckIdx = playerOneDeckIdx;
//    }
//
//    public int getPlayerTwoDeckIdx() {
//        return playerTwoDeckIdx;
//    }
//
//    public void setPlayerTwoDeckIdx(int playerTwoDeckIdx) {
//        this.playerTwoDeckIdx = playerTwoDeckIdx;
//    }
//
//    public int getShuffleSeed() {
//        return shuffleSeed;
//    }
//
//    public void setShuffleSeed(int shuffleSeed) {
//        this.shuffleSeed = shuffleSeed;
//    }
//
//    public CardInput getPlayerOneHero() {
//        return playerOneHero;
//    }
//
//    public void setPlayerOneHero(CardInput playerOneHero) {
//        this.playerOneHero = playerOneHero;
//    }
//
//    public CardInput getPlayerTwoHero() {
//        return playerTwoHero;
//    }
//
//    public void setPlayerTwoHero(CardInput playerTwoHero) {
//        this.playerTwoHero = playerTwoHero;
//    }
//
//    public int getStartingPlayer() {
//        return startingPlayer;
//    }
//
//    public void setStartingPlayer(int startingPlayer) {
//        this.startingPlayer = startingPlayer;
//    }
//
//    public ArrayList<ActionsInput> getActions() {
//        return actions;
//    }
//
//    public void setActions(ArrayList<ActionsInput> actions) {
//        this.actions = actions;
//    }
//
//    private int playerOneDeckIdx;
//    private int playerTwoDeckIdx;
//    private int shuffleSeed;
//    private CardInput playerOneHero;
//    private CardInput playerTwoHero;
//    private int startingPlayer;
//    private ArrayList<ActionsInput> actions;
//
//    public Game() {
//
//    }
//
//    public Game(StartGameInput startGameInput, GameInput gameInput) {
//        this.playerOneDeckIdx = startGameInput.getPlayerOneDeckIdx();
//        this.playerTwoDeckIdx = startGameInput.getPlayerTwoDeckIdx();
//        this.shuffleSeed = startGameInput.getShuffleSeed();
//        this.playerOneHero = startGameInput.getPlayerOneHero();
//        this.playerTwoHero = startGameInput.getPlayerTwoHero();
//        this.startingPlayer = startGameInput.getStartingPlayer();
//        this.actions = gameInput.getActions();
//    }
//
//}
