package com.prorl.quent.piggame;

import com.prorl.quent.piggame.com.prorl.quent.piggame.listeners.PigGameEventListener;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Elliott Ewing on 6/24/2016.
 *
 * Contains a game state and rules to advance game states in the rules of the dice game "Pig"
 *
 */
public class PigGame {

    /**
     * Static references to the two players in the game of "Pig"
     */
    public enum Player {
        ONE,
        TWO;
    }

    /**
     * Static reference to a maximum score. Baked into the compilation of this class
     */
    public static final int MAX_SCORE = 100;

    /**
     * Holds score amounts for each of the players in the Player enum
     */
    private HashMap<Player, Integer> scores;

    /**
     * A random object, used to emulate dice roll
     */
    private Random dice;

    /**
     * The score of the current turn
     */
    private int currentTurnScore;

    /**
     * Reference to which player the current score will apply to
     */
    private Player currentPlayer = Player.ONE;

    /**
     * Callback reference to announce game events as they happen
     */
    private PigGameEventListener events = null;

    public PigGame() {
        scores = new HashMap<Player, Integer>();
        scores.put(Player.ONE, 0);
        scores.put(Player.TWO, 0);

        dice = new Random();
    }

    /**
     * Implements the set of actions that take place to advance the game state to the next turn
     *
     * @return whether the game was won by a player or not
     */
    public boolean nextTurn() {

        scores.put(currentPlayer, scores.get(currentPlayer) + currentTurnScore);
        currentTurnScore = 0;

        if(currentPlayer == Player.TWO && (scores.get(Player.ONE) > MAX_SCORE || scores.get(Player.TWO) > MAX_SCORE)) {
            newGame();
            return true;
        }

        switch (currentPlayer) {
            case ONE:
                currentPlayer = Player.TWO;
                break;
            case TWO:
                currentPlayer = Player.ONE;
                break;
        }
        return false;
    }

    /**
     * Implement the set of actions that need to take place when a player rolls their dice
     *
     * @return the face value of the dice
     */
    public int rollDice() {
        int roll = dice.nextInt(6) + 1;
        if(roll == 1) {
            currentTurnScore = 0;
            nextTurn();
            return 1;
        }
        currentTurnScore += roll;
        return roll;
    }

    /**
     * Starts the game over, and checks if a player won the game.
     */
    public void newGame() {
        declareWinner();
        clearScores();
        currentTurnScore = 0;
        currentPlayer = Player.ONE;
    }

    /**
     * Resets each of the player's scores to 0
     */
    public void clearScores() {
        scores.put(Player.ONE, 0);
        scores.put(Player.TWO, 0);
    }

    /**
     * Returns the player who's turn it currently is
     *
     * @return the playertype who's turn it is
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Retreive the score for the current turn
     *
     * @return the current turn's score
     */
    public int getCurrentTurnScore() {
        return currentTurnScore;
    }

    /**
     * Get the score for the given player type
     *
     * @param player the player type to retrieve the score of
     * @return the score for the given player
     */
    public int getScore(Player player) {
        if(!scores.containsKey(player)) {
            return 0;
        }
        return scores.get(player);
    }

    /**
     * Set the current event listener for this class
     *
     * @param listener the listener to retrieve event functions
     */
    public void setEventListener(PigGameEventListener listener) {
        events = listener;
    }

    /**
     * Set the score for the given player
     *
     * @param player the player to set the score for
     * @param score the score to set the player to
     */
    public void setScore(Player player, int score) {
        scores.put(player, score);
    }

    /**
     * Set the current score for the turn
     *
     * @param score the score to set the current turn at
     */
    public void setCurrentTurnScore(int score) {
        currentTurnScore = score;
    }

    /**
     * Set the player who's turn it current is
     *
     * @param player the player to set as the current turn-taker
     */
    public void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }

    /**
     * Declare a winner for the game, and invoke a callback event in the registered listener
     */
    private void declareWinner() {
        if(events == null) {
            return;
        }
        //NOTE: This scoring rubric means that no ties are given - Player one wins in the case of a tie.
        if(scores.get(Player.ONE) >= scores.get(Player.TWO)) {
            events.onWin(Player.ONE);
        } else {
            events.onWin(Player.TWO);
        }
    }

}
