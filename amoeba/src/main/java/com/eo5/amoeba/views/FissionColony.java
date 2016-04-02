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
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

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
    private boolean mIsExpanded =false;

    //layout params
    private int mMainButtonY;
    private int mMainButtonX;
    private int mMainButtonWidth;
    private int mMainButtonHeight;
    private int mShadowRadiusDelta;
    private int mButtonSpacing;
    private float mRotation;
    private RotatingIcon mIconDrawable;

    //animation params
    private float mStartRotation = 0f;
    private float mEndRotation = 90f;
    private Animation mMainExpandAnim, mMainCollapseAnim;
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
        mIconDrawable = new RotatingIcon(getResources().getDrawable(mMainButtonIconRes, mContext.getTheme()));
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


    private void toggleState(){
        if(mIsExpanded) {

            doFusion();

        }else{

            doFission();
        }
    }

    private void doFusion(){
        mIsExpanded = false;
        //mMainButton.startAnimation(mMainCollapseAnim);
        mExpandAnimSet.cancel();
        mCollapseAnimSet.start();
    }

    private void doFission(){
        mIsExpanded = true;
        //mMainButton.startAnimation(mMainExpandAnim);
        mCollapseAnimSet.cancel();
        mExpandAnimSet.start();


    }



    public void setRotationForIcon(float startRotation, float endRotation){
        mStartRotation = startRotation;
        mEndRotation = endRotation;
    }

    private void setAnimationParams(){
       // mMainExpandAnim = AnimationUtils.loadAnimation(getContext(), R.anim.expand_rotator);
       // mMainCollapseAnim = AnimationUtils.loadAnimation(getContext(), R.anim.collapse_rotator);
        mMainExpandAnimator = new ObjectAnimator().ofFloat(mIconDrawable, "rotation", mStartRotation, mEndRotation);
        mMainCollapseAnimator = new ObjectAnimator().ofFloat(mIconDrawable, "rotation", mEndRotation, mStartRotation);
        //mMainExpandAnimator.setDuration(200);
        mMainExpandAnimator.setInterpolator(new OvershootInterpolator());
       // mMainExpandAnimator.setPropertyName("rotation");
        //mMainExpandAnimator.setFloatValues(0f, 0.25f);


        //mMainCollapseAnimator.setDuration(200);
        mMainCollapseAnimator.setInterpolator(new OvershootInterpolator());
        //mMainCollapseAnimator.setPropertyName("rotation");
        //mMainCollapseAnimator.setFloatValues(0.25f, 0f);

        //mMainExpandAnimator.setTarget(mMainButton);
        //mMainCollapseAnimator.setTarget(mMainButton);
        mExpandAnimSet.setDuration(Constants.DEFAULT_ANIMATION_DURATION);
        mCollapseAnimSet.setDuration(Constants.DEFAULT_ANIMATION_DURATION);
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
                collapseAnimator.setInterpolator(new DecelerateInterpolator(2f));
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
        setLayerTypeForView(view, expandAnimator);
        setLayerTypeForView(view, collapseAnimator);
        mExpandAnimSet.play(expandAnimator);
        mCollapseAnimSet.play(collapseAnimator);
    }

    private void setLayerTypeForView(final View view, Animator animator){
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setLayerType(LAYER_TYPE_NONE, null);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                view.setLayerType(LAYER_TYPE_HARDWARE, null);
            }
        });
    }

    public void setFissionButtonAnims(Animation expandAnimation, Animation collapseAnimation){
        mMainExpandAnim = expandAnimation;
        mMainCollapseAnim = collapseAnimation;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mMainButtonY = b - t - mMainButtonHeight - mShadowRadiusDelta;
        mMainButtonX = r - l - mMainButtonWidth;
        mMainButton.layout(mMainButtonX, mMainButtonY, mMainButtonX + mMainButtonWidth, mMainButtonY + mMainButtonHeight);
        setAnimationParams();
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


    private static class RotatingIcon extends LayerDrawable {
        public RotatingIcon(Drawable drawable) {
            super(new Drawable[] { drawable });
        }

        private float mRotation;

        @SuppressWarnings("UnusedDeclaration")
        public float getRotation() {
            Log.v(Constants.TAG, "get rotation:"+mRotation);
            return mRotation;
        }

        @SuppressWarnings("UnusedDeclaration")
        public void setRotation(float rotation) {
            Log.v(Constants.TAG, "set rotation:"+rotation);
            mRotation = rotation;
            invalidateSelf();
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.save();
            canvas.rotate(mRotation, getBounds().centerX(), getBounds().centerY());
            super.draw(canvas);
            canvas.restore();
        }
    }
}
