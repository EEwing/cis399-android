package com.prorl.quent.clickcounter;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.prorl.quent.clickcounter.com.prorl.quent.clickcount.events.CountIncrementHandler;
import com.prorl.quent.clickcounter.com.prorl.quent.clickcount.events.CountResetHandler;

public class ClickCounterMainActivity extends AppCompatActivity {

    Count count;

    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_counter_main);

        TextView txtView = (TextView) findViewById(R.id.count_view);

        settings = getSharedPreferences("TotallyNotCookieClicker", MODE_PRIVATE);

        int def = 0;
        int val = 0;

        def = settings.getInt("default", def);
        val = settings.getInt("count", val);

        count = new Count(val, def, txtView);

        Button inc = (Button) findViewById(R.id.btn_add);
        Button res = (Button) findViewById(R.id.btn_reset);

        inc.setOnClickListener(new CountIncrementHandler(count));
        res.setOnClickListener(new CountResetHandler(count));
    }

    @Override
    public void onResume() {
        super.onResume();

        count.setCount(settings.getInt("count", 0));
        count.setDefault(settings.getInt("default", 0));
    }

    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("count", count.getCount());
        editor.putInt("default", count.getDefault());
        editor.commit();
    }
}
