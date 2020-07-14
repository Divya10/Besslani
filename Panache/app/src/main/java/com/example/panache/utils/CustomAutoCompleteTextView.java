/*
 * /////////////////////////////////////////////////////////////////////////////////////////////////
 * //
 * //            © Copyright 2019 JangleTech Systems Private Limited, Thane, India
 * //
 * /////////////////////////////////////////////////////////////////////////////////////////////////
 */

package com.example.panache.utils;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

public class CustomAutoCompleteTextView extends AppCompatAutoCompleteTextView {

    private int myThreshold;

    public CustomAutoCompleteTextView(Context context) {
        super(context);
    }

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused && getFilter() != null) {
            performFiltering(getText(), 0);
            showDropDown();
        }
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    public int getThreshold() {
        return myThreshold;
    }

    @Override
    public void setThreshold(int threshold) {
        if (threshold <= 0) {
            threshold = 0;
        }
        myThreshold = threshold;
    }
}
