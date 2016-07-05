package com.prorl.quent.piggame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.prorl.quent.piggame.com.prorl.quent.piggame.listeners.PigGameEventListener;

public class PigGameMainActivity extends AppCompatActivity implements PigGameEventListener {

    /*
    Reference to the structure of the game
    Provides game rule implementation
     */
    final PigGame game = new PigGame();

    private static final int SETTINGS_RESULT = 1;

    /*
    Editable UI elements
     */
    private EditText player1Name;
    private EditText player2Name;

    /*
    Display UI elements
     */
    private TextView playerDisplay;
    private TextView player1Score;
    private TextView player2Score;
    private TextView curTurnScore;
    private ImageView diceDisplay;

    /*
    Interactable UI elements
     */
    private Button btnRollDice;
    private Button btnNextTurn;
    private Button btnNewGame;

    /*
    Reference to data storage and retreival object
     */
    private SharedPreferences savedData;

    /*
    UI reference to remember last roll to display dice
     */
    private int lastRoll = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pig_game_main);

        // Register onWin function for our PigGame reference
        game.setEventListener(this);

        // Load data object from filesystem
        savedData = getSharedPreferences("OinkOinkData", MODE_PRIVATE);

        // Get references to UI elements
        diceDisplay = (ImageView) findViewById(R.id.img_cur_turn);
        playerDisplay = (TextView) findViewById(R.id.player_turn_view);
        player1Name = (EditText) findViewById(R.id.player_name1);
        player2Name = (EditText) findViewById(R.id.player_name2);
        player1Score = (TextView) findViewById(R.id.score_view1);
        player2Score = (TextView) findViewById(R.id.score_view2);
        curTurnScore = (TextView) findViewById(R.id.current_points_view);
        btnRollDice = (Button) findViewById(R.id.btn_roll);
        btnNextTurn = (Button) findViewById(R.id.btn_end_turn);
        btnNewGame = (Button) findViewById(R.id.btn_new_game);

        // Set default dice image
        diceDisplay.setImageResource(R.drawable.die1);

        // Register button click callback functions
        btnRollDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastRoll = game.rollDice();
                updateDisplay();
            }
        });

        btnNextTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean win = game.nextTurn();
                if(!win) {
                    updateDisplay();
                }
            }
        });

        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.newGame();
                player1Name.setText("");
                player2Name.setText("");
                updateDisplay();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_about:
                Toast.makeText(this.getApplication(), "PigGame app made by Elliott Ewing for CIS 399 - Android App Development, taught by Brian Bird", Toast.LENGTH_LONG
                ).show();
                return true;
            case R.id.menu_settings:
                Intent intent = new Intent(this, PigGameSettingsActivity.class);
                startActivityForResult(intent, SETTINGS_RESULT);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        switch(req) {
            case SETTINGS_RESULT:
                if(res == Activity.RESULT_OK) {
                    int numSides = data.getIntExtra("numSides", 6);
                    int maxScore = data.getIntExtra("maxScore", 100);
                    int deadSide = data.getIntExtra("deadSide", 1);
                    boolean aiMode = data.getBooleanExtra("aiMode", false);
                    game.setAiMode(aiMode);
                    game.setDeadSide(deadSide);
                    game.setDieSize(numSides);
                    game.setMaxScore(maxScore);
                    saveData();
                    //updateDisplay();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
        // Update display with PigGame data from data storage object
        updateDisplay();
    }

    /**
     * Updates all UI elements to reflect PigGame game state
     */
    private void updateDisplay() {
        player1Score.setText(game.getScore(PigGame.Player.ONE) + "");
        player2Score.setText(game.getScore(PigGame.Player.TWO) + "");

        curTurnScore.setText(game.getCurrentTurnScore() + "");

        // Grab player's names based on the current player
        String playerText = "";
        switch (game.getCurrentPlayer()) {
            case ONE:
                playerText = player1Name.getText() + "'s turn!";
                break;
            case TWO:
                playerText = player2Name.getText() + "'s turn!";
                break;
        }
        playerDisplay.setText(playerText);

        // Choose appropriate dice icon based on last roll
        int diceID = -1;
        switch(lastRoll) {
            case 1:
                diceID = R.drawable.die1;
                break;
            case 2:
                diceID = R.drawable.die2;
                break;
            case 3:
                diceID = R.drawable.die3;
                break;
            case 4:
                diceID = R.drawable.die4;
                break;
            case 5:
                diceID = R.drawable.die5;
                break;
            case 6:
                diceID = R.drawable.die6;
                break;
            default:
                break;
        }
        // Display dice only if lastRoll actually exists
        if(diceID != -1) {
            diceDisplay.setImageResource(diceID);
        }

        if(game.getAIMode()) {
            player2Name.setEnabled(false);
            player2Name.setText("Computer");
        } else {
            player2Name.setEnabled(true);
        }

    }

    @Override
    public void onWin(PigGame.Player winner) {
        // Update player text based on the winner of the game
        String winnerName = "";
        switch(winner) {
            case ONE:
                winnerName = player1Name.getText().toString();
                break;
            case TWO:
                winnerName = player2Name.getText().toString();
                break;
        }
        playerDisplay.setText(winnerName + " Wins!");
    }

    private void saveData() {
        // Write data to data storage object
        SharedPreferences.Editor editor = savedData.edit();
        editor.putInt("p1Score", game.getScore(PigGame.Player.ONE));
        editor.putInt("p2Score", game.getScore(PigGame.Player.TWO));

        editor.putString("p1Name", player1Name.getText().toString());
        editor.putString("p2Name", player2Name.getText().toString());

        editor.putInt("maxScore", game.getMaxScore());
        editor.putInt("numSides", game.getDieSize());
        editor.putInt("deadSide", game.getDeadSide());
        editor.putBoolean("aiMode", game.getAIMode());

        int curPlayer = -1;
        switch(game.getCurrentPlayer()) {
            case ONE:
                curPlayer = 0;
                break;
            case TWO:
                curPlayer = 1;
                break;
        }
        editor.putInt("curTurn", curPlayer);
        editor.putInt("curTurnScore", game.getCurrentTurnScore());
        editor.putInt("lastRoll", lastRoll);

        // Finialize data storage
        editor.commit();
    }

    private void loadData() {
        // Retrieve stored data
        player1Name.setText(savedData.getString("p1Name", ""));
        player2Name.setText(savedData.getString("p2Name", ""));

        // Set up game state from data storage object
        game.setScore(PigGame.Player.ONE, savedData.getInt("p1Score", 0));
        game.setScore(PigGame.Player.TWO, savedData.getInt("p2Score", 0));
        game.setAiMode(savedData.getBoolean("aiMode", false));
        game.setDeadSide(savedData.getInt("deadSide", 1));
        game.setDieSize(savedData.getInt("numSides", 6));
        game.setMaxScore(savedData.getInt("maxScore", 100));

        int curPlayer = savedData.getInt("curTurn", 0);
        switch(curPlayer) {
            case 0:
                game.setCurrentPlayer(PigGame.Player.ONE);
                break;
            case 1:
                game.setCurrentPlayer(PigGame.Player.TWO);
                break;
        }
        game.setCurrentTurnScore(savedData.getInt("curTurnScore", 0));
        lastRoll = savedData.getInt("lastRoll", 0);

    }
}
