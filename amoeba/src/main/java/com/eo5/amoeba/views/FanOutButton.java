package com.eo5.amoeba.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.eo5.amoeba.R;
import com.eo5.amoeba.utils.AnimUtils;
import com.eo5.amoeba.utils.Constants;

/**
 * Created by Sriram on 4/8/2016.
 */
public class FanOutButton extends ViewGroup{

    private static final int DEFAULT_BG_FANOUT_DURATION = 300;
    private static final int DEFAULT_ICON_FANOUT_DURATION = 350;
    private static final int DEFAULT_ICON_FANOUT_OFFSET = 100;

    private Context mContext;
    private FissionButton mMainButton;
    private ArcView mArcView;
    private float mButtonElevation;
    private int mMainButtonIconRes;
    private AnimUtils.RotatingIcon mIconDrawable;
    private int mButtonColor;
    private int mMainButtonWidth;
    private int mShadowRadiusDelta;
    private int mMainButtonHeight;
    private int mButtonSpacing;
    private boolean mIsBackdropEnabled;
    private int mBackdropColor;
    private int mOuterPadding;
    private int mMainButtonY;
    private int mMainButtonX;
    private int mFanoutRadius;
    private int mTopButtonRes;
    private int mLeftButtonRes;
    private int mAngledButtonRes;
    private FloatingActionButton mTopButton;
    private FloatingActionButton mLeftButton;
    private FloatingActionButton mAngledButton;
    private int mTopButtonY;
    private int mLeftButtonX;
    private int mAngledButtonX;
    private int mAngledButtonY;
    private int mOriginX;
    private int mOriginY;
    private int mCircleViewX;
    private int mCircleViewY;
    private int mFanMargin;
    private Canvas mCanvas;

    //paint params
    private Paint mCirclePaint;


    //animation params
    private float mStartRotation;
    private float mEndRotation;
    private boolean mIsExpanded = false;
    private AnimatorSet mMainExpandSet = new AnimatorSet().setDuration(Constants.DEFAULT_ANIMATION_DURATION);
    private AnimatorSet mMainCollapseSet = new AnimatorSet().setDuration(Constants.DEFAULT_ANIMATION_DURATION);



    public FanOutButton(Context context){
        this(context, null, 0);

    }

