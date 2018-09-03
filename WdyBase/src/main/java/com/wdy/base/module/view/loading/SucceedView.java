package com.wdy.base.module.view.loading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * 作者：王东一
 * 创建时间：2018/9/2.
 */
public class SucceedView extends View {
    private Context context;

    private int width;
    private int height;
    private int radius;

    private float startX;
    private float startY;
    private float middleX;
    private float middleY;
    private float endX;
    private float endY;

    // 图案线宽
    private int lineWidth;

    // 图案线颜色
    private int lineColor = Color.parseColor("#48D2A0");

    // 动画播放时间
    private int duration = 1500;

    // 动画控制器
    private Interpolator interpolator = new AccelerateDecelerateInterpolator();

    private Paint circlePaint;
    private Paint linePaint;
    private float progress;
    private boolean isDraw;

    // 动画播放回调
    private OnShowAnimationListener listener;

    public SucceedView(Context context) {
        this(context, null);
    }

    public SucceedView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SucceedView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    private void init() {
        if (lineWidth == 0) lineWidth = dp2px(3);

        if (circlePaint == null) circlePaint = new Paint();
        circlePaint.setStrokeWidth(lineWidth);
        circlePaint.setColor(lineColor);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeCap(Paint.Cap.ROUND);

        if (linePaint == null) linePaint = new Paint();
        linePaint.setStrokeWidth(1.5f * lineWidth);
        linePaint.setColor(lineColor);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeCap(Paint.Cap.ROUND);

        startX = lineWidth / 2 + radius * 2 / 5;
        startY = lineWidth / 2 + radius;
        middleX = lineWidth / 2 + radius - 2 * lineWidth;
        middleY = lineWidth / 2 + radius * 7 / 5;
        endX = lineWidth / 2 + radius + radius * (float) Math.cos(2 * Math.PI * 37.5 / 360);
        endY = lineWidth + radius - radius * (float) Math.sin(2 * Math.PI * 37.5 / 360);
        start();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getMeasuredWidth();
        height = width;
        radius = (width / 2 - lineWidth / 2);
        getLayoutParams().height = height;
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        doDraw(canvas);
    }

    /**
     * 绘制图案
     *
     * @param canvas
     */
    private void doDraw(Canvas canvas) {
        if (progress > 0) {
            // 画外面的圈
            if (progress <= 0.5) {
                canvas.drawArc(new RectF(lineWidth / 2, lineWidth / 2, width - lineWidth / 2, height - lineWidth / 2), -60, -315 * (progress / (float) 0.6), false, circlePaint);
                // 画勾的左半部分
            } else if (progress <= 0.7) {
                canvas.drawArc(new RectF(lineWidth / 2, lineWidth / 2, width - lineWidth / 2, height - lineWidth / 2), -60, -315, false, circlePaint);
                canvas.drawLine(startX, startY, startX + (middleX - startX) * (progress - 0.5f) / 0.2f, startY + (middleY - startY) * (progress - 0.5f) / 0.2f, linePaint);
                // 画勾的右半部分
            } else {
                canvas.drawArc(new RectF(lineWidth / 2, lineWidth / 2, width - lineWidth / 2, height - lineWidth / 2), -60, -315, false, circlePaint);
                canvas.drawLine(startX, startY, middleX, middleY, linePaint);
                canvas.drawLine(middleX, middleY, middleX + (endX - middleX) * (progress - 0.7f) / 0.3f, middleY + (endY - middleY) * (progress - 0.7f) / 0.3f, linePaint);
            }
        }
    }

    /**
     * 开始播放动画
     */
    public void start() {
        if (isDraw) {
            return;
        }
        isDraw = true;
        ValueAnimator animator = new ValueAnimator().ofFloat(0, 1, 1);
        animator.setDuration(duration);
        animator.setInterpolator(interpolator);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (isDraw) {
                    progress = (float) animation.getAnimatedValue();
                    invalidate();
                }
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (listener != null) listener.onStart();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                isDraw = false;
                if (listener != null) listener.onCancel();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isDraw = false;
                if (listener != null) listener.onFinish();
            }
        });
        animator.start();
    }

    /**
     * 停止播放动画方法
     */
    public void stop() {
        isDraw = false;
    }

    /**
     * 清除动画及图案
     */
    public void clear() {
        isDraw = false;
        progress = 0;
        invalidate();
    }

    /**
     * 设置动画播放时间
     *
     * @param duration 播放时间，单位毫秒
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * 设置图案线颜色
     *
     * @param lineColor 颜色值
     */
    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    /**
     * 设置图案线宽
     *
     * @param lineWidth 线宽，单位dp
     */
    public void setLineWidth(int lineWidth) {
        this.lineWidth = dp2px(lineWidth);
    }

    /**
     * 设置动画控制器
     *
     * @param interpolator
     */
    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    /**
     * 设置动画回调接口
     *
     * @param listener
     */
    public void setOnShowAnimationListener(OnShowAnimationListener listener) {
        this.listener = listener;
    }

    /**
     * 动画回调接口
     */
    public interface OnShowAnimationListener {
        /**
         * 动画开始播放时回调
         */
        void onStart();

        /**
         * 动画播放结束时回调
         */
        void onFinish();

        /**
         * 动画播放取消时回调
         */
        void onCancel();
    }

    private int dp2px(float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}

