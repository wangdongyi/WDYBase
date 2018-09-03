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
 * 创建时间：2018/9/3.
 */
public class FailedView extends View {
    private Context context;

    private int width;
    private int height;
    private int radius;

    private float leftStartX;
    private float leftStartY;
    private float leftEndX;
    private float leftEndY;
    private float rightStartX;
    private float rightStartY;
    private float rightEndX;
    private float rightEndY;
    private float length;

    // 图案线宽
    private int lineWidth;

    // 动画线颜色
    private int lineColor = Color.parseColor("#FF705C");

    // 红叉端点到边界的距离
    private int magin;

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

    public FailedView(Context context) {
        this(context, null);
    }

    public FailedView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public FailedView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    private void init() {
        if (lineWidth == 0) lineWidth = dp2px(3);
        if (magin == 0) magin = dp2px(6);

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

        length = 2 * radius - lineWidth - 2 * magin;
        leftStartX = width / 2 - length / 2 * (float) Math.cos(Math.PI / 4);
        leftStartY = height / 2 - length / 2 * (float) Math.sin(Math.PI / 4);
        leftEndX = width / 2 + length / 2 * (float) Math.cos(Math.PI / 4);
        leftEndY = height / 2 + length / 2 * (float) Math.sin(Math.PI / 4);
        rightStartX = width / 2 + length / 2 * (float) Math.cos(Math.PI / 4);
        rightStartY = height / 2 - length / 2 * (float) Math.sin(Math.PI / 4);
        rightEndX = width / 2 - length / 2 * (float) Math.cos(Math.PI / 4);
        rightEndY = height / 2 + length / 2 * (float) Math.sin(Math.PI / 4);
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
            // 画圆圈
            if (progress <= 0.5) {
                canvas.drawArc(new RectF(lineWidth / 2, lineWidth / 2, width - lineWidth / 2, height - lineWidth / 2), -90, -360 * (progress / (float) 0.5), false, circlePaint);
                // 画红叉左上到右下的线
            } else if (progress <= 0.75) {
                canvas.drawArc(new RectF(lineWidth / 2, lineWidth / 2, width - lineWidth / 2, height - lineWidth / 2), -90, -360, false, circlePaint);
                canvas.drawLine(leftStartX, leftStartY, (float) (leftStartX +
                                length * ((progress - 0.5) / 0.25) * Math.cos(Math.PI / 4)),
                        (float) (leftStartY + length * ((progress - 0.5) / 0.25) * Math.sin(Math.PI / 4)), linePaint);
                // 画红叉右上到左下的线
            } else {
                canvas.drawArc(new RectF(lineWidth / 2, lineWidth / 2, width - lineWidth / 2, height - lineWidth / 2), -90, -360, false, circlePaint);
                canvas.drawLine(leftStartX, leftStartY, leftEndX, leftEndY, linePaint);
                canvas.drawLine(rightStartX, rightStartY, (float) (rightStartX -
                                length * ((progress - 0.75) / 0.25) * Math.cos(Math.PI / 4)),
                        (float) (rightStartY + length * ((progress - 0.75) / 0.25) * Math.sin(Math.PI / 4)), linePaint);
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
     * 停止播放动画
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
     * 设置动画时间
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
     * 设置红叉端点到边界的距离
     *
     * @param magin 距离，单位dp
     */
    public void setMagin(int magin) {
        this.magin = dp2px(magin);
    }

    /**
     * 设置动画播放器
     *
     * @param interpolator
     */
    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    /**
     * 设置动画播放回调
     *
     * @param listener
     */
    public void setOnShowAnimationListener(OnShowAnimationListener listener) {
        this.listener = listener;
    }

    /**
     * 动画播放回调
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