    public FanOutButton(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public FanOutButton(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        final TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.FanOutButton);
        mButtonElevation = a.getDimension(R.styleable.FanOutButton_fan_elevation, Constants.DEFAULT_ELEVATION);
        //mMainButtonIconRes = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", Constants.DEFAULT_ICON);
        mMainButtonIconRes = a.getResourceId(R.styleable.FanOutButton_fan_main_icon, Constants.DEFAULT_ICON);

        TypedValue v = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.colorAccent, v, true);
        mIconDrawable = new AnimUtils.RotatingIcon(getResources().getDrawable(mMainButtonIconRes, mContext.getTheme()));
        mButtonColor = v.data;
        mBackdropColor = a.getColor(R.styleable.FanOutButton_fan_backdrop_color, mButtonColor);
        mIsBackdropEnabled = a.getBoolean(R.styleable.FanOutButton_fan_enable_backdrop, true);
        mFanMargin = (int) Math.ceil(a.getDimension(R.styleable.FanOutButton_fan_margin, Constants.DEFAULT_MARGIN));
        mTopButtonRes = a.getResourceId(R.styleable.FanOutButton_fan_top_icon, Constants.DEFAULT_ICON);
        mLeftButtonRes = a.getResourceId(R.styleable.FanOutButton_fan_left_icon, Constants.DEFAULT_ICON);
        mAngledButtonRes = a.getResourceId(R.styleable.FanOutButton_fan_angled_icon, Constants.DEFAULT_ICON);
        mStartRotation = a.getInteger(R.styleable.FanOutButton_fan_start_rotation, Constants.DEFAULT_START_ROTATION);
        mEndRotation = a.getInteger(R.styleable.FanOutButton_fan_end_rotation, Constants.DEFAULT_END_ROTATION);
        mMainButton = new FissionButton(mContext);
        //mMainButton.setImageResource(mMainButtonIconRes);
        mMainButton.setImageDrawable(mIconDrawable);
        mMainButton.setElevation(mButtonElevation);
        mMainButton.setId(R.id.fanout_button_main);
        mMainButton.setAction(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleState();
            }
        });

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(mBackdropColor);
        if(mIsBackdropEnabled) {
            mArcView = new ArcView(mContext, mBackdropColor);
            addView(mArcView);
        }


        addView(mMainButton, generateDefaultLayoutParams());
        addView(mTopButton = createChildButton(mTopButtonRes, R.id.fanout_button_top), generateDefaultLayoutParams());
        addView(mLeftButton = createChildButton(mLeftButtonRes, R.id.fanout_button_left), generateDefaultLayoutParams());
        addView(mAngledButton = createChildButton(mAngledButtonRes, R.id.fanout_button_angled), generateDefaultLayoutParams());
        a.recycle();
    }

    private FloatingActionButton createChildButton(@DrawableRes int iconResource, @IdRes int id){
        FloatingActionButton newButton = new FloatingActionButton(mContext);
        newButton.setElevation(mButtonElevation);
        newButton.setImageResource(iconResource);
        newButton.setId(id);
        return newButton;
    }

    public ViewGroup getAnchorLayout(){
        return this;
    }

    private void toggleState(){
        if(mIsExpanded){
            collapseButtons();
        }else{
            expandButtons();
        }

    }

    private void expandButtons(){
        mIsExpanded = true;

        mMainCollapseSet.cancel();
        mMainExpandSet.start();

    }

    private void collapseButtons(){
        mIsExpanded = false;
        mMainExpandSet.cancel();
        mMainCollapseSet.start();
    }




    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mMainButtonY = b - t - mMainButtonHeight - mShadowRadiusDelta - mFanMargin;
        mMainButtonX = r - l - mMainButtonWidth - mShadowRadiusDelta - mFanMargin;
        mOriginX = mMainButtonX + mMainButtonWidth/2;
        mOriginY = mMainButtonY + mMainButtonHeight/2;

        mMainButton.layout(mMainButtonX, mMainButtonY, mMainButtonX + mMainButtonWidth, mMainButtonY + mMainButtonHeight);

        mCircleViewY = b - mFanMargin - mMainButtonHeight/2;
        mCircleViewX = r - mFanMargin - mMainButtonWidth/2;


        //layout params for top button

        mTopButtonY = b - t - mFanoutRadius + mOuterPadding;
        mTopButton.layout(mMainButtonX, mTopButtonY, mMainButtonX + mMainButtonWidth, mTopButtonY+mMainButtonHeight);

        mLeftButtonX = r - l - mFanoutRadius + mOuterPadding;
        mLeftButton.layout(mLeftButtonX, mMainButtonY, mLeftButtonX + mMainButtonWidth, mMainButtonY + mMainButtonHeight);

        int circleRadius = Math.max((mFanoutRadius -mOuterPadding-mMainButtonHeight-mShadowRadiusDelta - mFanMargin),(mFanoutRadius -mOuterPadding-mMainButtonWidth-mShadowRadiusDelta - mFanMargin));
        if(mIsBackdropEnabled) {
            mArcView.layout(r - l - mFanoutRadius, b - t - mFanoutRadius, r - l, b - t);
        }

        int angledButtonCenterX = (int) (mOriginX - circleRadius* Math.cos(45 * Math.PI/180f));
        int angledButtonCenterY = (int) (mOriginY - circleRadius* Math.sin(45 * Math.PI/180f));

        mAngledButtonX = angledButtonCenterX-mMainButtonWidth / 2;
        mAngledButtonY = angledButtonCenterY - mMainButtonHeight / 2;
        mAngledButton.layout(mAngledButtonX, mAngledButtonY, mAngledButtonX + mMainButtonWidth, mAngledButtonY + mMainButtonHeight);
        setAnimationParams();

        //circleview for backdrop

        bringChildToFront(mMainButton);
    }

    private void setAnimationParams(){
        Animator mainExpandAnimator = new ObjectAnimator().ofFloat(mIconDrawable, "rotation", mStartRotation, mEndRotation);
        Animator mainCollapseAnimator = new ObjectAnimator().ofFloat(mIconDrawable, "rotation", mEndRotation, mStartRotation);
        mainCollapseAnimator.setInterpolator(new OvershootInterpolator());
        mainExpandAnimator.setInterpolator(new OvershootInterpolator());
        mMainExpandSet.play(mainExpandAnimator);
        mMainCollapseSet.play(mainCollapseAnimator);

        int topTranslationY = mMainButtonY - mTopButtonY;
        mTopButton.setTranslationY(mIsExpanded ? 0f : topTranslationY);
        mTopButton.setAlpha(mIsExpanded ? 1f : 0f);
        addToAnimatorSet(AnimUtils.setChildAnimation(AnimUtils.ANIMATION.TRANSLATE_Y, mTopButton, topTranslationY, 0f, DEFAULT_ICON_FANOUT_DURATION, DEFAULT_ICON_FANOUT_OFFSET, DEFAULT_ICON_FANOUT_OFFSET*3));
        addToAnimatorSet(AnimUtils.setChildAnimation(AnimUtils.ANIMATION.ALPHA, mTopButton, 0f, 1f, DEFAULT_ICON_FANOUT_DURATION, DEFAULT_ICON_FANOUT_OFFSET, DEFAULT_ICON_FANOUT_OFFSET*3));

        int leftTranslationX = mMainButtonX - mLeftButtonX;
        mLeftButton.setTranslationX(mIsExpanded ? 0f : leftTranslationX);
        mLeftButton.setAlpha(mIsExpanded ? 1f : 0f);
        addToAnimatorSet(AnimUtils.setChildAnimation(AnimUtils.ANIMATION.TRANSLATE_X, mLeftButton, leftTranslationX, 0f, DEFAULT_ICON_FANOUT_DURATION, DEFAULT_ICON_FANOUT_OFFSET*3, DEFAULT_ICON_FANOUT_OFFSET));
        addToAnimatorSet(AnimUtils.setChildAnimation(AnimUtils.ANIMATION.ALPHA, mLeftButton, 0f, 1f, DEFAULT_ICON_FANOUT_DURATION, DEFAULT_ICON_FANOUT_OFFSET*3, DEFAULT_ICON_FANOUT_OFFSET));

        int angledTranslationX = mMainButtonX - mAngledButtonX;
        int angledTranslationY = mMainButtonY - mAngledButtonY;
        mAngledButton.setTranslationX(mIsExpanded ? 0f : angledTranslationX);
        mAngledButton.setTranslationY(mIsExpanded ? 0f : angledTranslationY);
        mAngledButton.setAlpha(mIsExpanded ? 1f : 0f);
        addToAnimatorSet(AnimUtils.setChildAnimation(AnimUtils.ANIMATION.TRANSLATE_X, mAngledButton, angledTranslationX, 0f, DEFAULT_ICON_FANOUT_DURATION, DEFAULT_ICON_FANOUT_OFFSET*2, DEFAULT_ICON_FANOUT_OFFSET*2));
        addToAnimatorSet(AnimUtils.setChildAnimation(AnimUtils.ANIMATION.TRANSLATE_Y, mAngledButton, angledTranslationY, 0f, DEFAULT_ICON_FANOUT_DURATION, DEFAULT_ICON_FANOUT_OFFSET*2, DEFAULT_ICON_FANOUT_OFFSET*2));
        addToAnimatorSet(AnimUtils.setChildAnimation(AnimUtils.ANIMATION.ALPHA, mAngledButton, 0f, 1f, DEFAULT_ICON_FANOUT_DURATION, DEFAULT_ICON_FANOUT_OFFSET*2, DEFAULT_ICON_FANOUT_OFFSET*2));


    }

    private void addToAnimatorSet(AnimUtils.AnimatorGroup animGroup){
        mMainExpandSet.play(animGroup.expandAnimator);
        mMainCollapseSet.play(animGroup.collapseAnimator);
    }

    @Override
    protected  void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        View mainButton = getChildAt(0);
        mMainButtonWidth = mainButton.getMeasuredWidth();
        mMainButtonHeight = mainButton.getMeasuredHeight();
        mShadowRadiusDelta = (int)getResources().getDimension(R.dimen.button_shadow_width);
        mButtonSpacing = (int)getResources().getDimension(R.dimen.fanout_button_spacing);
        mOuterPadding = (int)getResources().getDimension(R.dimen.fanout_outer_padding);
        int height = mMainButtonHeight + mButtonSpacing + mMainButtonHeight + mOuterPadding + mShadowRadiusDelta + mFanMargin;
        int width = mMainButtonWidth + mButtonSpacing + mMainButtonWidth + mOuterPadding + mShadowRadiusDelta + mFanMargin;
        mFanoutRadius = Math.max(height, width);
        setMeasuredDimension(width, height);


    }
}
