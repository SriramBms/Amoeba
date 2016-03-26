package com.eo5.amoeba.views;


import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by Sriram on 3/25/2016.
 */
public class FloatingFissionColony extends ViewGroup{
    private Context mContext;

    public FloatingFissionColony(Context context){
        this(context, null, 0);

    }

    public FloatingFissionColony(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public FloatingFissionColony(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        mContext = context;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
