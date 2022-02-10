package com.example.artify;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private final ArrayList<Button> mButtons;
    private final int mColumnWidth, mColumnHeight;

    public CustomAdapter(ArrayList<Button> mButtons, int columnWidth, int columnHeight) {
        this.mButtons = mButtons;
        mColumnWidth = columnWidth;
        mColumnHeight = columnHeight;

    }

    @Override
    public int getCount() {
        return mButtons.size();
    }

    @Override
    public Object getItem(int i) {
        return mButtons.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Button button;

        if(view == null) {
            button = mButtons.get(i);
        } else {
            button = (Button) view;
        }

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(mColumnWidth, mColumnHeight);
        button.setLayoutParams(params);

        return button;
    }
}
