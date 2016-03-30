package com.eo5.amoeba.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

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
    private FissionButton mMainButton;
    private FloatingActionButton mTestButton;
    private boolean isExpanded=false;

    //layout params
    private int mMainButtonY;
    private int mMainButtonX;
    private int mMainButtonWidth;
    private int mMainButtonHeight;
    private int mShadowRadiusDelta;
    private int mButtonSpacing;


    //animation params
    private Animation mExpandAnim, mCollapseAnim;


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
        setAnimationParams();
        final TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.FissionButton);
        mCanReplicate = a.getBoolean(R.styleable.FissionButton_fb_enable_fission, false);
        mButtonDepth = a.getDimension(R.styleable.FissionButton_fb_elevation, Constants.DEFAULT_ELEVATION);
        mDoesPulse = a.getBoolean(R.styleable.FissionButton_fb_pulse, Constants.DEFAULT_DOES_PULSE);
        mMainButtonIconRes = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", Constants.DEFAULT_ICON);
        a.recycle();
        //create main button
        TypedValue v = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.colorAccent, v, true);
        mButtonColor = v.data;
        mMainButton = new FissionButton(mContext);
        //mMainButton.setBackground(getResources().getDrawable(mMainButtonIconRes));
        mMainButton.setImageResource(mMainButtonIconRes);
        mMainButton.setElevation(mButtonDepth);
        mMainButton.setId(R.id.fission_button);
        mMainButton.setAction(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });


        addView(mMainButton, super.generateDefaultLayoutParams());

        //create test button
        if(Constants.TEST_MODE) {
            mTestButton = new FloatingActionButton(mContext);
            mTestButton.setImageResource(mMainButtonIconRes);
            mTestButton.setId(R.id.fission_button_test);
            mTestButton.setElevation(mButtonDepth);
            mTestButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTestButton.setImageResource(R.drawable.ic_arrow_down);
                }
            });

            addView(mTestButton, super.generateDefaultLayoutParams());
        }else{

        }




    }


    private void toggle(){
        isExpanded = !isExpanded;
        if(isExpanded) {
            mMainButton.startAnimation(mExpandAnim);
        }else{
            mMainButton.startAnimation(mCollapseAnim);
        }
    }



    private void setAnimationParams(){
        mExpandAnim = AnimationUtils.loadAnimation(getContext(), R.anim.expand_rotator);
        mCollapseAnim = AnimationUtils.loadAnimation(getContext(), R.anim.collapse_rotator);
    }

    public void setFissionButtonAnims(Animation expandAnimation, Animation collapseAnimation){
        mExpandAnim = expandAnimation;
        mCollapseAnim = collapseAnimation;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mMainButtonY = b - t - mMainButtonHeight - mShadowRadiusDelta;
        mMainButtonX = r - l - mMainButtonWidth;
        mMainButton.layout(mMainButtonX, mMainButtonY, mMainButtonX+mMainButtonWidth, mMainButtonY+mMainButtonHeight);

        if(Constants.TEST_MODE){
            int mTestButtonY = mMainButtonY - mTestButton.getMeasuredHeight() - mShadowRadiusDelta
                    - mButtonSpacing;
            int mTestButtonX = mMainButtonX;
            mTestButton.layout(mTestButtonX, mTestButtonY, mTestButtonX + mTestButton.getMeasuredWidth(), mTestButtonY + mTestButton.getMeasuredHeight());
        }
    }

    @Override
    protected  void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        View mainButton = getChildAt(0);
        mMainButtonWidth = mainButton.getMeasuredWidth();
        mMainButtonHeight = mainButton.getMeasuredHeight();
        mShadowRadiusDelta = (int)getResources().getDimension(R.dimen.button_shadow_width);
        mButtonSpacing = (int)getResources().getDimension(R.dimen.button_spacing);
        int height = mMainButtonHeight + mShadowRadiusDelta + mButtonSpacing;
        int width = mMainButtonWidth + mShadowRadiusDelta;
        if(Constants.TEST_MODE){
            height += mTestButton.getMeasuredHeight() + mButtonSpacing + mShadowRadiusDelta;
        }
        setMeasuredDimension(width, height);


    }
}
