package com.prorl.quent.clickcounter.com.prorl.quent.clickcount.events;

import android.view.View;

import com.prorl.quent.clickcounter.Count;

/**
 * Created by Elliott Ewing on 6/23/2016.
 */
public class CountResetHandler implements View.OnClickListener {

    private Count count;

    public CountResetHandler(Count c) {
        count = c;
    }

    @Override
    public void onClick(View view) {
        count.reset();
    }
}
