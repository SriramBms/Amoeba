package com.eo5.amoeba.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.eo5.amoeba.Fission;
import com.eo5.amoeba.R;
import com.eo5.amoeba.utils.Constants;

/**
 * Created by Sriram on 3/11/2016.
 *
 * WIP: Use FloatingActionButton until this feature has been defined and implemented
 * Used only to enforce restrictions on the fission button
 */
public final class FissionButton extends FloatingActionButton{

    public FissionButton(Context context) {
        this(context, null);
    }
    public FissionButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public FissionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    public void setOnClickListener(OnClickListener listener){
        Log.e(Constants.TAG, "Cannot set a listener for the main fission button. Use a regular FloatingActionButton for this feature");
    }

    protected void setAction(OnClickListener listener){
        super.setOnClickListener(listener);
    }




}
