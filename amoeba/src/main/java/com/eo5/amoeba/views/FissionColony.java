package com.eo5.amoeba.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.eo5.amoeba.R;
import com.eo5.amoeba.utils.Constants;

/**
 * Created by Sriram on 3/25/2016.
 */
public class FissionColony extends ViewGroup{
    private Context mContext;
    private boolean mCanReplicate = false;
    private float mButtonDepth;
    private boolean mDoesPulse;
    private int mButtonColor;
    private int mMainButtonIconRes;
    private FloatingActionButton mMainButton;

    //layout params
    private int mMainButtonY;
    private int mMainButtonX;


    public FissionColony(Context context){
        this(context, null, 0);

    }

    public FissionColony(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public FissionColony(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs){
        final TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.FissionButton);
        mCanReplicate = a.getBoolean(R.styleable.FissionButton_fb_enable_fission, false);
        mButtonDepth = a.getDimension(R.styleable.FissionButton_fb_elevation, Constants.DEFAULT_ELEVATION);
        mDoesPulse = a.getBoolean(R.styleable.FissionButton_fb_pulse, Constants.DEFAULT_DOES_PULSE);
        mMainButtonIconRes = a.getInt(R.styleable.FissionButton_fb_icon, Constants.DEFAULT_ICON);
        a.recycle();
        //create main button
        TypedValue v = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.colorAccent, v, true);
        mButtonColor = v.data;
        mMainButton = new FloatingActionButton(mContext);
        //mMainButton.setBackground(getResources().getDrawable(mMainButtonIconRes));
        mMainButton.setImageResource(mMainButtonIconRes);
        mMainButton.setElevation(mButtonDepth);
        mMainButton.setId(R.id.fission_button_main);
        mMainButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        addView(mMainButton, super.generateDefaultLayoutParams());


    }




    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mMainButtonY = b - t - mMainButton.getMeasuredHeight()-(int)getResources().getDimension(R.dimen.button_shadow_width);
        mMainButtonX = r - l - mMainButton.getMeasuredWidth();
        mMainButton.layout(mMainButtonX, mMainButtonY, mMainButtonX+mMainButton.getMeasuredWidth(), mMainButtonY+mMainButton.getMeasuredHeight());

    }

    @Override
    protected  void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        measureChildren(widthMeasureSpec, heightMeasureSpec);


        View mainButton = getChildAt(0);
        int height = mainButton.getMeasuredHeight() + (int)getResources().getDimension(R.dimen.button_spacing) + (int)getResources().getDimension(R.dimen.button_shadow_width);
        int width = mainButton.getMeasuredWidth();

        setMeasuredDimension(width, height);


    }
}
