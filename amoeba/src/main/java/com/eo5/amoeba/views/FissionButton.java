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
 */
public class FissionButton extends FloatingActionButton{

    private boolean mCanReplicate=false;
    private Context mContext;
    private float mButtonDepth;
    private boolean mDoesPulse;
    private View mParent;
    private Canvas mCanvas;
    private AttributeSet mAttrs;

    //View params
    private int mColorDefault;
    private int mColorPressed;


    //child parameters
    private int mChildColor;
    private float mChildElevation;
    private boolean doesPulse;

    //


    public FissionButton(Context context) {
        this(context, null);
    }
    public FissionButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public FissionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mAttrs = attrs;
        init(attrs);

    }

    private void init(AttributeSet attrs){
        final TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.FissionButton);
        mCanReplicate = a.getBoolean(R.styleable.FissionButton_fb_enable_fission, false);
        if(mCanReplicate){
            mButtonDepth = a.getDimension(R.styleable.FissionButton_fb_elevation, Constants.DEFAULT_ELEVATION);
            mDoesPulse = a.getBoolean(R.styleable.FissionButton_fb_pulse, Constants.DEFAULT_DOES_PULSE);
            a.recycle();
            setParameters();
        }


    }

    private void setParameters(){
        this.setElevation(mButtonDepth);
        TypedValue v = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.colorAccent, v, true);
        mChildColor = v.data;

    }



    public void doFission(int type){
        if(!mCanReplicate)
            mCanReplicate=true;
        Log.v(Constants.TAG, "doFission");

        switch (type){
            case Fission.UP:

                break;
            case Fission.DOWN:
                break;
            case Fission.RIGHT:
                break;
            case Fission.LEFT:
                break;
            default:

        }

    }

    @Override
    public void setOnClickListener(OnClickListener clickListener){
        if(mCanReplicate){

        }else{
            super.setOnClickListener(clickListener);
        }


    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
    }


    @Override
    public void setElevation(float elevation){
        super.setElevation(elevation);
        if(mCanReplicate)
            mChildElevation = elevation;
    }

}
