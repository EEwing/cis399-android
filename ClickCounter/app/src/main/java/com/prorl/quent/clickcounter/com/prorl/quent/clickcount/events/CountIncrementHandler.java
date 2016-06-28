package com.prorl.quent.clickcounter.com.prorl.quent.clickcount.events;

import android.view.View;

import com.prorl.quent.clickcounter.Count;

/**
 * Created by Elliott Ewing on 6/23/2016.
 */
public class CountIncrementHandler implements View.OnClickListener {

    private Count count;

    public CountIncrementHandler(Count c) {
        count = c;
    }
    @Override
    public void onClick(View view) {
        count.increment();
    }
}
