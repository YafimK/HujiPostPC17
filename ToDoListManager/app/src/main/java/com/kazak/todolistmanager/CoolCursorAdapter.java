package com.kazak.todolistmanager;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by fimka on 22/03/2017.
 */

class CoolCursorAdapter extends SimpleCursorAdapter {
    public CoolCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }


    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        if((position % 3) == 0 ){
            view.setBackgroundColor(Color.RED);
        } else if((position % 3) == 1 ){
            view.setBackgroundColor(Color.GREEN);
        } else {
            view.setBackgroundColor(Color.BLUE);
        }

        return view;
    }


}
