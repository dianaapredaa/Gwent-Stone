//package main;
//
//import fileio.CardInput;
//import fileio.DecksInput;
//
//import java.util.ArrayList;
//
//public class Player {
//
//    public int getNrCardsInDeck() {
//        return nrCardsInDeck;
//    }
//
//    public void setNrCardsInDeck(int nrCardsInDeck) {
//        this.nrCardsInDeck = nrCardsInDeck;
//    }
//
//    public int getNrDecks() {
//        return nrDecks;
//    }
//
//    public void setNrDecks(int nrDecks) {
//        this.nrDecks = nrDecks;
//    }
//
//    public ArrayList<ArrayList<CardInput>> getDecks() {
//        return decks;
//    }
//
//    public void setDecks(ArrayList<ArrayList<CardInput>> decks) {
//        this.decks = decks;
//    }
//
//    private int nrCardsInDeck;
//    private int nrDecks;
//    private ArrayList<ArrayList<CardInput>> decks;
//    public Player() {
//    }
//
//    public Player(DecksInput decksInput) {
//        this.nrCardsInDeck = decksInput.getNrCardsInDeck();
//        this.nrDecks = decksInput.getNrDecks();
//        this.decks = new ArrayList<ArrayList<CardInput>>(decksInput.getDecks());
//    }
//
//}
