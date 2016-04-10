package com.eo5.amoeba.views;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.eo5.amoeba.R;
import com.eo5.amoeba.utils.AnimUtils;
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
    private boolean mIsExpanded =false;

    //layout params
    private int mMainButtonY;
    private int mMainButtonX;
    private int mMainButtonWidth;
    private int mMainButtonHeight;
    private int mShadowRadiusDelta;
    private int mButtonSpacing;
    private float mRotation;
    private AnimUtils.RotatingIcon mIconDrawable;

    //animation params
    private float mStartRotation = 0f;
    private float mEndRotation = 90f;
    private AnimatorSet mExpandAnimSet = new AnimatorSet().setDuration(Constants.DEFAULT_ANIMATION_DURATION);
    private AnimatorSet mCollapseAnimSet = new AnimatorSet().setDuration(Constants.DEFAULT_ANIMATION_DURATION);
    private ObjectAnimator mMainExpandAnimator, mMainCollapseAnimator;

    enum ANIMATION {
        ALPHA, TRANSLATE_Y
    }

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
        mMainButtonIconRes = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", Constants.DEFAULT_ICON);
        a.recycle();
        TypedValue v = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.colorAccent, v, true);
        mIconDrawable = new AnimUtils.RotatingIcon(getResources().getDrawable(mMainButtonIconRes, mContext.getTheme()));
        mButtonColor = v.data;
        mMainButton = new FissionButton(mContext);
        //mMainButton.setImageResource(mMainButtonIconRes);
        mMainButton.setImageDrawable(mIconDrawable);
        mMainButton.setElevation(mButtonDepth);
        mMainButton.setId(R.id.fission_button);
        mMainButton.setAction(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleState();
            }
        });

        addView(mMainButton, super.generateDefaultLayoutParams());

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

    public void doFission(){
        if(mIsExpanded)
            return;

        expandButtons();
    }

    public void doFusion(){
        if(!mIsExpanded)
            return;

        collapseButtons();
    }



    private void toggleState(){
        if(mIsExpanded) {

            collapseButtons();

        }else{

            expandButtons();
        }
    }

    private void collapseButtons(){
        mIsExpanded = false;
        mExpandAnimSet.cancel();
        mCollapseAnimSet.start();
    }

    private void expandButtons(){
        mIsExpanded = true;
        mCollapseAnimSet.cancel();
        mExpandAnimSet.start();
    }

    public void setRotationForIcon(float startRotation, float endRotation){
        mStartRotation = startRotation;
        mEndRotation = endRotation;
    }

    private void setAnimationParamsForMainIcon(){
        mMainExpandAnimator = new ObjectAnimator().ofFloat(mIconDrawable, "rotation", mStartRotation, mEndRotation);
        mMainCollapseAnimator = new ObjectAnimator().ofFloat(mIconDrawable, "rotation", mEndRotation, mStartRotation);
        mMainExpandAnimator.setInterpolator(new OvershootInterpolator());
        mMainCollapseAnimator.setInterpolator(new OvershootInterpolator());
        mExpandAnimSet.play(mMainExpandAnimator);
        mCollapseAnimSet.play(mMainCollapseAnimator);
    }

    private void setChildAnimation(ANIMATION type, View view, float startValue, float endValue){
        ObjectAnimator expandAnimator = new ObjectAnimator();
        ObjectAnimator collapseAnimator = new ObjectAnimator();
        switch (type){
            case ALPHA:
                expandAnimator.setProperty(View.ALPHA);
                expandAnimator.setInterpolator(new DecelerateInterpolator());
                expandAnimator.setFloatValues(startValue, endValue);
                collapseAnimator.setProperty(View.ALPHA);
                collapseAnimator.setInterpolator(new DecelerateInterpolator(4f));
                collapseAnimator.setFloatValues(endValue, startValue);
                expandAnimator.setTarget(view);
                collapseAnimator.setTarget(view);


                break;
            case TRANSLATE_Y:
                expandAnimator.setProperty(View.TRANSLATION_Y);
                expandAnimator.setInterpolator(new OvershootInterpolator());
                expandAnimator.setFloatValues(startValue, endValue);
                collapseAnimator.setProperty(View.TRANSLATION_Y);
                collapseAnimator.setInterpolator(new OvershootInterpolator());
                collapseAnimator.setFloatValues(endValue, startValue);
                expandAnimator.setTarget(view);
                collapseAnimator.setTarget(view);

                break;
            default:
                break;

        }
        AnimUtils.setLayerTypeForView(view, expandAnimator);
        AnimUtils.setLayerTypeForView(view, collapseAnimator);
        mExpandAnimSet.play(expandAnimator);
        mCollapseAnimSet.play(collapseAnimator);
    }




    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mMainButtonY = b - t - mMainButtonHeight - mShadowRadiusDelta;
        mMainButtonX = r - l - mMainButtonWidth - mShadowRadiusDelta;
        mMainButton.layout(mMainButtonX, mMainButtonY, mMainButtonX + mMainButtonWidth, mMainButtonY + mMainButtonHeight);
        setAnimationParamsForMainIcon();
        if(Constants.TEST_MODE){
            int mTestButtonY = mMainButtonY - mTestButton.getMeasuredHeight() - mShadowRadiusDelta
                    - mButtonSpacing;
            int mTestButtonX = mMainButtonX;
            mTestButton.layout(mTestButtonX, mTestButtonY, mTestButtonX + mTestButton.getMeasuredWidth(), mTestButtonY + mTestButton.getMeasuredHeight());
            float collapseY = mMainButtonY - mTestButtonY;
            float expandY = 0f;
            mTestButton.setTranslationY(mIsExpanded? expandY: collapseY);
            mTestButton.setAlpha(mIsExpanded ? 1f : 0f);
            setChildAnimation(ANIMATION.TRANSLATE_Y, mTestButton, collapseY, expandY);
            setChildAnimation(ANIMATION.ALPHA, mTestButton, 0f, 1f);

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

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        bringChildToFront(mMainButton);
    }



}
