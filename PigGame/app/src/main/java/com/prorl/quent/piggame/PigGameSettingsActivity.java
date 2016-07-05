package com.prorl.quent.piggame;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class PigGameSettingsActivity extends AppCompatActivity {

    private EditText numSides;
    private EditText maxScore;
    private EditText deadSide;
    private CheckBox aiMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pig_game_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        numSides = (EditText) findViewById(R.id.num_sides);
        maxScore = (EditText) findViewById(R.id.max_pts);
        deadSide = (EditText) findViewById(R.id.dead_side);
        aiMode = (CheckBox) findViewById(R.id.ai_mode);

        findViewById(R.id.btn_submit_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra("numSides", Integer.parseInt(numSides.getText().toString()));
                result.putExtra("maxScore", Integer.parseInt(maxScore.getText().toString()));
                result.putExtra("deadSide", Integer.parseInt(deadSide.getText().toString()));
                result.putExtra("aiMode", aiMode.isChecked());
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });
    }
}
