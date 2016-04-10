package com.eo5.amoeba.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Created by Sriram on 3/29/2016.
 */
public class AnimUtils {

    public enum ANIMATION {
        ALPHA, TRANSLATE_Y, TRANSLATE_X, TRANSLATE_XY
    }

    public static class AnimatorGroup {
        public Animator expandAnimator;
        public Animator collapseAnimator;

        public AnimatorGroup(Animator anim1, Animator anim2){
            expandAnimator = anim1;
            collapseAnimator = anim2;
        }
    }

    public static class RotatingIcon extends LayerDrawable {
        public RotatingIcon(Drawable drawable) {
            super(new Drawable[] { drawable });
        }

        private float mRotation;

        @SuppressWarnings("UnusedDeclaration")
        public float getRotation() {
            return mRotation;
        }

        @SuppressWarnings("UnusedDeclaration")
        public void setRotation(float rotation) {
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

    public static void setLayerTypeForView(final View view, Animator animator){
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setLayerType(ViewGroup.LAYER_TYPE_NONE, null);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                view.setLayerType(ViewGroup.LAYER_TYPE_HARDWARE, null);
            }
        });
    }

    public static AnimatorGroup setChildAnimation(ANIMATION type, View view, float startValue, float endValue, int duration, int startoffset){
        ObjectAnimator expandAnimator = new ObjectAnimator();
        ObjectAnimator collapseAnimator = new ObjectAnimator();
        switch (type){
            case ALPHA:
                expandAnimator.setProperty(View.ALPHA);
                expandAnimator.setInterpolator(new DecelerateInterpolator());
                collapseAnimator.setProperty(View.ALPHA);
                collapseAnimator.setInterpolator(new DecelerateInterpolator(4f));
                break;
            case TRANSLATE_Y:
                expandAnimator.setProperty(View.TRANSLATION_Y);
                expandAnimator.setInterpolator(new OvershootInterpolator());
                collapseAnimator.setProperty(View.TRANSLATION_Y);
                collapseAnimator.setInterpolator(new OvershootInterpolator());
                break;

            case TRANSLATE_X:
                expandAnimator.setProperty(View.TRANSLATION_X);
                expandAnimator.setInterpolator(new OvershootInterpolator());
                collapseAnimator.setProperty(View.TRANSLATION_X);
                collapseAnimator.setInterpolator(new OvershootInterpolator());
                break;
            case TRANSLATE_XY:
                break;
            default:
                break;

        }
        expandAnimator.setFloatValues(startValue, endValue);
        collapseAnimator.setFloatValues(endValue, startValue);
        expandAnimator.setTarget(view);
        collapseAnimator.setTarget(view);
        expandAnimator.setDuration(duration);
        collapseAnimator.setDuration(duration);
        expandAnimator.setStartDelay(startoffset);
        AnimUtils.setLayerTypeForView(view, expandAnimator);
        AnimUtils.setLayerTypeForView(view, collapseAnimator);

        return new AnimatorGroup(expandAnimator, collapseAnimator);

    }


}
