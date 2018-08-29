package com.wdy.base.module.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

/**
 * 作者：王东一 on 2016/4/25 11:15
 **/
public class AnimationUtil {
    //缩放
    public static void addZoomAnimation(View view) {
        float[] vaules = new float[]{0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f};
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules), ObjectAnimator.ofFloat(view, "scaleY", vaules));
        set.setDuration(150);
        set.start();
    }

    //旋转
    public static void addRotateAnimation(View view) {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(200);
        rotateAnimation.setFillAfter(false);
        view.setAnimation(rotateAnimation);
        view.startAnimation(rotateAnimation);
    }

    //旋转
    public static void addRotateAnimation(View view, int star, int end, boolean fillAfter) {
        RotateAnimation rotateAnimation = new RotateAnimation(star, end, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(300);
        rotateAnimation.setFillAfter(fillAfter);
        view.setAnimation(rotateAnimation);
        view.startAnimation(rotateAnimation);
    }

    //向下滑出
    public static void addDownAnimation(View view) {
        int h = 0;
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) view.getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        if (view.getHeight() == 0) {
            h = view.getMeasuredHeight();
        } else {
            h = view.getHeight();
        }
        TranslateAnimation translateAnimation;
        translateAnimation = new TranslateAnimation(0, 0, 0, -h);
        translateAnimation.setDuration(500);
        view.startAnimation(translateAnimation);
    }

    //向下滑出
    public static void addDownAnimation(View view, int height) {
        TranslateAnimation translateAnimation;
        translateAnimation = new TranslateAnimation(0, 0, view.getY(),view.getY()+height);
        translateAnimation.setDuration(500);
        translateAnimation.setFillAfter(true);
        view.startAnimation(translateAnimation);
    }

    //向上滑出
    public static void addUpAnimation(View view) {
        int h = 0;
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) view.getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        if (view.getHeight() == 0) {
            h = view.getMeasuredHeight();
        } else {
            h = view.getHeight();
        }
        TranslateAnimation translateAnimation;
        translateAnimation = new TranslateAnimation(0, 0, -h, 0);
        translateAnimation.setDuration(500);
        view.startAnimation(translateAnimation);
    }

    //向上滑出
    public static void addUpAnimation(View view, int height) {
        TranslateAnimation translateAnimation;
        translateAnimation = new TranslateAnimation(0, 0, view.getY(), view.getY()-height);
        translateAnimation.setDuration(500);
        translateAnimation.setFillAfter(true);
        view.startAnimation(translateAnimation);
    }

    //闪烁
    public static void evasiveAnimator(long AnimatorDelay, View view) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f,
                0f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f,
                1.5f, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f,
                1.5f, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(AnimatorDelay).start();
    }

}
