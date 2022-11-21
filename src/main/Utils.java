package main;

import java.util.ArrayList;
import java.util.LinkedList;

public final class Utils {

    public int getPlayerOneMana() {
        return playerOneMana;
    }

    public void setPlayerOneMana(final int playerOneMana) {
        this.playerOneMana = playerOneMana;
    }

    public int getPlayerTwoMana() {
        return playerTwoMana;
    }

    public void setPlayerTwoMana(final int playerTwoMana) {
        this.playerTwoMana = playerTwoMana;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(final int turn) {
        this.turn = turn;
    }

    public int getPlayerOneWins() {
        return playerOneWins;
    }

    public void setPlayerOneWins(final int playerOneWins) {
        this.playerOneWins = playerOneWins;
    }

    public int getPlayerTwoWins() {
        return playerTwoWins;
    }

    public void setPlayerTwoWins(final int playerTwoWins) {
        this.playerTwoWins = playerTwoWins;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setNumberOfRounds(final int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }


    private int playerOneMana = 1;
    private int playerTwoMana = 1;
    private int turn = 0;
    private int playerOneWins = 0;
    private int playerTwoWins = 0;
    private int numberOfRounds = 0;

    /**
     *
     * @param playingTable
     * @param turn
     * @return
     */
    public static int isTank(final ArrayList<LinkedList<Minion>> playingTable, final int turn) {
        int isTank = 0;
        for (int k = 0; k < playingTable.get(turn).size(); k++) {
            if (playingTable.get(turn).get(k).getName().equals("Goliath")
                    || playingTable.get(turn).get(k).getName().equals("Warden")) {
                isTank = 1;
                // break tank search
                break;
            }
        }
        return isTank;
    }
}
